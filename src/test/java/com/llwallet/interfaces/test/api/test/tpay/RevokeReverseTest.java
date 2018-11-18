package com.llwallet.interfaces.test.api.test.tpay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.llwallet.interfaces.bean.tpay.CreateQr;
import com.llwallet.interfaces.bean.tpay.RiskInfo;
import com.tools.dataprovider.ExcelProvider2;
import com.tools.http.HttpRequest;
import com.tools.utils.GenSign;
import com.tools.utils.MyConfig;
import com.tools.utils.MyDate;
import com.tools.utils.Property;
import com.tools.utils.SampleFileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/*
 * @author jiangxm
 * 撤销冲正测试
 */

public class RevokeReverseTest {

	@BeforeClass
	public void beforeClass() throws InterruptedException {
		Property.set();
	}

	@SuppressWarnings({ "rawtypes" })
	@Test(description = "撤销冲正测试", timeOut = 60000, dataProvider = "provider")
	public void test(LinkedHashMap<String, LinkedHashMap<String, String>> dt, ExcelProvider2 excel) throws Exception {

		// 生成二维码
		Thread.sleep(1000);
		CreateQr createQr = new CreateQr();
		RiskInfo riskInfo = new RiskInfo();
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = dt.get("createQr").entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!entry.getValue().equals("") && StringUtils.startsWith((String) entry.getKey(), "riskInfo.")) {
				params.put(((String) entry.getKey()).split("\\.")[1], (String) entry.getValue());
			}
		}
		BeanUtils.populate(riskInfo, params);
		createQr.setRiskInfo(riskInfo);
		createQr.setSign_type(dt.get("createQr").get("sign_type"));
		createQr.setOid_partner(MyConfig.getPartner(dt.get("createQr").get("oid_partner")));
		createQr.setVersion(dt.get("createQr").get("version"));
		createQr.setUser_id(dt.get("createQr").get("user_id"));
		createQr.setTimestamp(MyConfig.getTimestamp(dt.get("createQr").get("timestamp")));
		createQr.setAuth_flag(dt.get("createQr").get("auth_flag"));
		createQr.setAuth_amt(dt.get("createQr").get("auth_amt"));
		createQr.setDt_order(MyConfig.getDtOrder(dt.get("createQr").get("dt_order")));
		createQr.setNo_order(MyConfig.getNoOrder(dt.get("createQr").get("no_order")));
		createQr.setPay_partner(dt.get("createQr").get("pay_partner"));
		createQr.setQr_validtime(dt.get("createQr").get("qr_validtime"));
		createQr.setSign(GenSign.genSign(JSON.parseObject(JSON.toJSONString(createQr)), dt.get("createQr").get("key")));
		String createQrUrl = Property.get("tpay.createQr.url");
		String reqJson = JSON.toJSONString(createQr);
		String rsp = HttpRequest.httpPostWithJSON(createQrUrl, reqJson);
		// excel.writeCurrentCell("createQr", "actual_return", rsp);
		String qr_no = JSONObject.parseObject(rsp).getString("qr_no");
		System.out.println("qr_no-------" + qr_no);

		// 消费
		JSONObject json = new JSONObject();
		JSONObject extParam = new JSONObject();
		JSONObject riskParam = new JSONObject();

		json.put("merchantNo", dt.get("createQr").get("trade.merchantNo"));
		json.put("version", dt.get("createQr").get("trade.version"));
		json.put("signType", dt.get("createQr").get("trade.signType"));
		json.put("signData", dt.get("createQr").get("trade.signData"));
		json.put("requestDateTime", MyConfig.getDtOrder(dt.get("createQr").get("trade.requestDateTime")));
		json.put("outTradeNo", MyConfig.getNoOrder(dt.get("createQr").get("trade.outTradeNo")));
		json.put("outTradeDate", MyConfig.getDtOrder(dt.get("createQr").get("trade.outTradeDate")));
		json.put("tradeAmount", dt.get("createQr").get("trade.tradeAmount"));
		json.put("elecCardNo", dt.get("createQr").get("trade.elecCardNo"));
		json.put("elecBankCode", dt.get("createQr").get("trade.elecBankCode"));
		json.put("merchantName", dt.get("createQr").get("trade.merchantName"));
		json.put("merchantType", dt.get("createQr").get("trade.merchantType"));
		json.put("tradeName", dt.get("createQr").get("trade.tradeName"));
		json.put("cardAccpTrId", dt.get("createQr").get("trade.cardAccpTrId"));
		json.put("tradeSubject", dt.get("createQr").get("trade.tradeSubject"));
		json.put("password", dt.get("createQr").get("trade.password"));
		json.put("pinBlock", dt.get("createQr").get("trade.pinBlock"));
		json.put("operationType", dt.get("createQr").get("trade.operationType"));
		json.put("secAuth", dt.get("createQr").get("trade.secAuth"));
		json.put("terminalType", dt.get("createQr").get("trade.terminalType"));
		json.put("returnParam", dt.get("createQr").get("trade.returnParam"));

		if ("get".equals(dt.get("createQr").get("trade.qrcodePaymentCode"))) {
			extParam.put("qrcodePaymentCode", qr_no);
		} else {
			extParam.put("qrcodePaymentCode", dt.get("createQr").get("trade.qrcodePaymentCode"));
		}
		extParam.put("tokenFlag", "QR");
		json.put("extParam", extParam.toString());

		riskParam.put("primary_acct_num", "test123");
		riskParam.put("processing_code", "00X000");
		riskParam.put("amt_trans", "100");
		riskParam.put("transmsn_date_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		riskParam.put("sys_trace_audit_num", "2345");
		riskParam.put("mchnt_type ", "3456");
		riskParam.put("pos_entry_mode_code", "3456789097654");
		riskParam.put("pos_cond_code", "3456789876");
		riskParam.put("acq_inst_id_code", "987654");
		riskParam.put("fwd_inst_id_code", "45678");
		riskParam.put("retrivl_ref_num", "877654");
		riskParam.put("card_accptr_termnl_id", "3435879");
		riskParam.put("card_accptr_id", "987654");
		riskParam.put("card_accptr_name_loc", "43758690-");
		json.put("riskParam", riskParam.toString());

		String tradeUrl = Property.get("tpay.trade.url");
		String rsp1 = HttpRequest.httpPostWithJSON(tradeUrl, json.toString());
		String no_orderConsumer1 = JSONObject.parseObject(rsp1).getString("outTradeNo");

		// 撤销
		JSONObject revokeJson = new JSONObject();
		revokeJson.put("merchantNo", dt.get("createQr").get("revoke.merchantNo"));
		revokeJson.put("version", dt.get("createQr").get("revoke.version"));
		revokeJson.put("requestDateTime", MyConfig.getDtOrder(dt.get("createQr").get("revoke.requestDateTime")));
		revokeJson.put("outTradeNo", MyConfig.getNoOrder(dt.get("createQr").get("revoke.outTradeNo")));
		revokeJson.put("signData", dt.get("createQr").get("revoke.signData"));
		revokeJson.put("signType", dt.get("createQr").get("revoke.signType"));
		revokeJson.put("tradeAmount", dt.get("createQr").get("revoke.tradeAmount"));
		revokeJson.put("originalOutTradeNo", no_orderConsumer1);// 原消费流水外部单号
	    //revokeJson.put("originalOutTradeNo", "ConsumerBill20180125133944");//原单已撤销冲正成功，重复撤销冲正
	    //revokeJson.put("originalOutTradeNo", "ConsumerBill20180125134932");//原单为撤销冲正处理中，重复撤销冲正
	    //revokeJson.put("originalOutTradeNo", "ConsumerBill20180125140340");//原单为撤销冲正失败，重复撤销冲正
	    //revokeJson.put("originalOutTradeNo", "ConsumerBill20180125144744");//原单为撤销冲正失败，重复撤销冲正
		revokeJson.put("elecCardNo", dt.get("createQr").get("revoke.elecCardNo"));
		revokeJson.put("elecBankCode", dt.get("createQr").get("revoke.elecBankCode"));
		revokeJson.put("password", dt.get("createQr").get("revoke.password"));
		revokeJson.put("pinBlock", dt.get("createQr").get("revoke.pinBlock"));
		revokeJson.put("tradeSubject", "连连测试kkghdfdsgj");
		revokeJson.put("returnParam ", "5416");
		revokeJson.put("extParam", "afdghfgjhfs");

		String revokeUrl = Property.get("tpay.revoke.url");
		String rsp2 = HttpRequest.httpPostWithJSON(revokeUrl, revokeJson.toString());
		String no_orderConsumer2 = JSONObject.parseObject(revokeJson.toString()).getString("outTradeNo");

		// 撤销冲正
		JSONObject reverseJson = new JSONObject();
		reverseJson.put("merchantNo", dt.get("createQr").get("reverse.merchantNo"));
		reverseJson.put("version", dt.get("createQr").get("reverse.version"));
		reverseJson.put("requestDateTime", MyConfig.getDtOrder(dt.get("createQr").get("reverse.requestDateTime")));
		reverseJson.put("outTradeNo", MyConfig.getNoOrder(dt.get("createQr").get("reverse.outTradeNo")));
		reverseJson.put("signData", dt.get("createQr").get("reverse.signData"));
		reverseJson.put("signType", dt.get("createQr").get("reverse.signType"));
		reverseJson.put("tradeAmount", dt.get("createQr").get("reverse.tradeAmount"));
		reverseJson.put("originalOutTradeNo", no_orderConsumer2);// 原消费流水外部单号
	    //reverseJson.put("originalOutTradeNo", "ConsumerBill20180124151519");//原消费流水外部单号,消费冲正成功，重复消费冲正
	    //reverseJson.put("originalOutTradeNo", "ConsumerBill20180124151519");//原消费流水外部单号，消费冲正失败，重复消费冲正
	    //reverseJson.put("originalOutTradeNo", "ConsumerBill20180129200909");//对同一笔消费并发发起消费冲正
	    //reverseJson.put("originalOutTradeNo", "ConsumerBill20180129211612");//原支付单已关闭
		reverseJson.put("tradeType", dt.get("createQr").get("reverse.tradeType"));
		reverseJson.put("elecCardNo", dt.get("createQr").get("reverse.elecCardNo"));
		reverseJson.put("elecBankCode", dt.get("createQr").get("reverse.elecBankCode"));
		reverseJson.put("tradeSubject", "连连测试kkghdfdsgj");
		reverseJson.put("returnParam ", "5416");
		reverseJson.put("extParam", "afdghfgjhfs");

		String reverseUrl = Property.get("tpay.reversal.url");
		String rsp3 = HttpRequest.httpPostWithJSON(reverseUrl, reverseJson.toString());

		Reporter.log("撤销冲正测试： " + dt.get("createQr").get("comment"), true);
		System.out.println("===============================================");

		// 请求&响应写入文件
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", MyDate.getStringDate());
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", reqJson);
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", rsp);
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", json.toString());
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", rsp1);
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", revokeJson.toString());
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", rsp2);
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", reverseJson.toString());
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", rsp3);
		SampleFileUtils.appendLine("D://TA//log//revokereverse.txt", "===============================================");

	}

	@DataProvider(name = "provider")
	public Iterator<Object[]> provider() throws IOException {
		return new ExcelProvider2(this, 3);
	}

	@AfterClass
	public void afterClass() {

	}
}