package com.llwallet.interfaces.test.dubbo;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.utils.ApiUtils;
import com.yintong.llwallet.domain.TrustAuth;
import com.yintong.llwallet.service.ILLWalletCoreService;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class QueryKYC extends AbstractTestNGSpringContextTests {

	@Autowired
	private ILLWalletCoreService llwalletCoreService;

	@Test
	public void test() {

		String correlationID = ApiUtils.getCurrentDateMillisecondStr();

		TrustAuth trustAuth = new TrustAuth();
		trustAuth.setCorrelationID(correlationID);
		trustAuth.setOid_partner("201701120000283004");
		trustAuth.setUser_id("20171017001");
		System.out.println("trustAuth： " + JSON.toJSONString(trustAuth));
		String rsp = JSON.toJSONString(llwalletCoreService.queryKYC(trustAuth));
		System.out.println("查询KYC等级返回：" + rsp);

		System.out.println("===============================================");
	}

}