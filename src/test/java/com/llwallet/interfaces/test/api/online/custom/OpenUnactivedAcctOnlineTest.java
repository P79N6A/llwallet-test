package com.llwallet.interfaces.test.api.online.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.OpenUnactivedAcct;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ApiUtils;
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
 * 钱包账户待激活开户接口测试
 */

public class OpenUnactivedAcctOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "钱包账户待激活开户接口测试", timeOut = 60000, dataProvider = "openUnactivedAcct")
	public void openUnactivedAcct(Map<String, String> datadriven) throws Exception {

		Thread.sleep(1000);
		String user_id = ApiUtils.getCurrentDateMillisecondStr();
		String mob_bind = ApiUtils.getMobStr();
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = datadriven.get("oid_partner");

		// 组织请求报文
		OpenUnactivedAcct openUnactivedAcct = new OpenUnactivedAcct();
		openUnactivedAcct.setOid_partner(oid_partner);
		openUnactivedAcct.setSign_type(datadriven.get("sign_type"));
		if ("auto".equals(datadriven.get("user_id"))) {
			openUnactivedAcct.setUser_id(user_id);
		} else {
			openUnactivedAcct.setUser_id(datadriven.get("user_id"));
		}
		if ("auto".equals(datadriven.get("mob_bind"))) {
			openUnactivedAcct.setMob_bind(mob_bind);
		} else {
			openUnactivedAcct.setMob_bind(datadriven.get("mob_bind"));
		}
		openUnactivedAcct.setName_user(datadriven.get("name_user"));
		openUnactivedAcct.setType_idcard(datadriven.get("type_idcard"));
		openUnactivedAcct.setNo_idcard(datadriven.get("no_idcard"));
		openUnactivedAcct.setExp_idcard(datadriven.get("exp_idcard"));
		openUnactivedAcct.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0) {
			openUnactivedAcct.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedAcct.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0) {
			openUnactivedAcct.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUnactivedAcct.setPwd_login(datadriven.get("pwd_pay"));
		}

		if ("auto".equals(datadriven.get("eml_bind"))) {
			openUnactivedAcct.setEml_bind(eml_bind);
		} else {
			openUnactivedAcct.setEml_bind(datadriven.get("eml_bind"));
		}
		openUnactivedAcct.setAddr_conn(datadriven.get("addr_conn"));
		openUnactivedAcct.setAddr_pro(datadriven.get("addr_pro"));
		openUnactivedAcct.setAddr_city(datadriven.get("addr_city"));
		openUnactivedAcct.setAddr_dist(datadriven.get("addr_dist"));
		openUnactivedAcct.setFlg_sex(datadriven.get("flg_sex"));
		openUnactivedAcct.setOid_job(datadriven.get("oid_job"));
		openUnactivedAcct.setRisk_item(datadriven.get("risk_item"));
		openUnactivedAcct.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUnactivedAcct)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(openUnactivedAcct);
		String openUnactivedAcctUrl = Property.get("llwallet.openUnactivedAcct.url.online");

		// 钱包账户待激活开户请求提交
		String rsp = HttpRequest.httpPostWithJSON(openUnactivedAcctUrl, reqJson);

		Reporter.log("钱包账户待激活开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUnactivedAcct.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUnactivedAcct.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//openUnactivedAcct.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUnactivedAcct.txt", "===============================================");
		
		// 钱包账户待激活开户请求返回检查
		assert rsp.contains(datadriven.get("result_code"));
		
	}

	@DataProvider(name = "openUnactivedAcct")
	public Iterator<Object[]> data4openUnactivedAcct() throws IOException {
		return new ExcelProvider(this, "openUnactivedAcct", 8);
	}

	@AfterClass
	public void afterClass() {

	}

}
