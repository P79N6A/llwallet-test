package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.BankCardBindNoPwd;
import com.tools.dataprovider.ExcelProvider;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.JavaBeanUtil;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 四要素无验绑卡接口测试
 */

public class BankCardBindNoPwdTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "四要素无验绑卡接口测试", timeOut = 60000, dataProvider = "bankCardBindNoPwd")
	public void bankCardBindNoPwd(Map<String, String> datadriven) throws Exception {

		BankCardBindNoPwd bankCardBindNoPwd = new BankCardBindNoPwd();
		bankCardBindNoPwd.setOid_partner(datadriven.get("oid_partner"));
		bankCardBindNoPwd.setSign_type(datadriven.get("sign_type"));
		bankCardBindNoPwd.setUser_id(datadriven.get("user_id"));
		bankCardBindNoPwd.setCard_no(datadriven.get("card_no"));
		bankCardBindNoPwd.setApi_version(datadriven.get("api_version"));
		bankCardBindNoPwd.setBind_mob(datadriven.get("bind_mob"));
		bankCardBindNoPwd.setCvv2(datadriven.get("cvv2"));
		bankCardBindNoPwd.setPay_type(datadriven.get("pay_type"));
		bankCardBindNoPwd.setVali_date(datadriven.get("vali_date"));
		bankCardBindNoPwd.setRisk_item(datadriven.get("risk_item"));
		bankCardBindNoPwd.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(bankCardBindNoPwd)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(bankCardBindNoPwd);
		String bankCardBindNoPwdUrl = Property.get("llwallet.bankCardBindNoPwd.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardBindNoPwdUrl, reqJson);

		// 四要素无验绑卡请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));

		Reporter.log("四要素无验绑卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "四要素无验绑卡接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		BankCardBindNoPwd bankCardBindNoPwd = new BankCardBindNoPwd();
		Object reqObj = JavaBeanUtil.convertMap(bankCardBindNoPwd.getClass(), dt.get("baseline"));
		bankCardBindNoPwd
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", bankCardBindNoPwd.getSign());
		String reqJson = JSON.toJSONString(map);
		String bankCardBindNoPwdUrl = Property.get("llwallet.bankCardBindNoPwd.url");
		String rsp = HttpRequest.httpPostWithJSON(bankCardBindNoPwdUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//bankCardBindNoPwd.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//bankCardBindNoPwd.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//bankCardBindNoPwd.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//bankCardBindNoPwd.txt",
				"===============================================");
		
		// 四要素无验绑卡请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));

		Reporter.log("四要素无验绑卡接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "bankCardBindNoPwd")
	public Iterator<Object[]> data4bankCardBindNoPwd() throws IOException {
		return new ExcelProvider(this, "bankCardBindNoPwd", 2);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3, 6);
	}

	@AfterClass
	public void afterClass() {

	}
}