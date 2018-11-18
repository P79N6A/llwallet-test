package com.llwallet.interfaces.test.api.test.personal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.personal.UserBankCard;
import com.llwallet.interfaces.dao.DbUtils;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpFixture;
import com.tools.utils.GenSign;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 签约查询接口测试
 */

public class UserBankCardTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "签约查询接口测试", timeOut = 600000, dataProvider = "userBankCard")
	public void userBankCard(Map<String, String> datadriven) throws Exception {

		UserBankCard userBankCard = new UserBankCard();
		userBankCard.setOid_partner(datadriven.get("oid_partner"));
		userBankCard.setSign_type(datadriven.get("sign_type"));
		userBankCard.setUser_id(datadriven.get("user_id"));
		userBankCard.setPay_type(datadriven.get("pay_type"));
		if ("auto".equals(datadriven.get("no_agree"))){			
			DbUtils dbUtils = new DbUtils();
			String no_agree = dbUtils.querySigned(datadriven.get("user_id")).getAgreementno();
			userBankCard.setNo_agree(no_agree);
		}else{
			userBankCard.setNo_agree(datadriven.get("no_agree"));
		}
		userBankCard.setCard_no(datadriven.get("card_no"));
		userBankCard.setCashcard_flag(datadriven.get("cashcard_flag"));
		userBankCard.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(userBankCard)),datadriven.get("key")));
		String reqJson = JSON.toJSONString(userBankCard);
		String userBankCardUrl = Property.get("llwallet.userBankCard.url");

		// 签约查询请求提交
		HttpFixture hf = new HttpFixture();
		hf.setEncode("UTF-8");
		hf.setUrl(userBankCardUrl);
		hf.addHeaderValue("Content-Type", "application/json");
		hf.addRequestBody(reqJson);
		hf.Post();
		// 签约查询请求返回检查
		assert hf.getResponseBody().contains(datadriven.get("result_code"));
		assert hf.getResponseBody().contains(datadriven.get("result_code1"));
		
		Reporter.log("签约查询接口测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}
	   
	@DataProvider(name = "userBankCard")
	public Iterator<Object[]> data4userBankCard() throws IOException {
		return new ExcelProvider(this, "userBankCard", 2);
	}
	
	@AfterClass
	public void afterClass() {

	}

}