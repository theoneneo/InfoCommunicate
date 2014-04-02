package com.neo.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author LiuBing
 * @version 2014-4-2 下午2:15:16
 */
public class DHttpClient {

    public DHttpClient() {
    }

    public String post(String url, String msg) {
	String result = null;
	HttpPost httpPost = new HttpPost(url);
	// 设置HTTP POST请求参数必须用NameValuePair对象
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("msg", msg));

	HttpResponse httpResponse = null;
	try {
	    // 设置httpPost请求参数
	    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    httpResponse = new DefaultHttpClient().execute(httpPost);
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {
		result = EntityUtils.toString(httpResponse.getEntity());
	    }
	} catch (ClientProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return result;
    }

}
