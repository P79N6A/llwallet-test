package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.AddUserVbAccount;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.ExcelUtil;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import com.tools.utils.TelnetUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 开通企业用户虚拟账户接口测试
 */

public class AddUserVbAccountTest {

	ExcelUtil excelUtil = new ExcelUtil();

	@BeforeClass
	public void beforeClass() throws Exception {
		Property.set();
		excelUtil.prepare4Excel(this, "addUserVbAccount");
	}

	@Test(description = "开通企业用户虚拟账户接口测试", timeOut = 60000, dataProvider = "addUserVbAccount")
	public void addUserVbAccount(Map<String, String> datadriven) throws Exception {

		DbUtils dbUtils = new DbUtils();
		if (datadriven.get("expect_return").contains("0000")) {
			String oid_userno = dbUtils.queryOidUserNo(MyConfig.getPartner(datadriven.get("oid_partner")),
					MyConfig.getUser(datadriven.get("user_id"))).getOid_userno().trim();
			dbUtils.deleteVbAccountBind(oid_userno, datadriven.get("bank_code"));
		}

		// 清除支付密码3次错误缓存
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (datadriven.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(MyConfig.getPartner(datadriven.get("oid_partner")),
					MyConfig.getUser(datadriven.get("user_id"))).getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		AddUserVbAccount addUserVbAccount = new AddUserVbAccount();
		addUserVbAccount.setOid_partner(MyConfig.getPartner(datadriven.get("oid_partner")));
		addUserVbAccount.setSign_type(datadriven.get("sign_type"));
		addUserVbAccount.setUser_id(MyConfig.getUser(datadriven.get("user_id")));
		addUserVbAccount.setNotify_url(datadriven.get("notify_url"));
		addUserVbAccount.setBank_code(datadriven.get("bank_code"));
		if (datadriven.get("pwd_pay").length() != 0 && datadriven.get("pwd_pay").length() < 50) {
			addUserVbAccount.setPwd_pay(RSAUtil.encrypt(datadriven.get("pwd_pay"), Property.get("rsa_pub_key")));
		} else {
			addUserVbAccount.setPwd_pay(datadriven.get("pwd_pay"));
		}
		addUserVbAccount.setCheck_name(datadriven.get("check_name"));
		addUserVbAccount.setRandom_key(datadriven.get("random_key"));
		addUserVbAccount
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(addUserVbAccount)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(addUserVbAccount);
		String addUserVbAccountUrl = Property.get("llwallet.addUserVbAccount.url");

		// 开通企业用户虚拟账户请求提交
		String rsp = HttpRequest.httpPostWithJSON(addUserVbAccountUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//addUserVbAccount.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//addUserVbAccount.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//addUserVbAccount.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//addUserVbAccount.txt",
				"===============================================");

		Reporter.log("开通企业用户虚拟账户接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");

		// excel.writeResult2Excel(this, "addUserVbAccount", 3, rsp);
		// 开通企业用户虚拟账户请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "addUserVbAccount")
	public Iterator<Object[]> data4addUserVbAccount() throws IOException {
		return new ExcelProvider(this, "addUserVbAccount", 1, 7);
	}

	@AfterClass
	public void afterClass() {

	}

}