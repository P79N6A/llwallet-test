package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.OpenUser;
import com.llwallet.interfaces.bean.personal.SmsCheck;
import com.llwallet.interfaces.bean.personal.SmsSend;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
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
 * 个人用户开户接口测试
 */

public class OpenUserTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "个人用户开户接口测试", timeOut = 60000, dataProvider = "openUser")
	public void openUser(Map<String, String> datadriven) throws Exception {

		String user_id = MyConfig.getUser(datadriven.get("user_id"));
		String eml_bind = user_id + "@yintong.com.cn";
		String oid_partner = MyConfig.getPartner(datadriven.get("oid_partner"));
		String mob_bind = MyConfig.getMobBind(datadriven.get("mob_bind"));
		
		// 下发短信
		SmsSend smsSend = new SmsSend();
		smsSend.setOid_partner(oid_partner);
		smsSend.setSign_type(datadriven.get("sign_type"));
		smsSend.setUser_id(user_id);
		smsSend.setMob_bind(mob_bind);
		smsSend.setFlag_send(datadriven.get("flag_send"));
		smsSend.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsSend)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(smsSend);
		String smsSendUrl = Property.get("llwallet.smsSend.url");
		String rsp = HttpRequest.httpPostWithJSON(smsSendUrl, reqJson);
		assert rsp.contains(datadriven.get("expect_return"));

		DbUtils dbUtils = new DbUtils();		
		String token = JSONObject.parseObject(rsp).getString("token");
		String verify_code = dbUtils.queryCdSmscd(mob_bind).getTno_smscd();

		// 短信验证
		SmsCheck smsCheck = new SmsCheck();
		smsCheck.setOid_partner(oid_partner);
		smsCheck.setSign_type(datadriven.get("sign_type"));
		smsCheck.setUser_id(user_id);
		smsCheck.setToken(token);
		smsCheck.setVerify_code(verify_code);
		smsCheck.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(smsCheck)), datadriven.get("key")));
		String reqJson1 = JSON.toJSONString(smsCheck);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");
		String rsp1 = HttpRequest.httpPostWithJSON(smsCheckUrl, reqJson1);

		assert rsp1.contains(datadriven.get("expect_return"));
		String token1 = JSONObject.parseObject(rsp1).getString("token");
		
		// 个人开户
		OpenUser openUser = new OpenUser();
		openUser.setOid_partner(oid_partner);
		openUser.setSign_type(datadriven.get("sign_type"));
		openUser.setUser_id(user_id);
		openUser.setName_user(datadriven.get("name_user"));
		openUser.setType_idcard(datadriven.get("type_idcard"));
		openUser.setNo_idcard(MyConfig.getNoIdcard(datadriven.get("no_idcard")));
		openUser.setExp_idcard(datadriven.get("exp_idcard"));
		openUser.setType_expidcard(datadriven.get("type_expidcard"));
		if (datadriven.get("pwd_login").length() != 0 && datadriven.get("pwd_login").length() < 50) {
			openUser.setPwd_login(RSAUtil.encrypt(datadriven.get("pwd_login"), Property.get("rsa_pub_key")));
		} else {
			openUser.setPwd_login(datadriven.get("pwd_login"));
		}
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			openUser.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			openUser.setPwd_pay(datadriven.get("pwd_pay"));
		}
		if ("get".equals(datadriven.get("eml_bind"))) {
			openUser.setEml_bind(eml_bind);
		} else {
			openUser.setEml_bind(datadriven.get("eml_bind"));
		}
		openUser.setAddr_conn(datadriven.get("addr_conn"));
		openUser.setAddr_pro(datadriven.get("addr_pro"));
		openUser.setAddr_city(datadriven.get("addr_city"));
		openUser.setAddr_dist(datadriven.get("addr_dist"));
		openUser.setNation_user(datadriven.get("nation_user"));
		openUser.setFlg_sex(datadriven.get("flg_sex"));
		openUser.setOid_job(datadriven.get("oid_job"));
		openUser.setFlag_auth(datadriven.get("flag_auth"));
		openUser.setRisk_item(datadriven.get("risk_item"));
		openUser.setRandom_key(datadriven.get("random_key"));
		openUser.setToken(token1);
		openUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(openUser)), datadriven.get("key")));
		String reqJson2 = JSON.toJSONString(openUser);
		String openUserUrl = Property.get("llwallet.openUser.url");
		String rsp2 = HttpRequest.httpPostWithJSON(openUserUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", "===============================================");
		
		Reporter.log("个人用户开户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 个人用户开户请求返回检查
		assert rsp2.contains(datadriven.get("expect_return1"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(10);
			String oid_userno_new = JSONObject.parseObject(rsp2).getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert datadriven.get("result_acctstat").equals(stat_acct);
		}
	}

	@SuppressWarnings("unchecked")
	@Test(description = "个人用户开户接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		// 下发短信
		SmsSend smsSend = new SmsSend();
		Object reqObj = JavaBeanUtil.convertMap(smsSend.getClass(), dt.get("baseline"));
		smsSend.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", smsSend.getSign());
		String reqJson = JSON.toJSONString(map);
		String smsSendUrl = Property.get("llwallet.smsSend.url");
		String rsp = HttpRequest.httpPostWithJSON(smsSendUrl, reqJson);

		DbUtils dbUtils = new DbUtils();
		String token = JSONObject.parseObject(rsp).getString("token");
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String verify_code = dbUtils.queryCdSmscd(map.get("mob_bind")).getTno_smscd();

		// 短信验证
		SmsCheck smsCheck = new SmsCheck();
		dt.get("baseline").put("user_id", user_id);
		dt.get("baseline").put("token", token);
		dt.get("baseline").put("verify_code", verify_code);
		Object reqObj1 = JavaBeanUtil.convertMap(smsCheck.getClass(), dt.get("baseline"));
		smsCheck.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj1)), dt.get("baseline").get("key")));
		Map<String, String> map1 = JSON.parseObject(JSON.toJSONString(reqObj1), Map.class);
		map1.put("sign", smsCheck.getSign());
		String reqJson1 = JSON.toJSONString(map1);
		String smsCheckUrl = Property.get("llwallet.smsCheck.url");
		String rsp1 = HttpRequest.httpPostWithJSON(smsCheckUrl, reqJson1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");

		// 开户
		OpenUser openUser = new OpenUser();
		dt.get("baseline").put("token", token1);
		Object reqObj2 = JavaBeanUtil.convertMap(openUser.getClass(), dt.get("baseline"));
		openUser.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj2)), dt.get("baseline").get("key")));
		Map<String, String> map2 = JSON.parseObject(JSON.toJSONString(reqObj2), Map.class);
		map2.put("sign", openUser.getSign());
		String reqJson2 = JSON.toJSONString(map2);
		String openUserUrl = Property.get("llwallet.openUser.url");
		String rsp2 = HttpRequest.httpPostWithJSON(openUserUrl, reqJson2);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//openUser4Sms.txt", "===============================================");

		Reporter.log("个人用户开户接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");

		// 个人用户开户请求返回检查
		assert rsp2.contains(dt.get("baseline").get("expect_return1"));
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			Thread.sleep(10);
			String oid_userno_new = JSONObject.parseObject(rsp2).getString("oid_userno");
			String stat_acct = dbUtils.queryAcctStat(oid_userno_new).getStat_acct();
			Reporter.log("stat_acct： " + stat_acct, true);
			assert dt.get("baseline").get("expect_acctstat").equals(stat_acct);
		}
	}

	@DataProvider(name = "openUser")
	public Iterator<Object[]> data4openUser() throws IOException {
		return new ExcelProvider(this, "openUser", 8);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 311);
	}

	@AfterClass
	public void afterClass() {

	}

}
