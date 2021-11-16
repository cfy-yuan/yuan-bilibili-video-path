package com.yuan.utils;

import org.brotli.dec.BrotliInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * http 请求工具类
 *
 * @Classname HttpUtils
 * @Description :
 * @Date 2021/11/16 16:11
 * @Author cfy
 */
public class HttpUtils {
    private final static Integer readTimeout = 3000;
    private final static Integer connectTimeout = 3000;

    /**
     * 初始化请求头信息
     * @param httpURLConnection
     * @return: void
     * @sourceOrTime:
     * @author: cfy
     * @date: 2021/11/16 16:24
     * @version: 0.0.1
     */
    private static void setRequestProperty(HttpURLConnection httpURLConnection) {
        String host = httpURLConnection.getURL().getHost();
        String path = httpURLConnection.getURL().getPath();
        httpURLConnection.setRequestProperty("authority", host);
        httpURLConnection.setRequestProperty("method", "GET");
        httpURLConnection.setRequestProperty("path", path);
        httpURLConnection.setRequestProperty("scheme", "https");
        httpURLConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpURLConnection.setRequestProperty("Accept-Charset", "ISO-8859-2,utf-8; q = 0.7,*; q = 0.7");
        // 网页使用压缩方式
        httpURLConnection.setRequestProperty("accept-encoding", "gzip, deflate, br");
        httpURLConnection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9");
        httpURLConnection.setRequestProperty("cache-control", "max-age=0");
        httpURLConnection.setRequestProperty("upgrade-insecure-requests", "1");
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");

    }
    /**
     * http 请求网页数据
     * @param urlPath
     * @return: java.io.BufferedReader
     * @sourceOrTime:
     * @author: cfy
     * @date: 2021/11/16 16:28
     * @version: 0.0.1
     */

    private static BufferedReader getBufferedReader(String urlPath) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            setRequestProperty(httpURLConnection);

            httpURLConnection.setReadTimeout(readTimeout);
            httpURLConnection.setConnectTimeout(connectTimeout);
            InputStream inputStream = httpURLConnection.getInputStream();
            // 针对解压数据
            if (httpURLConnection.getContentEncoding().equals("br"))
                bufferedReader = new BufferedReader(new InputStreamReader(new BrotliInputStream(inputStream)));
            else if (httpURLConnection.getContentEncoding().equals("deflate"))
                bufferedReader = new BufferedReader(new InputStreamReader(new InflaterInputStream(inputStream)));
            else if (httpURLConnection.getContentEncoding().equals("gzip"))
                bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)));
            else
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferedReader;
    }


    /**
     * 获取哔哩哔哩视频真是地址
     * @param urlPath
     * @return: java.lang.String
     * @sourceOrTime:
     * @author: cfy
     * @date: 2021/11/16 16:29
     * @version: 0.0.1
     */
    public static String getBiliUrl(String urlPath){
        String biliUrl = "";
        try {
            BufferedReader bufferedReader = getBufferedReader(urlPath);
            List<String> list = new ArrayList<>();
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null)
                if (temp.indexOf("readyVideoUrl") != -1) {
                    list.add(temp);
                    break;
                }
            if (list.size() > 0)
                biliUrl = list.get(0).split("'")[1];
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return biliUrl;
    }


}
