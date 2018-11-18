package com.tools.utils;

public class SignTest {

	public static void main(String[] args) throws Exception {

		// AP支付 MD5签名方式 验签 比较生成的md5值与返回的partner_sign是否一致
		String a = "dt_order=20170616165910&info_order=20170616165910&money_order=0.01&no_order=LL20170616165910&oid_partner=201602220000099008&oid_paybill=2017061655611113&partner_sign_type=MD5&ret_code=0000&ret_msg=交易成功&key=201602220000099008";
		System.out.println("MD5------:" + GenSign.getSignMD5(a));

		String b = "bank_code=01020000&dt_order=20170705135823&info_order=WEB网关测试商品&money_order=0.01&no_order=WalletWeb20170705135823&oid_partner=201701120000283004&oid_paybill=2017070559246375&pay_type=2&result_pay=SUCCESS&settle_date=20170705&sign_type=RSA";
		String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign = "R5cZzBXlZcD09Bs6Qh/Gqg95ZGxfDUeAUprR1iSiCzKdOby+KNhO31AHuVJgzkgsn6BycuX5pJTd1PUK2z9aYx22fosVjvAiYXsEh8QRfiofENTqb417QTw9H716UaE93ALrIAB6p01LUeTnPVkf5E/0usZ6h1XXLoZ/HADUoAM=";
		System.out.println();
		System.out.println("RSA验证签名------:" + RSAUtil.checksign(key, b, sign));

		String b1 = "dt_order=20170705140147&info_order=WEB网关测试商品&money_order=0.01&no_order=WalletWeb20170705140147&oid_partner=201701120000283004&oid_paybill=2017070559249675&pay_type=0&result_pay=SUCCESS&settle_date=20170705&sign_type=RSA";
		String key1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign1 = "KOWCyAxem2hQH3Kiv0o1b0ZvhBdOrGtNL5tK3VJai9yQzS4xUiBvsE+iDwDR+06PFZi1c6ZeFZEnNtO5GrnyMTP5LfLtQWk6Fv0Cu/BgkuEsXIwl8K0iiAZSG+NtKNdYNd4HiFfhoWPXdB75W0sNxMWd7aS893o5ziYwDwFwTJ4=";
		System.out.println();
		System.out.println("RSA验证签名1------:" + RSAUtil.checksign(key1, b1, sign1));

		// web组件版提现
		String b2 = "balance=98.85&bankCode=01040000&bankName=中国银行&errorCode=0000&errorMessage=交易成功&freezeBalance=1.13&oidPartner=201701120000283004&signType=RSA&userId=test9001@yintong.com.cn";
		String key2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign2 = "Ee47uY3E0LgWO9HVwpO0Ueje1Z60AlvaeStMQsoFZlkKKenkLbI7CKgsi82c6FWEaAJaS/rtSNulaNEHD/5fcZ3b830CuZ8jWQwWIW6fXzrYdNB3FY8Jz6etXj7fDSXrPS/VejZBmbYHCPTt2/1pjX0eLKRi/GTzsSmyZWd5vQ4=";
		System.out.println();
		System.out.println("RSA验证签名2------:" + RSAUtil.checksign(key2, b2, sign2));

		// web组件版开户&实名认证
		String b3 = "addrConn=大师傅&bindMobile=140****9090&errorCode=0000&errorMessage=交易成功&expIdCard=99990101&expLicense=99990101&idCardNo=1*******3&idCardType=1&licenseNumber=123123123&oidPartner=201701120000283004&registerDate=2017-08-18 13:33:56&signType=RSA&userId=test9090@yintong.com.cn&userName=阿什顿发斯蒂芬&userState=0&userType=1";
		String key3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign3 = "aEfMeQxV95S+PRRkPuyllrPt4tH1xrU/eKcdxxj4EDYnZyg4QoJCm6Q33x1o0q4Zwb8aHLm0jAJg2S6Cck/lILt9uxJZVccUdblcRAW9XLeQkQ1zP9CGYh/PFgaYPPDG018n8Hhl4+gWyWiJBjUX9o6kjtfVruEWzx+kjvbdPwM=";
		System.out.println();
		System.out.println("RSA验证签名3------:" + RSAUtil.checksign(key3, b3, sign3));

		// 聚合SDK 
		String b4 = "dt_order=20170818163052&money_order=0.01&no_order=201708181630527cF&oid_partner=201610201001187480&oid_paybill=2017081833174934&pay_type=X&ret_code=0000&ret_msg=支付完成&sign_type=RSA";
		String key4 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign4 = "cZyXV7iLLKLv3OgV2yZ75146jemb6D0UCDeagUY3VxvXBKwDq2PS6ikF8XKWa9VwNUdOYZ27F9vaU8XMREfIB4hIuNPE9tia408MRhYo5eBSfx+suAhP88N0SVfX3rTDVikHXXvbTPWMfFvOKMJEwxAE9dZkH6yrmKkYy/uI1wKw=";
		System.out.println();
		System.out.println("RSA验证签名4------:" + RSAUtil.checksign(key4, b4, sign4));
		
		// 聚合SDK 
		String b5 = "dt_order=20170818171231&money_order=0.01&no_order=20170818171231zVT4&oid_partner=201610201001187480&oid_paybill=2017081833427996&pay_type=X&ret_code=0000&ret_msg=支付完成&sign_type=RSA";
		String key5 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign5 = "L6MiujmRwy7/jhxRMXOzoCuV4EK08o+hS0MBXra9aY8HHglmn58/ri1YEF08pqjylmMpW76smeaPGXNtnurqsVCr+jJqlNHL0NOr9hy8hHiRVGzxES8j6Bi+W1jBuaZtZJxppqIV1St+zeRByEr403TA34YknPinAsuUAdBV64A=";
		System.out.println();
		System.out.println("RSA验证签名5------:" + RSAUtil.checksign(key5, b5, sign5));
		
		// 钱包组件web 找回密码、重置密码
		String b6 = "errorCode=0000&errorMessage=交易成功&oidPartner=201701120000283004&signType=RSA&userId=test9001@yintong.com.cn";
		String key6 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign6 = "ar9o4RRB3Z2XZ1Tt1lnkzujgeLjACD3kkAsJ/wnUYkeXLL4nVCs96bIwl6L/DnAsHKFJ5wbQ0afJvrlwHhC3X2l1HTFQ9vHhmBRgPJ7IolCRquXyhH3oHwT5Mw6YXFILye45osfDfZT+V+PxYZwF3DPV3+IC1MYlUhERXsHHRkM=";
		System.out.println();
		System.out.println("RSA验证签名6------:" + RSAUtil.checksign(key6, b6, sign6));
		
		// 钱包组件SDK 钱包管理
		String b7 = "balance=0.00&freeze_balance=0.00&level_auth=0&mob_bind=140****0002&name_user=*试·测试是了了是了是了就快了&no_idcard=1****************9&platform=201701120000283004&ret_code=0000&ret_msg=交易成功&sign_type=RSA&user_id=2017090202";
		String key7 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign7 = "YTHw83xmMpiCU1bODRj58PMT7UgSl37YFbyd7jnZf5yT+Xmcn4dLk5uPS655DGoph35Vf9M1jBuIIVf9BflEEim7B5Pood6FooTP5lT+8DoHkoNI55agG1Fu5PzEDHxpSK8/+YEfp45XvaMwyyeTU9PNrcAAYyMSmRBKBPvCPpQ=";
		System.out.println();
		System.out.println("RSA验证签名7------:" + RSAUtil.checksign(key7, b7, sign7));
		
		// 钱包组件SDK 个人开户		
		String b8 = "dt_register=2017-09-04 19:55:53&level_auth=0&mob_bind=140****0031&name_user=*萌&no_idcard=1****************9&oid_partner=201701120000283004&ret_code=0000&ret_msg=交易成功&sign_type=RSA&type_idcard=0&user_id=20170904003";
		String key8 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign8 = "LToH2vtlWD2o/gIzBgQHUrNrZ4q5bw/LHYlszkp2tggQMzSgDFRz4ShFjJff7GDB7nteI1xr8W30gSjDgpVimBM7ClcNxu067ahhFeKg1zgL9tDWn3lbBbngSoRc00kJCk9JksZaMypp5gpcwPs4LKEMqzCBEz3oGvj4TvA3Md4=";
		System.out.println();
		System.out.println("RSA验证签名8------:" + RSAUtil.checksign(key8, b8, sign8));		
		
		// 钱包组件SDK 充值 提现		
		String b9 = "bank_code=01020000&dt_order=20170905164358&money_order=5.0&no_order=20170905164358_n7I&oid_partner=201701120000283004&oid_paybill=2017090563942911&pay_type=2&result_pay=SUCCESS&settle_date=20170905&sign_type=RSA";
		String key9 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign9 = "VnN1reIillkP5uI6pYBWXElc1DMSCRSyl5YR6vsMFPCMi6+2dqmrt0q7AoSK4EPrhTJRyfTw51DEa45gn4HkogxyddnQYqaFhiTPrvXf3Fz6aak1Igf2MwsHZjzRNHnwDtQaGfWD3qW/8jQ3NZvhdW0AJzxQbOrWye3R/lYkjPg=";
		System.out.println();
		System.out.println("RSA验证签名9------:" + RSAUtil.checksign(key9, b9, sign9));
		
		// 余额到他人卡 api		
		String b10 = "balance=161678.06&freeze_balance=7447.77&oid_partner=201701120000283004&oid_paybill=2017091466803963&ret_code=0000&ret_msg=交易成功&sign_type=RSA&user_id=20170328152612914";
		String key10 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
		String sign10 = "d+Bn81HRK59h1gSG4mxJWFrHRzhq1dDEhdnGyhk2NvLLBsDTxnH+Iq2O9vCUeBH2YnXp9jFmhbCY+W9MeM0NbhwvPNh3dx484QSLtckUr9oeKMoaK7Tws4TJi1CSdqnJDxSjj835Lo5ZIWg/LT3ucrTLMJUF6w+GqYv3pMT03GE=";
		System.out.println();
		System.out.println("RSA验证签名10------:" + RSAUtil.checksign(key10, b10, sign10));

	}
}
