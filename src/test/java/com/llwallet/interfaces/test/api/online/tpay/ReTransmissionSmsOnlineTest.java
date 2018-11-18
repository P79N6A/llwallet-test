package com.llwallet.interfaces.test.api.online.tpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.ReTransmissionSms;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通开户短信重发接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class ReTransmissionSmsOnlineTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "rawtypes" })
	@Test(description = "消费通开户短信重发接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 消费通开户短信重发
		ReTransmissionSms reTransmissionSms = new ReTransmissionSms();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("reTransmissionSms").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "reTransmissionSms.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(reTransmissionSms, params);
		reTransmissionSms.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(reTransmissionSms)),
				dt.get("reTransmissionSms").get("reTransmissionSms.key")));
		String reqJson1 = JSON.toJSONString(reTransmissionSms);
		String reTransmissionSmsUrl = Property.get("tpay.reTransmissionSms.url.online");
		// 消费通开户短信重发请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(reTransmissionSmsUrl, reqJson1);
		// excel.writeCurrentCell("reTransmissionSms", "actual_return", rsp1);
		Reporter.log("消费通开户短信重发接口测试： " + dt.get("reTransmissionSms").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", "===============================================");

		// 消费通开户短信重发请求返回检查
		assert rsp1.contains(dt.get("reTransmissionSms").get("expect_return"));
		// 返回验签
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)), JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this);
	}

	@AfterClass
	public void afterClass() {

	}
}