package com.llwallet.interfaces.test.web.online;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.apache.commons.lang.StringUtils;
import com.llwallet.interfaces.bean.ParameterSource;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.ApiUtils;
import com.tools.utils.Md5Algorithm;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 聚合支付Web接口测试
 */

public class AggregateWebOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "聚合支付测试", timeOut = 60000, dataProvider = "aggregateWeb")
	public void aggregateWeb(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String dt_order = ApiUtils.getCurrentDateSecondStr();
		String no_order = ApiUtils.getCurrentDateMillisecondStr();
		String timestamp = ApiUtils.getCurrentDateSecondStr();

		StringBuffer signBuf = new StringBuffer();
		String[] strArray = new ParameterSource().aggregateWebParameter();

		// sign
		for (String key : strArray) {
			if (key.equals("dt_order") && datadriven.get(key).equals("auto")) {
				signBuf.append(key + "=" + dt_order + "&");
			} else if (key.equals("no_order") && datadriven.get(key).equals("auto")) {
				signBuf.append(key + "=" + no_order + "&");
			} else if (key.equals("timestamp") && datadriven.get(key).equals("auto")) {
				signBuf.append(key + "=" + timestamp + "&");
			} else if (datadriven.get(key).length() >= 1) {
				signBuf.append(key + "=" + datadriven.get(key) + "&");
			}
		}

		String sign_src = signBuf.substring(0, signBuf.length() - 1);
		System.out.println("sign未签名前源串：" + sign_src);
		String sign = "";
		if (datadriven.get("sign_type").equals("MD5")) {
			sign_src = sign_src + "&key=" + datadriven.get("key");
			sign = Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
		} else if (datadriven.get("sign_type").equals("RSA")) {
			sign = RSAUtil.sign(datadriven.get("key"), sign_src);
		}
		sign = URLEncoder.encode(sign, "UTF-8");
		System.out.println("sign签名后：" + sign);

		String aggregatewebUrl = Property.get("llwallet.aggregateweb.url.online");
		String reqBody = signBuf.append("sign=" + sign).toString();
		System.out.println("支付请求reqBody：" + reqBody);

		// 商户下单请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(aggregatewebUrl);
		hf.addHeaderValue("Content-Type", "application/x-www-form-urlencoded");
		hf.addRequestBody(reqBody);

		hf.Post();
//		System.out.println("返回resp：" + hf.getResponseBody());
//		System.out.println("返回消息头：" + URLDecoder.decode(hf.getResponseheaders(), "utf-8"));
		System.out.println("返回消息头：" + URLDecoder
				.decode(StringUtils.substringBetween(hf.getResponseheaders(), "PSA OUR;", "Content-Type="), "utf-8"));
		// 支付请求返回检查
		assert URLDecoder.decode(hf.getResponseheaders(), "utf-8").contains(datadriven.get("result_code"));

		Reporter.log("聚合支付Web接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "aggregateWeb")
	public Iterator<Object[]> data4aggregateWeb() throws IOException {
		return new ExcelProvider(this, "aggregateWeb");
	}

	@AfterClass
	public void afterClass() {

	}

}
