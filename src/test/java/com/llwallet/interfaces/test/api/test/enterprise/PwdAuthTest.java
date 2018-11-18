package com.llwallet.interfaces.test.api.test.enterprise;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.enterprise.PwdAuth;
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
 * 钱包支付密码验证授权接口测试
 */

public class PwdAuthTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包支付密码验证授权接口测试", timeOut = 60000, dataProvider = "pwdAuth")
	public void pwdAuth(Map<String, String> datadriven) throws Exception {

		PwdAuth pwdAuth = new PwdAuth();
		pwdAuth.setOid_partner(datadriven.get("oid_partner"));
		pwdAuth.setSign_type(datadriven.get("sign_type"));
		pwdAuth.setUser_id(datadriven.get("user_id"));
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			pwdAuth.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			pwdAuth.setPwd_pay(datadriven.get("pwd_pay"));
		}
		pwdAuth.setNum_license(datadriven.get("num_license"));
		pwdAuth.setFlag_check(datadriven.get("flag_check"));
		pwdAuth.setMob_bind(datadriven.get("mob_bind"));
		pwdAuth.setFlag_cashcard(datadriven.get("flag_cashcard"));
		pwdAuth.setRisk_item(datadriven.get("risk_item"));
		pwdAuth.setRandom_key(datadriven.get("random_key"));
		pwdAuth.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(pwdAuth)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(pwdAuth);
		String pwdAuthUrl = Property.get("llwallet.pwdAuth.url");
		String rsp = HttpRequest.httpPostWithJSON(pwdAuthUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//pwdAuth.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//pwdAuth.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//pwdAuth.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//pwdAuth.txt", "===============================================");

		Reporter.log("钱包支付密码验证授权接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 钱包支付密码验证授权请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}

	}

	@DataProvider(name = "pwdAuth")
	public Iterator<Object[]> data4pwdAuth() throws IOException {
		return new ExcelProvider(this, "pwdAuth", 5, 7);
	}

	@AfterClass
	public void afterClass() {

	}

}
