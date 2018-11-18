package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.personal.BalancePwdPay;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
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
 * 余额消费接口测试
 */

public class BalancePwdPayTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额支付接口测试", timeOut = 60000, dataProvider = "balancePwdPay")
	public void balancePwdPay(Map<String, String> dt) throws Exception {

		// 清除支付密码3次错误缓存
		DbUtils dbUtils = new DbUtils();
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (dt.get("expect_return").contains("7701")) {
			String oid_userno = dbUtils.queryOidUserNo(dt.get("oid_partner"), dt.get("user_id")).getOid_userno().trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		// 余额支付
		BalancePwdPay balancePwdPay = new BalancePwdPay();
		Object reqObj = JavaBeanUtil.convertMap(balancePwdPay.getClass(), dt);
		balancePwdPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", balancePwdPay.getSign());
		String reqJson = JSON.toJSONString(map);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");
		String rsp = HttpRequest.httpPostWithJSON(balancePwdPayUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", "===============================================");

		Reporter.log("余额支付接口测试： " + dt.get("comment"), true);
		System.out.println("===============================================");
		
		assert rsp.contains(dt.get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@SuppressWarnings("unchecked")
	@Test(description = "余额支付接口测试", timeOut = 60000, dataProvider = "smoke")
	public void smoke(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 清除支付密码3次错误缓存
		DbUtils dbUtils = new DbUtils();
		TelnetUtil telnetUtil = new TelnetUtil(Property.get("host_redis"),
				Integer.parseInt(Property.get("port_redis")));
		StringBuffer command = new StringBuffer();
		StringBuffer command1 = new StringBuffer();
		if (dt.get("smoke").get("expect_return").contains("7701")) {
			String oid_userno = dbUtils
					.queryOidUserNo(dt.get("smoke").get("oid_partner"), dt.get("smoke").get("user_id")).getOid_userno()
					.trim();
			command.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd").append("\"])");
			command1.append("invoke com.lianpay.redis.dubbo.IRedisService.delete([\"").append(oid_userno)
					.append("api_pwd_valid_times_pay_pwd_TIME").append("\"])");
			telnetUtil.execute(command.toString());
			telnetUtil.execute(command1.toString());
		}
		telnetUtil.disconnect();

		// 余额支付
		BalancePwdPay balancePwdPay = new BalancePwdPay();
		Object reqObj = JavaBeanUtil.convertMap(balancePwdPay.getClass(), excel.getParamMap(dt.get("smoke")));
		balancePwdPay.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("smoke").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", balancePwdPay.getSign());
		String reqJson = JSON.toJSONString(map);
		String balancePwdPayUrl = Property.get("llwallet.balancePwdPay.url");
		String rsp = HttpRequest.httpPostWithJSON(balancePwdPayUrl, reqJson);
		
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//balancePwdPay.txt", "===============================================");
		
		// excel.writeCurrentCell("balancePwdPay", "actual_return", rsp);
		Reporter.log("余额支付接口测试： " + dt.get("smoke").get("comment"), true);
		System.out.println("===============================================");
		
		assert rsp.contains(dt.get("smoke").get("expect_return"));
		if (rsp.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")) {
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp)),
					JSONObject.parseObject(rsp).getString("sign"));
		}
	}

	@DataProvider(name = "balancePwdPay")
	public Iterator<Object[]> data4balancePwdPay() throws IOException {
		return new ExcelProvider(this, "balancePwdPay", 1, 7);
	}

	@DataProvider(name = "smoke")
	public Iterator<Object[]> smoke() throws IOException {
		return new ExcelProvider2(this, 291);
	}

	@AfterClass
	public void afterClass() {

	}

}
