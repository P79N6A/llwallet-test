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
import com.llwallet.interfaces.bean.tpay.OpenUser;
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
import com.lianlian.crypt.service.IAESCryptService;

/*
 * @author jiangxm
 * 消费通开户短信重发接口测试
 */

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class ReTransmissionSmsTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IAESCryptService aesCryptService;

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(description = "消费通开户短信重发接口测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 删除卡签约信息
		DbUtils dbUtils = new DbUtils();
		dbUtils.deleteCardSigned(aesCryptService, dt.get("reTransmissionSms").get("card_no"));

		// 消费通开户申请
		OpenUser openUser = new OpenUser();
		Object reqObj = JavaBeanUtil.convertMap(openUser.getClass(), excel.getParamMap(dt.get("reTransmissionSms")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		openUser.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(map)),
				dt.get("reTransmissionSms").get("key")));
		map.put("sign", openUser.getSign());
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("openload", JSON.toJSONString(map));
		Reporter.log("req-------" + JSONObject.toJSON(map).toString(),true);
		String contentType = "image/png";// image/png
		// 设置file的name，路径
		Map<String, String> fileMap = new HashMap<String, String>();
		if (dt.get("reTransmissionSms").get("front_card").length() > 0) {
			fileMap.put("front_card", dt.get("reTransmissionSms").get("front_card"));
		}
		if (dt.get("reTransmissionSms").get("back_card").length() > 0) {
			fileMap.put("back_card", dt.get("reTransmissionSms").get("back_card"));
		}
		String openUserUrl = Property.get("tpay.openUser.url");
		// 消费通开户申请请求提交
		String rsp = HttpUploadFile.formUpload(openUserUrl, textMap, fileMap, contentType);
		Reporter.log("rsp-------" + rsp,true);
		Thread.sleep(500);
		String user_id = JSONObject.parseObject(rsp).getString("user_id");
		String token = JSONObject.parseObject(rsp).getString("token");

		// 消费通开户短信重发
		ReTransmissionSms reTransmissionSms = new ReTransmissionSms();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("reTransmissionSms").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "reTransmissionSms.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(reTransmissionSms, params);
		if ("get".equals(dt.get("reTransmissionSms").get("reTransmissionSms.user_id"))) {
			reTransmissionSms.setUser_id(user_id);
		}
		if ("get".equals(dt.get("reTransmissionSms").get("reTransmissionSms.token"))) {
			reTransmissionSms.setToken(token);
		}
		reTransmissionSms.setSign(GenSign.genSign(JSONObject.parseObject(JSON.toJSONString(reTransmissionSms)),
				dt.get("reTransmissionSms").get("reTransmissionSms.key")));
		String reqJson1 = JSON.toJSONString(reTransmissionSms);
		String reTransmissionSmsUrl = Property.get("tpay.reTransmissionSms.url");
		// 消费通开户短信重发请求提交
		String rsp1 = HttpRequest.httpPostWithJSON(reTransmissionSmsUrl, reqJson1);
		// excel.writeCurrentCell("reTransmissionSms", "actual_return", rsp1);
		Reporter.log("消费通开户短信重发接口测试： " + dt.get("reTransmissionSms").get("comment"), true);
		System.out.println("===============================================");
		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", JSONObject.toJSON(map).toString());
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", reqJson1);
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//reTransmissionSms.txt", "===============================================");

		// 消费通开户短信重发请求返回检查
		assert rsp1.contains(dt.get("reTransmissionSms").get("expect_return"));
		// 返回验签
		if (rsp1.contains("\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"")){
			assert RSAUtil.checksign(Property.get("rsa_pub_key"), GenSign.genSignData(JSONObject.parseObject(rsp1)), JSONObject.parseObject(rsp1).getString("sign"));
		}
	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this);
	}

	@AfterClass
	public void afterClass() {

	}
}