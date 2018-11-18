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

//import com.lianpay.util.RSAUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author xiaoming.jiang
 * 充值接口测试
 */

public class RechargeTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "充值接口测试", timeOut = 60000, dataProvider = "recharge")
	public void recharge(Map<String, String> datadriven) throws Exception {


		StringBuffer sign_src = new StringBuffer();
		String[] strArray = new ParameterSource().rechargeGatewayParameter();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date date = cal.getTime();
		String dt_order = sdf.format(date);
		String no_order = sdf.format(date);
		String timestamp = sdf.format(date);

		for (String key : strArray) {
			if (datadriven.get(key).length() > 0){
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
		}

		String sign_src1 = sign_src.substring(0, sign_src.length() - 1);
		
		System.out.println("signsrc" + sign_src1);
		
	    String sign = "";
	    if("RSA".equals(datadriven.get("sign_type"))){
//	    	sign = TraderRSAUtil.sign(datadriven.get("key"), sign_src1);	    	
	    	sign = RSAUtil.sign(datadriven.get("key"), sign_src1);
	    }else{
	    	sign_src1 +="&key=" + datadriven.get("key") ;
	    	sign = Md5Algorithm.getInstance().md5Digest(sign_src1.getBytes("utf-8"));
	    }

		String reqBody = sign_src.append("sign=" + sign).toString();
		System.out.println("充值请求reqBody：" + reqBody);
		String rechargeUrl = Property.get("llwallet.recharge.url");
		
		// 充值请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(rechargeUrl);
		hf.addHeaderValue("Content-Type", "application/x-www-form-urlencoded");
		hf.addRequestBody(reqBody);
		hf.Post();
		String respheaders = hf.getResponseheaders();  
		respheaders = StringUtils.substringBetween(respheaders,"PSA OUR;Set-Cookie=", "; Path=/;S");
		System.out.println("实际返回respheaders：" +  respheaders);

		assert respheaders.contains(datadriven.get("result_code"));

		Reporter.log("充值接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "recharge")
	public Iterator<Object[]> data4recharge() throws IOException {
		return new ExcelProvider(this, "recharge",2);
	}

	@AfterClass
	public void afterClass() {

	}

}