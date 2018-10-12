package com.youmai.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.youmai.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WeixinPayServiceImpl
 * @Description: createNative
 * @Author: 泊松
 * @Date: 2018/10/11 21:25
 * @Version: 1.0
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    /**
     * @return java.util.Map
     * @Description 生成微信支付二维码
     * @Date 21:26 2018/10/11
     * @Param [out_trade_no, total_fee]
     **/
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //1 创建参数
        Map param = new HashMap();
        //公众号账户
        param.put("appid", appid);
        //商户
        param.put("mch_id", partner);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("body", "youmai");
        //订单交易号
        param.put("out_trade_no", out_trade_no);
        //金额（分）
        param.put("total_fee", total_fee);
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("notify_url", "http://www.google.com");
        //交易类型
        param.put("trade_type", "NATIVE");


        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求的参数" + xmlParam);

            //2 发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();

            //3 获取结果
            String xmlResult = httpClient.getContent();

            Map<String, String> mapResult = WXPayUtil.xmlToMap(xmlResult);
            System.out.println("微信返回的结果" + mapResult);
            Map map = new HashMap();
            //生成二维码的链接
            map.put("code_url", mapResult.get("code_url"));
            map.put("out_trade_no", out_trade_no);
            map.put("total_fee", total_fee);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }

    }
}
