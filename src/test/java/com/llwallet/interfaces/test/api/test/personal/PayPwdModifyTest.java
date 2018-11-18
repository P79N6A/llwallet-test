package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.PayPwdModify;
import com.tools.dataprovider.ExcelProvider;
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
 * 密码修改接口测试
 */

public class PayPwdModifyTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "密码修改接口测试", timeOut = 60000, dataProvider = "payPwdModify")
	public void payPwdModify(Map<String, String> datadriven) throws Exception {

		PayPwdModify payPwdModify = new PayPwdModify();
		payPwdModify.setOid_partner(datadriven.get("oid_partner"));
		payPwdModify.setSign_type(datadriven.get("sign_type"));
		payPwdModify.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			payPwdModify.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			payPwdModify.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if (datadriven.get("pwd_pay_new").length() != 0 && datadriven.get("pwd_pay_new").length() < 50) {
			payPwdModify.setPwd_pay_new(RSAUtil.encrypt(datadriven.get("pwd_pay_new"), Property.get("rsa_pub_key")));
		} else {
			payPwdModify.setPwd_pay_new(datadriven.get("pwd_pay_new"));
		}
		payPwdModify.setRisk_item(datadriven.get("risk_item"));
		payPwdModify.setRandom_key(datadriven.get("random_key"));
		payPwdModify.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(payPwdModify)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(payPwdModify);
		String payPwdModifyUrl = Property.get("llwallet.payPwdModify.url");
		String rsp = HttpRequest.httpPostWithJSON(payPwdModifyUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//payPwdModify.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//payPwdModify.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//payPwdModify.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//payPwdModify.txt", "===============================================");

		Reporter.log("密码修改接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 密码修改请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
	}

	@DataProvider(name = "payPwdModify")
	public Iterator<Object[]> data4payPwdModify() throws IOException {
		return new ExcelProvider(this, "payPwdModify", 1, 7);
	}

	@AfterClass
	public void afterClass() {

	}

}
