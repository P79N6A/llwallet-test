package com.llwallet.interfaces.test.api.test.tpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlian.crypt.service.IAESCryptService;
import com.llwallet.interfaces.bean.tpay.OpenUser;
import com.llwallet.interfaces.bean.tpay.OpenUserVerify;
import com.llwallet.interfaces.bean.tpay.ReTransmissionSms;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.HttpUploadFile;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.RSAUtil;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 消费通开户验证接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class OpenUserVerifyTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(description = "消费通开户申请-验证接口测试", timeOut = 6000000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		dbUtils.deleteCardSigned(aesCryptService, dt.get("openUserVerify").get("card_no"));

		// 消费通开户申请
		OpenUser openUser = new OpenUser();
		Object reqObj = JavaBeanUtil.convertMap(openUser.getClass(), excel.getParamMap(dt.get("openUserVerify")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		openUser.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("openUserVerify").get("key")));
		map.put("sign", openUser.getSign());
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("openload", JSON.toJSONString(map));
		Reporter.log("req-------" + JSONObject.toJSON(map).toString(),true);
		String contentType = "image/png";// image/png
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (dt.get("openUserVerify").get("front_card").length() > 0) {
			fileMap.put("front_card", dt.get("openUserVerify").get("front_card"));
		}
		if (dt.get("openUserVerify").get("back_card").length() > 0) {
			fileMap.put("back_card", dt.get("openUserVerify").get("back_card"));
		}
		String openUserUrl = Property.get("tpay.openUser.url");
		// 消费通开户申请请求提交
		String rsp = HttpUploadFile.formUpload(openUserUrl, textMap, fileMap, contentType);
		Reporter.log("rsp-------" + rsp,true);
		Thread.sleep(500);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String token = JSONObject.parseObject(rsp).getString("token");
		String bind_mob = map.get("bind_mob");
		String verify_code = dbUtils.queryCdSmscd(bind_mob).getTno_smscd();
		System.out.println("verify_code-------" + verify_code);

		// 消费通开户验证
		OpenUserVerify openUserVerify = new OpenUserVerify();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("openUserVerify").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "openUserVerify.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(openUserVerify, params);
		if ("get".equals(dt.get("openUserVerify").get("openUserVerify.user_id"))) {
			openUserVerify.setUser_id(user_id);
		}
		if ("get".equals(dt.get("openUserVerify").get("openUserVerify.token"))) {
			openUserVerify.setToken(token);
		}
		if ("get".equals(dt.get("openUserVerify").get("openUserVerify.verify_code"))) {
			openUserVerify.setVerify_code(verify_code);
		}
		openUserVerify.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(openUserVerify)),
				dt.get("openUserVerify").get("openUserVerify.key")));
		String reqJson1 = JSON.toJSONString(openUserVerify);
		String openUserVerifyUrl = Property.get("tpay.openUserVerify.url");
		// 消费通开户验证请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(openUserVerifyUrl, reqJson1);
		// excel.writeCurrentCell("openUserVerify", "actual_return", rsp1);
		Reporter.log("消费通开户验证接口测试： " + dt.get("openUserVerify").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", JSONObject.toJSON(map).toString());
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp);		
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt","===============================================");
		
		// 消费通开户验证请求返回检查
		assert rsp1.contains(dt.get("openUserVerify").get("expect_return"));
		// 返回验签&数据库检查
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)), JSONObject.parseObject(rsp1).getString("sign"));
			assert "1".equals(dbUtils.queryTUserBusi(map.get("user_id")).getStat_busi());
			assert "0".equals(dbUtils.queryTPayAcct(map.get("user_id")).getStat_acct());
		}		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(description = "消费通开户申请-短信重发-验证接口测试", timeOut = 6000000, dataProvider = "provider1")
	public void test1(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {
		//
		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		dbUtils.deleteCardSigned(aesCryptService, dt.get("openUserVerify1").get("card_no"));

		// 消费通开户申请
		OpenUser openUser = new OpenUser();
		Object reqObj = JavaBeanUtil.convertMap(openUser.getClass(), excel.getParamMap(dt.get("openUserVerify1")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		openUser.setSign(
				GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)), dt.get("openUserVerify1").get("key")));
		map.put("sign", openUser.getSign());
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("openload", JSON.toJSONString(map));
		Reporter.log("req-------" + JSONObject.toJSON(map).toString(),true);
		String contentType = "image/png";// image/png
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (dt.get("openUserVerify1").get("front_card").length() > 0) {
			fileMap.put("front_card", dt.get("openUserVerify1").get("front_card"));
		}
		if (dt.get("openUserVerify1").get("back_card").length() > 0) {
			fileMap.put("back_card", dt.get("openUserVerify1").get("back_card"));
		}
		String openUserUrl = Property.get("tpay.openUser.url");
		// 消费通开户申请请求提交
		String rsp = HttpUploadFile.formUpload(openUserUrl, textMap, fileMap, contentType);
		Reporter.log("rsp-------" + rsp,true);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String token = JSONObject.parseObject(rsp).getString("token");
		String bind_mob = map.get("bind_mob");

		// 消费通开户短信重发
		ReTransmissionSms reTransmissionSms = new ReTransmissionSms();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("openUserVerify1").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "reTransmissionSms.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(reTransmissionSms, params);
		if ("get".equals(dt.get("openUserVerify1").get("reTransmissionSms.user_id"))) {
			reTransmissionSms.setUser_id(user_id);
		}
		if ("get".equals(dt.get("openUserVerify1").get("reTransmissionSms.token"))) {
			reTransmissionSms.setToken(token);
		}
		reTransmissionSms.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(reTransmissionSms)),
				dt.get("openUserVerify1").get("reTransmissionSms.key")));
		String reqJson1 = JSON.toJSONString(reTransmissionSms);
		String reTransmissionSmsUrl = Property.get("tpay.reTransmissionSms.url");
		// 消费通开户短信重发请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(reTransmissionSmsUrl, reqJson1);
		String token1 = JSONObject.parseObject(rsp1).getString("token");
		Thread.sleep(500);
		String verify_code1 = dbUtils.queryCdSmscd(bind_mob).getTno_smscd();
		System.out.println("verify_code-------" + verify_code1);
		
		// 消费通开户验证
		OpenUserVerify openUserVerify = new OpenUserVerify();
		Map<String, String> params1 = new HashMap<String, String>();
		Iterator it1 = dt.get("openUserVerify1").entrySet().iterator();
		while (it1.hasNext()) {
			Map.Entry entry = (Map.Entry) it1.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "openUserVerify.")) {
				params1.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(openUserVerify, params1);
		if ("get".equals(dt.get("openUserVerify1").get("openUserVerify.user_id"))) {
			openUserVerify.setUser_id(user_id);
		}
		if ("get".equals(dt.get("openUserVerify1").get("openUserVerify.token"))) {
			openUserVerify.setToken(token1);
		}
		if ("get".equals(dt.get("openUserVerify1").get("openUserVerify.verify_code"))) {
			openUserVerify.setVerify_code(verify_code1);
		}
		openUserVerify.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(openUserVerify)),
				dt.get("openUserVerify1").get("openUserVerify.key")));
		String reqJson2 = JSON.toJSONString(openUserVerify);
		String openUserVerifyUrl = Property.get("tpay.openUserVerify.url");
		// 消费通开户验证请求提交
		String rsp2 = HttpRequest.httpPostWithJSON(openUserVerifyUrl, reqJson2);
		// excel.writeCurrentCell("openUserVerify1", "actual_return", rsp2);
		Reporter.log("消费通开户验证接口测试： " + dt.get("openUserVerify1").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", JSONObject.toJSON(map).toString());
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp);		
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", reqJson2);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//openUserVerify.txt","===============================================");
		
		// 消费通开户验证请求返回检查
		assert rsp2.contains(dt.get("openUserVerify1").get("expect_return"));
		// 返回验签&数据库检查
		if (rsp2.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp2)), JSONObject.parseObject(rsp2).getString("sign"));
			assert "1".equals(dbUtils.queryTUserBusi(map.get("user_id")).getStat_busi());
			assert "0".equals(dbUtils.queryTPayAcct(map.get("user_id")).getStat_acct());
		}		
	}
	
	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this,333);		
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this,333);
	}
	
	@AfterClass
	public void afterClass() {

	}
}