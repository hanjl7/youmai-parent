package com.youmai.pay.service;

import java.util.Map;

/**
 * @Description 微信支付接口
 * @Date 21:22 2018/10/11
 * @Param * @param null
 * @return
 **/
public interface WeixinPayService {

    /**
     * @return java.util.Map
     * @Description 生成微信支付二维码
     * @Date 21:24 2018/10/11
     * @Param [out_trade_no, total_fee]
     **/
    public Map createNative(String out_trade_no, String total_fee);

    /**
     * @Description 查询支付状态
     * @Date 19:14 2018/10/12
     * @Param [out_trade_no]
     * @return java.util.Map
     **/
    public Map queryPayStatus(String out_trade_no);
}
