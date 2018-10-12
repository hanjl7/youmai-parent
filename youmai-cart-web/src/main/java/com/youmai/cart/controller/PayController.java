package com.youmai.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.pay.service.WeixinPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.Map;

/**
 * @ClassName: PayController
 * @Description: 支付控制层
 * @Author: 泊松
 * @Date: 2018/10/11 22:07
 * @Version: 1.0
 */
@RestController
@RequestMapping("pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    /**
     * @return java.util.Map
     * @Description 生成二维码
     * @Date 22:18 2018/10/11
     * @Param []
     **/
    @RequestMapping("/createNative")
    public Map createNative() {
        IdWorker idWorker = new IdWorker();
        return weixinPayService.createNative(idWorker.nextId() + "", "1");
    }
}
