package com.lianpay.interfaces.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.lianpay.interfaces.bean.SmsSend;
import com.lianpay.tools.dataprovider.ExcelProvider;
import com.lianpay.tools.http.HttpFixture;
import com.lianpay.tools.utils.GenSign;
import com.lianpay.tools.utils.Property;
import com.lianpay.tools.utils.TelnetUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包下发短信接口测试
 */

public class SmsSendTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包下发短信接口测试", timeOut = 60000, dataProvider = "smsSend")
	public void smsSend(Map<String, String> datadriven) throws Exception {

		// 清除缓存 用户短信发送5次
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host"), Integer.parseInt(Property.get("port")));
		StringBuffer command = new StringBuffer();
		command.append("flush_all");
		telnetUtil.execute(command.toString());
		telnetUtil.disconnect();
		
		SmsSend smsSend = new SmsSend();
		smsSend.setOid_partner(datadriven.get("oid_partner"));
		smsSend.setSign_type(datadriven.get("sign_type"));
		smsSend.setUser_id(datadriven.get("user_id"));
		smsSend.setMob_bind(datadriven.get("mob_bind"));
		smsSend.setFlag_send(datadriven.get("flag_send"));
		smsSend.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsSend)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(smsSend);
		String smsSendUrl = Property.get("llwallet.smsSend.url");

		// 钱包下发短信请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(smsSendUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 钱包下发短信请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));

		Reporter.log("钱包下发短信接口测试： " + datadriven.get("comment"), true);
		Reporter.log("===============================================", true);
	}

	@DataProvider(name = "smsSend")
	public Iterator<Object[]> data4smsSend() throws IOException {
		return new ExcelProvider(this, "smsSend");
	}

	@AfterClass
	public void afterClass() {

	}

}
