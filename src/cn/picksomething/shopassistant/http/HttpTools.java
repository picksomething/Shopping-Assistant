package cn.picksomething.shopassistant.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpTools {
	
	private static ArrayList<String> goodIDArray = new ArrayList<String>();
	private static ArrayList<String> goodsNameArray = new ArrayList<String>();

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月6日
	 * @param jdUrl
	 * @return
	 * @throws IOException
	 */
	public static String searchInJD(String jdUrl) throws IOException {
		String resultData = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HttpURLConnection connection = null;
		InputStream inStream = null;
		URL url = null;
		byte[] data = new byte[1024];
		int len = 0;
		try {
			url = new URL(jdUrl);
			connection = (HttpURLConnection) url.openConnection();
			inStream = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		while ((len = inStream.read(data)) != -1) {
			outputStream.write(data, 0, len);
		}
		inStream.close();
		resultData = new String(outputStream.toByteArray());
		Log.d("caobin", "resultData = " + resultData);
		return resultData;
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月6日
	 * @param params
	 * @param url
	 * @return
	 */
	public static String doPost(List<NameValuePair> params, String url) {
		String result = null;
		// get HttpClient intance
		HttpClient httpClient = new DefaultHttpClient();
		// get HttpPost instance
		HttpPost httpPost = new HttpPost(url);
		if (null != params) {
			try {
				// 设置字符集
				HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				// 设置参数实体
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 连接超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
		try {
			// get HttpResponse instance
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			} else {
				Log.d("caobin", "httppost request failed");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("caobin", "result = " + result);
		return result;
	}

	public static ArrayList<String> matchResults(String result, String regx) {
		int goodsNum = 0;
		ArrayList<String> idArray = new ArrayList<String>();
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(result);

		while (m.find() && (5 > goodsNum)) {
			goodsNum++;
			MatchResult mr = m.toMatchResult();
			Log.d("picksomething", "id = " + mr.group(1));
			idArray.add(mr.group(1));
		}
		return idArray;

	}

	public static ArrayList<HashMap<String, Object>> getJsonDataByID(String url) throws IOException {
		
		String regxID = "sku=\"(.*?)\"";
		String regxName = "<title>(.*?)</title>";
		ArrayList<HashMap<String, Object>> finalDatas = new ArrayList<HashMap<String, Object>>();
		goodIDArray = matchResults(doPost(null, url), regxID);
		Iterator<String> id = goodIDArray.iterator();
		String jsonItem = null;
		while (id.hasNext()) {
			String tempurl = id.next();
			Log.d("picksomething", "temp = " + tempurl);
			String detailUrl = "http://item.jd.com/"+tempurl + ".html";
			String jsonUrl = "http://p.3.cn/prices/mgets?skuIds=J_" + tempurl;
			goodsNameArray = matchResults(doPost(null, detailUrl),regxName);
			jsonItem = searchInJD(jsonUrl);
			try {
				finalDatas = AnalysisJson(jsonItem);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return finalDatas;

	}

	public static ArrayList<HashMap<String, Object>> AnalysisJson(String jsonItem) throws JSONException {
		JSONArray jsonArray = null;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		jsonArray = new JSONArray(jsonItem);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			// 初始化HashMap
			HashMap<String, Object> map = new HashMap<String, Object>();
			// json sample:[{"id":"J_62939582","p":"2.99","m":"2.99"}]
			map.put("id", jsonObject.getString("id"));
			map.put("name", goodsNameArray.get(i));
			map.put("price", jsonObject.getString("p"));
			map.put("originPrice", jsonObject.getString("m"));
			list.add(map);
		}
		return list;
	}
}
