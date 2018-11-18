package com.llwallet.interfaces.test.api.online.combination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.llwallet.interfaces.bean.combination.CombinationRefund;
import com.llwallet.interfaces.bean.combination.RefundSerialList;
import com.tools.dataprovider.ExcelProvider;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.Property;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 组合支付退款测试
 */

public class CombinationRefundOnlineTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@Test(description = "组合支付退款测试", timeOut = 60000, dataProvider = "combinationRefund")
	public void combinationRefund(Map<String, String> datadriven) throws Exception {

		Thread.sleep(100);
		String refundSerialList1 = datadriven.get("refundSerialList1");
		String refundSerialList2 = datadriven.get("refundSerialList2");
		String refundSerialList3 = datadriven.get("refundSerialList3");
		RefundSerialList refundList1 = new RefundSerialList();
		RefundSerialList refundList2 = new RefundSerialList();
		RefundSerialList refundList3 = new RefundSerialList();
		List<RefundSerialList> refundList = new ArrayList<RefundSerialList>();
		CombinationRefund combinationRefund = new CombinationRefund();
		combinationRefund.setOid_partner(datadriven.get("oid_partner"));
		combinationRefund.setSign_type(datadriven.get("sign_type"));
		combinationRefund.setApi_version(datadriven.get("api_version"));
		combinationRefund.setTimestamp(MyConfig.getTimestamp(datadriven.get("timestamp")));
		combinationRefund.setUser_id(datadriven.get("user_id"));
		combinationRefund.setCol_oidpartner(datadriven.get("col_oidpartner"));
		combinationRefund.setNo_refund(MyConfig.getNoOrder(datadriven.get("no_refund")));
		combinationRefund.setDt_refund(MyConfig.getDtOrder(datadriven.get("dt_refund")));
		combinationRefund.setMoney_refund(datadriven.get("money_refund"));
		combinationRefund.setNo_order(datadriven.get("no_order"));
		combinationRefund.setDt_order(datadriven.get("dt_order"));
		combinationRefund.setOid_paybill(datadriven.get("oid_paybill"));
		combinationRefund.setNotify_url(datadriven.get("notify_url"));
		combinationRefund.setCol_custid(datadriven.get("col_custid"));
		combinationRefund.setCol_custtype(datadriven.get("col_custtype"));
		if (!StringUtils.isBlank(refundSerialList1)) {
			String result1[] = refundSerialList1.split(",");
			refundList1.setRefund_custId(result1[0]);
			refundList1.setCust_type(result1[1]);
			refundList1.setOrign_paytype(result1[2]);
			refundList1.setRefund_amt(result1[3]);
			refundList.add(refundList1);
		}
		if (!StringUtils.isBlank(refundSerialList2)) {
			String result2[] = refundSerialList2.split(",");
			refundList2.setRefund_custId(result2[0]);
			refundList2.setCust_type(result2[1]);
			refundList2.setOrign_paytype(result2[2]);
			refundList2.setRefund_amt(result2[3]);
			refundList.add(refundList2);
		}
		if (!StringUtils.isBlank(refundSerialList3)) {
			String result3[] = refundSerialList3.split(",");
			refundList3.setRefund_custId(result3[0]);
			refundList3.setCust_type(result3[1]);
			refundList3.setOrign_paytype(result3[2]);
			refundList3.setRefund_amt(result3[3]);
			refundList.add(refundList3);
		}
		combinationRefund.setRefundSerialList(refundList);
		combinationRefund.setSign(
				GenSign.genSign(JSON.parseObject(JSON.toJSONString(combinationRefund)), datadriven.get("key")));
		String reqJson = JSON.toJSONString(combinationRefund);
		String combinationRefundUrl = Property.get("llwallet.combinationRefund.url.online");

		String rsp = HttpRequest.httpPostWithJSON(combinationRefundUrl, reqJson);
		Reporter.log("组合支付退款测试： " + datadriven.get("comment"), true);
		System.out.println("===============================================");
		
		// 组合支付退款请求返回检查
		assert rsp.contains(datadriven.get("expect_return"));
		
	}

	@DataProvider(name = "combinationRefund")
	public Iterator<Object[]> data4combinationRefund() throws IOException {
		return new ExcelProvider(this, "combinationRefund",1);
	}

	@AfterClass
	public void afterClass() {

	}

}