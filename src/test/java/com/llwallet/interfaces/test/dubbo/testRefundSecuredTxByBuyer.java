package com.llwallet.interfaces.test.dubbo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.alibaba.fastjson.JSON;
import com.tools.dataprovider.ExcelProvider;
import com.tools.utils.ApiUtils;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.lianpay.gateway.base.domain.RefundReq;
import com.lianpay.gateway.base.util.JSONHandler;
import com.lianpay.gateway.refund.dubbo.service.IRefundService;

@ContextConfiguration(locations = { "classpath:context/dubbo-consumer.xml" })
public class testRefundSecuredTxByBuyer extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Autowired
	private IRefundService refundService;


	@Test(description = "担保交易单退款", timeOut = 60000, dataProvider = "refundSecured")
	public void refundSecured(Map<String, String> datadriven) throws Exception {
		
		//String correlationID = ApiUtils.getCurrentDateMillisecondStr();
		
		String Oid_billno=datadriven.get("Oid_billno");

		//testrefundSecuredTxByBuyer req = new testrefundSecuredTxByBuyer();	
		RefundReq req = new RefundReq();
		//RefundUserAcctReq req =new RefundUserAcctReq();
		

		req.setOid_trader("201606010000149006");// 退货商户号
		//req.setOrder_id(com.lianpay.share.utils.UuidUtil.getUuid());// ("RFD_"+DateUtil.getCurrentDateTimeStr1());//外部退货订单号
		req.setOrder_id(ApiUtils.getCurrentDateMillisecondStr());
		req.setOrder_dt(ApiUtils.getCurrentDateSecondStr());// 外部退货时间
		req.setAmt("100");// 退款金额
		req.setOld_oid_billno(Oid_billno);// 原支付单号
		req.setProc_src("127.0.0.1");
		req.setNotify_url("http://192.168.20.151:7220/gateway/sdk/paycreate");
			// 担保卖方ID
		req.setSec_seller_id("201606150000160004");
			// 担保卖方类型
		req.setSec_seller_type("1");
			// refReq.setRefund_currency("USD");// 退货外币币种
			// refReq.setMoney_refund_foreign("10");// 退货外币金额
		String refreq_req = JSONHandler.toJSONString(req);
		/*	System.out.println("请求报文：" + refreq_req);
			String refreq_res = refundService.refund(refreq_req);
			System.out.println("返回报文：" + refreq_res);*/
		
		System.out.println("refreq_req----" + refreq_req);
		//SecuredTxBuyerConfirmResp resp = securedTransactionService.confirmSecuredTxByBuyer(req);
		
		System.out.println("refundSecured：1111111111111111111111 " + JSON.toJSONString(req));
		String rsp = JSON.toJSONString(refundService.refundByUserAcct(refreq_req));
		System.out.println("后端返回：：：：：：：：：：：：：：：：：：：：：：：； ： " + rsp);
		Reporter.log("担保交易：" + rsp);
		// 查询余额消费是否超限请求返回检查
		assert rsp.contains(datadriven.get("result_code"));

		Reporter.log("担保交易： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
	}

	@DataProvider(name = "refundSecured")
	public Iterator<Object[]> data4refundSecured() throws IOException {
		return new ExcelProvider(this, "refundSecured",1);
	}
	

	@AfterClass
	public void afterClass() {

	}
}