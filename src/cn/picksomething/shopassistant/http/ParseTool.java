package cn.picksomething.shopassistant.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gongzhanhong on 15/2/25.
 */
public class ParseTool {
  private static ArrayList<HashMap<String, Object>> finalResults;
  private static String regxID;
  private static String regxName;
  private static String regxImageLink;
  private static String regxPrice;
  private static String jdResultString;
  private static String tmallResultString;
  private static String suningResultString;
  private static String regxPriceID;
  private static String gomeResultString;

  public static void init() {
    finalResults = new ArrayList<HashMap<String, Object>>();
  }

  public static void emptyArray() {
    finalResults = null;
  }

  /**
   * @author caobin
   * @created 2014年11月14日
   */
  public static ArrayList<HashMap<String, Object>> getFinalReslut(String url, int source) throws IOException {
    if (0 == source) {
      regxID = "sku=\"(.*?)\"";
      regxName = "<div class=\"p-name\">\\n\\s+<.*?>\\n\\s+(.*?) class='adwords' .*?></font>";
      regxImageLink = "<img width=\"220\".*? data-lazyload=\"(.*?)\" />";
      jdResultString = HttpTools.getStringResult(null, url);
      ArrayList<String> jdIDArray = matchResults(jdResultString, regxID);
      ArrayList<String> jdNameArray = filterJdName(matchResults(jdResultString, regxName));
      ArrayList<String> jdImageLinkArray = matchResults(jdResultString, regxImageLink);
      ArrayList<Bitmap> jdBitmapArray = getBitmapArray(jdImageLinkArray);
      ArrayList<String> jdPriceArray = getJdPrice(jdIDArray);
      ArrayList<String> jdDetailLinkAddr = getDetailLinkByID(jdIDArray, null, source);
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
      tmallResultString = HttpTools.getStringResult(null, url);
      ArrayList<String> tmallIDArray = matchResults(tmallResultString, regxID);
      ArrayList<String> tmallNameArray = matchResults(tmallResultString, regxName);
      ArrayList<String> tmallImageLinkArray = matchResults(tmallResultString, regxImageLink);
      ArrayList<Bitmap> tmallBitmapArray = getBitmapArray(tmallImageLinkArray);
      ArrayList<String> tmallPriceArray = matchResults(tmallResultString, regxPrice);
      ArrayList<String> tmallDetailLinkAddr = getDetailLinkByID(tmallIDArray, null, source);
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
      regxPriceID = "<li class=\"(.*?) 000000000.*?\"  name=\".*?\">";
      regxName = "<a title=\"(.*?)\" class=\"search-bl\"";
      regxImageLink = "<img class=\"err-product\" src=\"(.*?)\"";
      suningResultString = HttpTools.getStringResult(null, url);
      ArrayList<String> suningIDArray = matchResults(suningResultString, regxID);
      ArrayList<String> suningPriceIDArray = matchResults(suningResultString, regxPriceID);
      ArrayList<String> suningNameArray = matchResults(suningResultString, regxName);
      ArrayList<String> suningImageLinkArray = matchResults(suningResultString, regxImageLink);
      ArrayList<Bitmap> suningBitmapArray = getBitmapArray(suningImageLinkArray);
      ArrayList<Bitmap> suningPriceArray = getSuningPrice(suningPriceIDArray);
      ArrayList<String> suningDetailLinkAddr = getDetailLinkByID(suningIDArray, null, source);
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
    } else if (3 == source) {
      String regxPro_ID = "<li class=\"\" g-li=\"(.*?)\" g-data=\".*?\">";
      String regxSku_ID = "<span id=\"(.*?)-sku-id\">.*?</span>";
      regxPriceID = "<span class=\"price\"><em>¥</em>(.*?)</span>";
      regxName = "<a track=\".*?\" target=\"_blank\" href=\".*?\" ><img alt=\"(.*?)\" gome-src=\".*?\" src=\"http://app.gome.com.cn/images/grey.gif\"></a>";
      regxImageLink = "<a track=\".*?\" target=\"_blank\" href=\".*?\" ><img alt=\".*?\" gome-src=\"(.*?)\" src=\"http://app.gome.com.cn/images/grey.gif\"></a>";
      gomeResultString = HttpTools.getStringResult(null, url);
      ArrayList<String> gomeProIDArray = matchResults(gomeResultString, regxPro_ID);
      ArrayList<String> gomeSkuIDArray = matchResults(gomeResultString, regxSku_ID);
      ArrayList<String> gomePriceIDArray = matchResults(gomeResultString, regxPriceID);
      ArrayList<String> gomeNameArray = matchResults(gomeResultString, regxName);
      ArrayList<String> gomeImageLinkArray = matchResults(gomeResultString, regxImageLink);
      ArrayList<Bitmap> gomeBitmapArray = getBitmapArray(gomeImageLinkArray);
      ArrayList<String> gomeDetailLinkAddr = getDetailLinkByID(gomeProIDArray, gomeSkuIDArray, source);
      ArrayList<HashMap<String, Object>> suningResults = new ArrayList<HashMap<String, Object>>();
      for (int i = 0; i < gomeProIDArray.size(); i++) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodName", gomeNameArray.get(i));
        map.put("goodBitmap", gomeBitmapArray.get(i));
        map.put("goodPrice", gomePriceIDArray.get(i));
        map.put("goodSource", "国美在线");
        map.put("detailLink", gomeDetailLinkAddr.get(i));
        suningResults.add(map);
      }
      finalResults.addAll(suningResults);
    }
    return finalResults;
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

  public static Bitmap DecodeFromURL(String url) {
    URL mURL = null;
    Bitmap mBitmap = null;
    InputStream mInputStream = null;
    try {
      // <strong>图片</strong>地址
      mURL = new URL(url);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      // 获得URL的输入流
      mInputStream = mURL.openStream();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 解码输入流
    mBitmap = BitmapFactory.decodeStream(mInputStream);
    // 显示<strong>图片</strong>

    try {
      // 关闭输入流
      mInputStream.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return mBitmap;
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





  private static ArrayList<String> getDetailLinkByID(ArrayList<String> ProIDArray, ArrayList<String> SkuIDArray,
                                                     int source) {
    ArrayList<String> tempLinkArray = new ArrayList<String>();
    String tempLinkStr = null;
    for (int i = 0; i < ProIDArray.size(); i++) {
      if (0 == source) {
        tempLinkStr = "http://item.jd.com/" + ProIDArray.get(i) + ".html";
      } else if (1 == source) {
        tempLinkStr = "http://detail.m.tmall.com/item.htm?id=" + ProIDArray.get(i);
      } else if (2 == source) {
        tempLinkStr = "http://m.suning.com/product/" + ProIDArray.get(i) + ".html";
      } else if (3 == source) {
        tempLinkStr = "http://m.gome.com.cn/product-" + ProIDArray.get(i) + "-" + SkuIDArray.get(i) + "-0-0.html";
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
      jsonItem = HttpTools.requestJdJson(jsonUrl);
      try {
        tempArray.add(parseJson(jsonItem));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    Log.d("picksomething", "** call getJdPrice ** tempArray.size = " + tempArray.size());
    return tempArray;
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
        bitmap = HttpTools.getGoodsImage(imageUrlArray.get(i));
        bitmapArray.add(i, bitmap);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return bitmapArray;
  }

  private static ArrayList<Bitmap> getSuningPrice(ArrayList<String> suningIDArray) {
    ArrayList<Bitmap> priceBitmap = new ArrayList<Bitmap>();
    Bitmap bitmap = null;
    for (int i = 0; i < suningIDArray.size(); i++) {
      String tempBitmapUrl = "http://price2.suning.cn/webapp/wcs/stores/prdprice/" + suningIDArray.get(i)
          + "_9051_10000_9-1.png";
      Log.d("picksomething", "tempBitmapURL = " + tempBitmapUrl);
      bitmap = DecodeFromURL(tempBitmapUrl);
      priceBitmap.add(i, bitmap);
    }
    return priceBitmap;
  }


}
