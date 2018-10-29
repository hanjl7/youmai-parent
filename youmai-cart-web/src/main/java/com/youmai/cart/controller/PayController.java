package com.youmai.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.order.service.OrderService;
import com.youmai.pay.service.WeixinPayService;
import com.youmai.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
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

    @Reference
    private OrderService orderService;

    /**
     * @return java.util.Map
     * @Description 生成二维码
     * @Date 22:18 2018/10/11
     * @Param []
     **/
    @RequestMapping("/createNative")
    public Map createNative() {
        //获取当前用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //得到redis中的数据
        TbPayLog payLog = orderService.searchPayLogFromRedis(username);
        //判断支付日志是否存在
        if (payLog != null) {
            return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
        } else {
            return new HashMap();
        }

    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result = null;
        int count = 0;
        while (true) {
            //调用查询接口
            Map map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "支付出错");
                break;
            }

            if (map.get("trade_state").equals("SUCCESS")) {
                result = new Result(true, "支付成功");
                //修改订单状态
                orderService.updateOrderStatus(out_trade_no, (String) map.get("transaction_id"));

                break;
            }

            count++;
            System.out.println("count = " + count);
            if (count >= 10) {
                result = new Result(false, "二维码超时");
                break;
            }

            try {
                //间隔6秒查询，否则一直循环
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
}
