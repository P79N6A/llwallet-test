package com.tools.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.testng.Reporter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpRequest {
	private static final String APPLICATION_JSON = "application/json";

	public static String httpPostWithJSON(String url, String json) throws Exception {
		
		Reporter.log("post body -----:" + json,true);
		// 将JSON进行UTF-8编码,以便传输中文
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		StringEntity se = new StringEntity(json, "UTF-8");
		httpPost.setEntity(se);
		HttpResponse httpResponse=httpClient.execute(httpPost);
		// 响应分析
		HttpEntity entity = httpResponse.getEntity();

		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
		StringBuffer responseString = new StringBuffer();
		String result = br.readLine();
		while (result != null)
		{
			responseString.append(result);
			result = br.readLine();
		}
		Reporter.log("response-----:" + responseString.toString(), true);
		return responseString.toString();
	}
}