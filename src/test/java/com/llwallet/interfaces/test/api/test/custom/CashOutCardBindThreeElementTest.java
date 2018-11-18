package com.llwallet.interfaces.test.api.test.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.custom.CashOutCardBindThreeElement;
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
 * 三要素无验绑定提现卡接口测试
 */

public class CashOutCardBindThreeElementTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "三要素无验绑定提现卡接口测试", timeOut = 60000, dataProvider = "cashOutCardBindThreeElement")
	public void cashOutCardBindThreeElement(Map<String, String> datadriven) throws Exception {

		CashOutCardBindThreeElement cashOutCardBindThreeElement = new CashOutCardBindThreeElement();
		cashOutCardBindThreeElement.setOid_partner(datadriven.get("oid_partner"));
		cashOutCardBindThreeElement.setSign_type(datadriven.get("sign_type"));
		cashOutCardBindThreeElement.setUser_id(datadriven.get("user_id"));
		cashOutCardBindThreeElement.setCard_no(datadriven.get("card_no"));
		cashOutCardBindThreeElement.setAcct_name(datadriven.get("acct_name"));
		cashOutCardBindThreeElement.setId_no(datadriven.get("id_no"));
		cashOutCardBindThreeElement.setRisk_item(datadriven.get("risk_item"));
		cashOutCardBindThreeElement.setSign(GenSign
				.genSign(JSON.parseObject(JSON.toJSONString(cashOutCardBindThreeElement)), datadriven.get("key")));

		String reqJson = JSON.toJSONString(cashOutCardBindThreeElement);
		String cashOutCardBindThreeElementUrl = Property.get("llwallet.cashOutCardBindThreeElement.url");
		String rsp = HttpRequest.httpPostWithJSON(cashOutCardBindThreeElementUrl, reqJson);

		// 三要素无验绑定提现卡请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));

		Reporter.log("三要素无验绑定提现卡接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@SuppressWarnings({ "unchecked" })
	@Test(description = "三要素无验绑定提现卡接口测试", timeOut = 60000, dataProvider = "provider1")
	public void baseline(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel)
			throws Exception {

		CashOutCardBindThreeElement cashOutCardBindThreeElement = new CashOutCardBindThreeElement();
		Object reqObj = JavaBeanUtil.convertMap(cashOutCardBindThreeElement.getClass(), dt.get("baseline"));
		cashOutCardBindThreeElement
				.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(reqObj)), dt.get("baseline").get("key")));
		Map<String, String> map = JSON.parseObject(JSON.toJSONString(reqObj), Map.class);
		map.put("sign", cashOutCardBindThreeElement.getSign());
		String reqJson = JSON.toJSONString(map);
		String cashOutCardBindThreeElementUrl = Property.get("llwallet.cashOutCardBindThreeElement.url");
		String rsp = HttpRequest.httpPostWithJSON(cashOutCardBindThreeElementUrl, reqJson);

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBindThreeElement.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBindThreeElement.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBindThreeElement.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//cashOutCardBindThreeElement.txt",
				"===============================================");
		
		// 三要素无验绑定提现卡请求返回检查
		assert rsp.contains(dt.get("baseline").get("expect_return"));

		Reporter.log("三要素无验绑定提现卡接口测试： " + dt.get("baseline").get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "cashOutCardBindThreeElement")
	public Iterator<Object[]> data4cashOutCardBindThreeElement() throws IOException {
		return new ExcelProvider(this, "cashOutCardBindThreeElement", 111);
	}

	@DataProvider(name = "provider1")
	public Iterator<Object[]> provider1() throws IOException {
		return new ExcelProvider2(this, 3, 4);
	}

	@AfterClass
	public void afterClass() {

	}
}