package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.OpenUnactivedAcct;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 钱包账户待激活开户接口测试
 */

public class OpenUnactivedAcctTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包账户待激活开户接口测试", timeOut = 60000, dataProvider = "openUnactivedAcct")
	public void openUnactivedAcct(Map<String, String> datadriven) throws Exception {

		OpenUnactivedAcct openUnactivedAcct = new OpenUnactivedAcct();
		openUnactivedAcct.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		openUnactivedAcct.setSign_type(datadriven.get("sign_type"));
		openUnactivedAcct.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		openUnactivedAcct.setMob_bind(MyConfig.getMobBind(datadriven.get("mob_bind")));
		openUnactivedAcct.setName_user(datadriven.get("name_user"));
		openUnactivedAcct.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedAcct.setNo_idcard(MyConfig.getNoIdcard(datadriven.get("no_idcard")));
		openUnactivedAcct.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedAcct.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0 && datadriven.get("pwd_login").length() < 50) {
			openUnactivedAcct.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedAcct.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			openUnactivedAcct.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedAcct.setPwd_pay(datadriven.get("pwd_pay"));
		}
		openUnactivedAcct.setEml_bind(datadriven.get("eml_bind"));
		openUnactivedAcct.setAddr_conn(datadriven.get("addr_conn"));
		openUnactivedAcct.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedAcct.setAddr_city(datadriven.get("addr_city"));
		openUnactivedAcct.setAddr_dist(datadriven.get("addr_dist"));
		openUnactivedAcct.setFlg_sex(datadriven.get("flg_sex"));
		openUnactivedAcct.setOid_job(datadriven.get("oid_job"));
		openUnactivedAcct.setRisk_item(datadriven.get("risk_item"));
		openUnactivedAcct.setRandom_key(datadriven.get("random_key"));
		openUnactivedAcct.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedAcct)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnactivedAcct);
		String openUnactivedAcctUrl = Property.get("llwallet.openUnactivedAcct.url");
		String rsp = HttpRequest.httpPostWithJSON(openUnactivedAcctUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//cashoutCombineApply.txt",
				"===============================================");

		Reporter.log("钱包账户待激活开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// 钱包账户待激活开户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "openUnactivedAcct")
	public Iterator<Object[]> data4openUnactivedAcct() throws IOException {
		return new ExcelProvider(this, "openUnactivedAcct", 1,7);
	}

	@AfterClass
	public void afterClass() {

	}

}
