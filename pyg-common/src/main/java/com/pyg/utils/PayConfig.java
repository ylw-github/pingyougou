package com.pyg.utils;

/**
 * 微信支付参数
 */
public class PayConfig {
	//企业方公众号Id
	public static String appid = "wx8397f8696b538317";
	//财付通平台的商户账号
	public static String partner = "1473426802";
	//财付通平台的商户密钥
	public static String partnerkey = "8A627A4578ACE384017C997F12D68B23";  
	//回调URL
	public static String notifyurl = "http://a31ef7db.ngrok.io/WeChatPay/WeChatPayNotify";
}
