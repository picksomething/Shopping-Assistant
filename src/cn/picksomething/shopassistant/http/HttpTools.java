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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpTools {

	private static ArrayList<String> goodIDArray;
	private static ArrayList<String> goodsNameArray;
	private static ArrayList<String> goodsLinkArray;
	private static ArrayList<String> goodsImageLinkArray;
	private static ArrayList<Bitmap> goodsImageArray;
	private static ArrayList<HashMap<String, Object>> jsonDataList;
	private static ArrayList<HashMap<String, Object>> finalDataList;

	public static void init() {
		goodIDArray = new ArrayList<String>();
		goodsNameArray = new ArrayList<String>();
		goodsLinkArray = new ArrayList<String>();
		goodsImageLinkArray = new ArrayList<String>();
		goodsImageArray = new ArrayList<Bitmap>();
		jsonDataList = new ArrayList<HashMap<String, Object>>();
		finalDataList = new ArrayList<HashMap<String, Object>>();
	}

	public static void emptyArray() {
		goodIDArray = null;
		goodsNameArray = null;
		goodsLinkArray = null;
		goodsImageLinkArray = null;
		goodsImageArray = null;
		jsonDataList = null;
		finalDataList = null;
	}

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
		return result;
	}

	public static String getDataByPost(String keyword, String url) {
		String results = null;
		// Creating HTTP client
		HttpClient httpClient = new DefaultHttpClient();
		// Creating HTTP Post
		HttpPost httpPost = new HttpPost(url);
		// Building post parameters
		// key and value pair
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("hotwords", keyword));
		nameValuePair.add(new BasicNameValuePair("enc", "utf-8"));
		// Url Encoding the POST parameters
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// writing error to Log
			e.printStackTrace();
		}
		// Making HTTP Request
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (200 == response.getStatusLine().getStatusCode()) {
				results = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.d("caobin", "results = " + results);
			}else{
				Log.d("caobin", "httppost request failed");
			}
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}
		return results;
	}
	
	public static Bitmap getGoodsImage(String path) throws IOException{
		Bitmap bitmap = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200){
				InputStream inStream = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inStream);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
		
	}

	public static ArrayList<String> matchResults(String result, String regx) {
		int resultNum = 0;
		ArrayList<String> matchArray = new ArrayList<String>();
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(result);
		while (m.find() && (5 > resultNum)) {
			resultNum++;
			MatchResult mr = m.toMatchResult();
			matchArray.add(mr.group(1));
			Log.d("picksomething", "matchResult = " + mr.group(1));
		}
		return matchArray;

	}
	
	/**
	 * 
	 * @author caobin
	 * @created 2014年11月14日
	 * @param inputString
	 * @return
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}

	public static ArrayList<HashMap<String, Object>> getJsonDataByID(String url, String goodName) throws IOException {
		String regxID = "sku=\"(.*?)\"";
		String regxName = "<div class=\"p-name\">\\n.*?<a target=\"_blank\" href=\".*?\" onclick=\".*?\">\\n\\s+(.*?) class='adwords' .*?></font>";
		String regxLink = "<div class=\"p-img\">\\n\\s+<a target=\"_blank\" href=\"(.*?)\" onclick=\".*?\">";
		String regxImageLink = "<img width=\"220\".*? data-lazyload=\"(.*?)\" />";
		String searchResultString = null;

		searchResultString = doPost(null, url);
		goodsLinkArray = matchResults(searchResultString, regxLink);
		goodsImageLinkArray = matchResults(searchResultString, regxImageLink);
		goodIDArray = matchResults(searchResultString, regxID);
		goodsNameArray = (matchResults(searchResultString, regxName));
		Iterator<String> id = goodIDArray.iterator();
		String jsonItem = null;
		while (id.hasNext()) {
			String tempurl = id.next();
			String jsonUrl = "http://p.3.cn/prices/mgets?skuIds=J_" + tempurl;
			jsonItem = searchInJD(jsonUrl);
			try {
				finalDataList = AnalysisJson(jsonItem);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return finalDataList;

	}

	public static ArrayList<HashMap<String, Object>> AnalysisJson(String jsonItem) throws JSONException {
		JSONArray jsonArray = null;
		jsonArray = new JSONArray(jsonItem);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			// 初始化HashMap
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", jsonObject.getString("id"));
			map.put("price", jsonObject.getString("p"));
			jsonDataList.add(map);
		}
		return jsonDataList;
	}

	public static void addNameToList() throws IOException {
		for (int j = 0; j < goodsNameArray.size(); j++) {
			HashMap<String, Object> goodMap = jsonDataList.get(j);
			String nameForFilter = goodsNameArray.get(j)+">";
			String realName = htmlRemoveTag(nameForFilter);
			Log.d("picksomething", "after filter realName = " + realName);
			goodsImageArray.add(getGoodsImage(goodsImageLinkArray.get(j)));
			goodMap.put("name", realName);
			goodMap.put("link", goodsLinkArray.get(j));
			goodMap.put("image", goodsImageArray.get(j));
		}
	}
}
