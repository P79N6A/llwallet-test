package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BankPayPwdSet;
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
 * 银行卡绑卡授权申请接口测试
 */

public class BankPayPwdSetTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "银行卡绑卡授权申请接口测试", timeOut = 60000, dataProvider = "bankPayPwdSet")
	public void bankPayPwdSet(Map<String, String> datadriven) throws Exception {

		BankPayPwdSet bankPayPwdSet = new BankPayPwdSet();
		bankPayPwdSet.setOid_partner(datadriven.get("oid_partner"));
		bankPayPwdSet.setSign_type(datadriven.get("sign_type"));
		bankPayPwdSet.setUser_id(datadriven.get("user_id"));
		bankPayPwdSet.setCard_no(datadriven.get("card_no"));
		bankPayPwdSet.setBind_mob(datadriven.get("bind_mob"));
		bankPayPwdSet.setAcct_name(datadriven.get("acct_name"));
		bankPayPwdSet.setId_type(datadriven.get("id_type"));
		bankPayPwdSet.setId_no(datadriven.get("id_no"));
		bankPayPwdSet.setVali_date(datadriven.get("vali_date"));
		bankPayPwdSet.setCvv2(datadriven.get("cvv2"));
		bankPayPwdSet.setPay_type(datadriven.get("pay_type"));
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			bankPayPwdSet.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			bankPayPwdSet.setPwd_pay(datadriven.get("pwd_pay"));
		}
		bankPayPwdSet.setRisk_item(datadriven.get("risk_item"));
		bankPayPwdSet.setFlag_cashcard(datadriven.get("flag_cashcard"));
		bankPayPwdSet.setRandom_key(datadriven.get("random_key"));
		bankPayPwdSet
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankPayPwdSet)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankPayPwdSet);
		String bankPayPwdSetUrl = Property.get("llwallet.bankPayPwdSet.url");
		String rsp = HttpRequest.httpPostWithJSON(bankPayPwdSetUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankPayPwdSet.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankPayPwdSet.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankPayPwdSet.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankPayPwdSet.txt", "===============================================");

		Reporter.log("银行卡绑卡授权申请接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 银行卡支付短信验证请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"") || rsp.contains("\"ret_code\":\"8888\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "bankPayPwdSet")
	public Iterator<Object[]> data4bankPayPwdSet() throws IOException {
		return new ExcelProvider(this, "bankPayPwdSet", 1,4);
	}

	@AfterClass
	public void afterClass() {

	}

}
