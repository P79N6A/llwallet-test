package com.lianpay.interfaces.test.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.lianpay.api.util.TraderRSAUtil;
import com.lianpay.interfaces.bean.ParameterSource;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.Property;
import com.lianpay.util.Md5Algorithm;
import com.lianpay.util.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author xiaoming.jiang
 * 消费接口测试
 */

public class PaymentTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "消费接口测试", timeOut = 60000, dataProvider = "payment")
	public void payment(Map<String, String> datadriven) throws Exception {


		StringBuffer sign_src = new StringBuffer();
		String[] strArray = new ParameterSource().paymentGatewayParameter();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date date = cal.getTime();
		String dt_order = sdf.format(date);
		String no_order = sdf.format(date);
		String timestamp = sdf.format(date);

		for (String key : strArray) {
			if (key.equals("dt_order")
					&& datadriven.get(key).equals("auto")) {
				sign_src.append(key + "=" + dt_order + "&");
			} else if (key.equals("no_order")
					&& datadriven.get(key).equals("auto")) {
				sign_src.append(key + "=" + no_order + "&");
			} else if (key.equals("timestamp")
					&& datadriven.get(key).equals("auto")) {
				sign_src.append(key + "=" + timestamp + "&");
			} else {
				sign_src.append(key + "=" + datadriven.get(key) + "&");
			}
		}

		String sign_src1 = sign_src.substring(0, sign_src.length() - 1);
		
		System.out.println("signsrc" + sign_src1);
		
	    String sign = "";
	    if("RSA".equals(datadriven.get("sign_type"))){
	    	sign = TraderRSAUtil.sign(datadriven.get("key"), sign_src1);	    	
//	    	sign = RSAUtil.sign(datadriven.get("key"), sign_src1);
	    }else{
	    	sign_src1 +="&key=" + datadriven.get("key") ;
	    	sign = Md5Algorithm.getInstance().md5Digest(sign_src1.getBytes("utf-8"));
	    }

		String reqBody = sign_src.append("sign=" + sign).toString();
		System.out.println("消费请求reqBody：" + reqBody);
		String paymentUrl = Property.get("llwallet.payment.url");
		
		// 消费请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(paymentUrl);
		hf.addHeaderValue("Content-Type", "application/x-www-form-urlencoded");
		hf.addRequestBody(reqBody);
		hf.Post();
		String respheaders = hf.getResponseheaders();  
		respheaders = StringUtils.substringBetween(respheaders,"PSA OUR;Set-Cookie=", "; Path=/;S");
		System.out.println("实际返回respheaders：" +  respheaders);

		assert respheaders.contains(datadriven.get("result_code"));

		Reporter.log("消费接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "payment")
	public Iterator<Object[]> data4payment() throws IOException {
		return new ExcelProvider(this, "payment",1);
	}

	@AfterClass
	public void afterClass() {

	}

}