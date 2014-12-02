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
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpTools {

	private static ArrayList<HashMap<String, Object>> finalResults;
	private static String regxID;
	private static String regxName;
	private static String regxImageLink;
	private static String regxPrice;
	private static String jdResultString;
	private static String tmallResultString;
	private static String suningResultString;

	public static void init() {
		finalResults = new ArrayList<HashMap<String, Object>>();
	}

	public static void emptyArray() {
		finalResults = null;
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月6日
	 * @param jdUrl
	 * @return
	 * @throws IOException
	 */
	public static String requestJdJson(String jdUrl) throws IOException {
		Log.d("picksomething", "** call getJdPrice **");
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
	 * @param url
	 * @return
	 */
	public static String getStringResult(List<NameValuePair> params, String url) {
		String result = null;
		// get HttpClient intance
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Custom user agent");
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
				Log.d("caobin", "httppost request failed and code = " + httpResponse.getStatusLine().getStatusCode());
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
			} else {
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

	public static Bitmap getGoodsImage(String path) throws IOException {
		Bitmap bitmap = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
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
		while (m.find() && (4 > resultNum)) {
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
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
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

	public static ArrayList<HashMap<String, Object>> getFinalReslut(String url, int source) throws IOException {
		Log.d("caobin", "url = " + url);
		if (0 == source) {
			regxID = "sku=\"(.*?)\"";
			regxName = "<div class=\"p-name\">\\n\\s+<.*?>\\n\\s+(.*?) class='adwords' .*?></font>";
			regxImageLink = "<img width=\"220\".*? data-lazyload=\"(.*?)\" />";
			jdResultString = getStringResult(null, url);
			ArrayList<String> jdIDArray = matchResults(jdResultString, regxID);
			ArrayList<String> jdNameArray = filterJdName(matchResults(jdResultString, regxName));
			ArrayList<String> jdImageLinkArray = matchResults(jdResultString, regxImageLink);
			ArrayList<Bitmap> jdBitmapArray = getBitmapArray(jdImageLinkArray);
			ArrayList<String> jdPriceArray = getJdPrice(jdIDArray);
			ArrayList<String> jdDetailLinkAddr = getDetailLinkByID(jdIDArray, source);
			ArrayList<HashMap<String, Object>> jdResults = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < jdIDArray.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("goodName", jdNameArray.get(i));
				map.put("goodBitmap", jdBitmapArray.get(i));
				map.put("goodPrice", jdPriceArray.get(i));
				map.put("detailLink", jdDetailLinkAddr.get(i));
				map.put("goodSource", "京东");
				jdResults.add(map);
			}
			finalResults.addAll(jdResults);
		} else if (1 == source) {
			regxID = "<div class=\"product\" data-id=\"(.*?)\"";
			regxName = "title=\"(.*?)\" data-p=\".*?\" >";
			regxImageLink = "<img  src=  \"(.*?)\" />";
			regxPrice = "<em title=\"(.*?)\">";
			tmallResultString = getStringResult(null, url);
			ArrayList<String> tmallIDArray = matchResults(tmallResultString, regxID);
			ArrayList<String> tmallNameArray = matchResults(tmallResultString, regxName);
			ArrayList<String> tmallImageLinkArray = matchResults(tmallResultString, regxImageLink);
			ArrayList<Bitmap> tmallBitmapArray = getBitmapArray(tmallImageLinkArray);
			ArrayList<String> tmallPriceArray = matchResults(tmallResultString, regxPrice);
			ArrayList<String> tmallDetailLinkAddr = getDetailLinkByID(tmallIDArray, source);
			ArrayList<HashMap<String, Object>> tmallResults = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < tmallIDArray.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("goodName", tmallNameArray.get(i));
				map.put("goodBitmap", tmallBitmapArray.get(i));
				map.put("goodPrice", tmallPriceArray.get(i));
				map.put("goodSource", "天猫商城");
				map.put("detailLink", tmallDetailLinkAddr.get(i));
				tmallResults.add(map);
			}
			finalResults.addAll(tmallResults);
		} else if (2 == source) {
			regxID = "<li class=\".*?\"  name=\"000000000(.*?)\">";
			regxName = "<a title=\"(.*?)\" class=\"search-bl\"";
			regxImageLink = "<img class=\"err-product\" src=\"(.*?)\"";
			suningResultString = getStringResult(null, url);
			ArrayList<String> suningIDArray = matchResults(suningResultString, regxID);
			ArrayList<String> suningNameArray = matchResults(suningResultString, regxName);
			ArrayList<String> suningImageLinkArray = matchResults(suningResultString, regxImageLink);
			ArrayList<Bitmap> suningBitmapArray = getBitmapArray(suningImageLinkArray);
			ArrayList<Bitmap> suningPriceArray = getSuningPrice(suningIDArray);
			ArrayList<String> suningDetailLinkAddr = getDetailLinkByID(suningIDArray, source);
			ArrayList<HashMap<String, Object>> suningResults = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < suningIDArray.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("goodName", suningNameArray.get(i));
				map.put("goodBitmap", suningBitmapArray.get(i));
				map.put("goodPrice", suningPriceArray.get(i));
				map.put("goodSource", "苏宁易购");
				map.put("detailLink", suningDetailLinkAddr.get(i));
				suningResults.add(map);
			}
			finalResults.addAll(suningResults);
		}
		return finalResults;
	}

	private static ArrayList<String> getDetailLinkByID(ArrayList<String> jdIDArray, int source) {
		ArrayList<String> tempLinkArray = new ArrayList<String>();
		String tempLinkStr = null;
		for (int i = 0; i < jdIDArray.size(); i++) {
			if (0 == source) {
				tempLinkStr = "http://item.jd.com/" + jdIDArray.get(i) + ".html";
			} else if (1 == source) {
				tempLinkStr = "http://detail.m.tmall.com/item.htm?id=" + jdIDArray.get(i);
			} else if (2 == source) {
				tempLinkStr = "http://m.suning.com/product/"+jdIDArray.get(i)+".html";
			}
			tempLinkArray.add(i, tempLinkStr);
		}
		return tempLinkArray;
	}

	private static ArrayList<String> getJdPrice(ArrayList<String> jdIDArray) throws IOException {
		Log.d("picksomething", "** call getJdPrice ** jdIDArray.size = " + jdIDArray.size());
		Iterator<String> id = jdIDArray.iterator();
		ArrayList<String> tempArray = new ArrayList<String>();
		String jsonItem = null;
		while (id.hasNext()) {
			String tempurl = id.next();
			String jsonUrl = "http://p.3.cn/prices/mgets?skuIds=J_" + tempurl;
			Log.d("picksomething", "start get good json url");
			jsonItem = requestJdJson(jsonUrl);
			try {
				tempArray.add(parseJson(jsonItem));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.d("picksomething", "** call getJdPrice ** tempArray.size = " + tempArray.size());
		return tempArray;
	}

	private static ArrayList<String> filterJdName(ArrayList<String> jdNameArray) {
		ArrayList<String> tempNameArray = new ArrayList<String>();
		for (int i = 0; i < jdNameArray.size(); i++) {
			String nameForFilter = jdNameArray.get(i) + ">";
			String filtedName = htmlRemoveTag(nameForFilter);
			tempNameArray.add(i, filtedName);
		}
		return tempNameArray;
	}

	public static String parseJson(String jsonItem) throws JSONException, IOException {
		JSONArray jsonArray = null;
		jsonArray = new JSONArray(jsonItem);
		String tempPrice = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			tempPrice = jsonObject.getString("p");
			Log.d("picksomething", "** call parseJson ** price = " + tempPrice);
		}
		return tempPrice;
	}

	private static ArrayList<Bitmap> getBitmapArray(ArrayList<String> imageUrlArray) {
		ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
		Bitmap bitmap = null;
		for (int i = 0; i < imageUrlArray.size(); i++) {
			try {
				bitmap = getGoodsImage(imageUrlArray.get(i));
				bitmapArray.add(i, bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmapArray;
	}

	private static ArrayList<Bitmap> getSuningPrice(ArrayList<String> suningIDArray) {
		ArrayList<Bitmap> priceBitmap = new ArrayList<Bitmap>();
		Bitmap bitmap = null;
		for (int i = 0; i < suningIDArray.size(); i++) {
			try {
				String tempBitmapUrl = "http://price2.suning.cn/webapp/wcs/stores/prdprice/" + suningIDArray.get(i)
						+ "_9051_10000_9-1.png";
				bitmap = getGoodsImage(tempBitmapUrl);
				priceBitmap.add(i, bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return priceBitmap;
	}
}
