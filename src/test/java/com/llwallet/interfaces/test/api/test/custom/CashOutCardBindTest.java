package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.custom.CashOutCardBind;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
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
 * 绑定提现卡接口测试
 */

public class CashOutCardBindTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "绑定提现卡接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		StringBuffer command2 = new StringBuffer();
		StringBuffer command3 = new StringBuffer();
		// 清除支付密码3次错误缓存
		if (dt.get("cashOutCardBind").get("expect_return").contains("7701")) {
			DbUtils dbUtils = new DbUtils();
			String oid_userno = dbUtils.queryOidUserNo(dt.get("cashOutCardBind").get("oid_partner"),
					dt.get("cashOutCardBind").get("user_id")).getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		// 同一个商户号下的同一张卡号当天（自然日）最大有效请求次数不超过5次
		if (dt.get("cashOutCardBind").get("card_no").length() > 0) {
			command2.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(dt.get("cashOutCardBind").get("oid_partner")).append("_")
					.append(dt.get("cashOutCardBind").get("card_no")).append("_bindCashOutCardTimesByCardNoCachedKey")
					.append("\"])");
			telnetUtil.execute(command2.toString());
		}
		// 同一个商户号下的 单个用户号 当天（自然日）最大提交不超过8次
		if (dt.get("cashOutCardBind").get("user_id").length() > 0) {
			command3.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(dt.get("cashOutCardBind").get("oid_partner")).append("_")
					.append(dt.get("cashOutCardBind").get("user_id")).append("_bindCashOutCardTimesByUserIDCachedKey")
					.append("\"])");
			telnetUtil.execute(command3.toString());
		}
		telnetUtil.disconnect();

		CashOutCardBind cashOutCardBind = new CashOutCardBind();
		Object reqObj = JavaBeanUtil.convertMap(cashOutCardBind.getClass(),
				excel.getParamMap(dt.get("cashOutCardBind")));
		cashOutCardBind.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("cashOutCardBind").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", cashOutCardBind.getSign());
		String reqJson = JSON.toJSONString(map);
		String cashOutCardBindUrl = Property.get("llwallet.cashOutCardBind.url");
		String rsp = HttpRequest.httpPostWithJSON(cashOutCardBindUrl, reqJson);
		// excel.writeCurrentCell("cashOutCardBind", "actual_return", rsp);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt",
				"===============================================");

		Reporter.log("绑定提现卡接口测试： " + dt.get("cashOutCardBind").get("comment"), true);
		System.out.println("===============================================");

		assert rsp.contains(dt.get("cashOutCardBind").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@SuppressWarnings("unchecked")
	@Test(description = "绑定提现卡接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		StringBuffer command2 = new StringBuffer();
		StringBuffer command3 = new StringBuffer();
		// 清除支付密码3次错误缓存
		if (dt.get("baseline").get("expect_return").contains("7701")) {
			DbUtils dbUtils = new DbUtils();
			String oid_userno = dbUtils
					.queryOidUserNo(dt.get("baseline").get("oid_partner"), dt.get("baseline").get("user_id"))
					.getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		// 同一个商户号下的同一张卡号当天（自然日）最大有效请求次数不超过5次
		if (dt.get("baseline").get("card_no").length() > 0) {
			command2.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(dt.get("baseline").get("oid_partner")).append("_").append(dt.get("baseline").get("card_no"))
					.append("_bindCashOutCardTimesByCardNoCachedKey").append("\"])");
			telnetUtil.execute(command2.toString());
		}
		// 同一个商户号下的 单个用户号 当天（自然日）最大提交不超过8次
		if (dt.get("baseline").get("user_id").length() > 0) {
			command3.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"")
					.append(dt.get("baseline").get("oid_partner")).append("_").append(dt.get("baseline").get("user_id"))
					.append("_bindCashOutCardTimesByUserIDCachedKey").append("\"])");
			telnetUtil.execute(command3.toString());
		}
		telnetUtil.disconnect();

		CashOutCardBind cashOutCardBind = new CashOutCardBind();
		Object reqObj = JavaBeanUtil.convertMap(cashOutCardBind.getClass(), excel.getParamMap(dt.get("baseline")));
		cashOutCardBind
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", cashOutCardBind.getSign());
		String reqJson = JSON.toJSONString(map);
		String cashOutCardBindUrl = Property.get("llwallet.cashOutCardBind.url");
		String rsp = HttpRequest.httpPostWithJSON(cashOutCardBindUrl, reqJson);
		// excel.writeCurrentCell("cashOutCardBind", "actual_return", rsp);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBind.txt",
				"===============================================");

		Reporter.log("绑定提现卡接口测试： " + dt.get("cashOutCardBind").get("comment"), true);
		System.out.println("===============================================");

		assert rsp.contains(dt.get("baseline").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 5);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 333);
	}

	@AfterClass
	public void afterClass() {

	}
}