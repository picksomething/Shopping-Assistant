package cn.picksomething.shopassistant.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

    /**
     * @param jdUrl
     * @return
     * @throws IOException
     * @author caobin
     * @created 2014年11月6日
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
     * @param url
     * @return
     * @author caobin
     * @created 2014年11月6日
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
                results = EntityUtils.toString(response.getEntity(), "GBK");
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

    public static Bitmap getGoodsImage(String path) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inStream);
                inStream.close();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return bitmap;
    }



}
