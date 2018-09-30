package com.youmai.page.service.impl;

import com.youmai.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @ClassName: PageListener
 * @Description: 静态页面监听器
 * @Author: 泊松
 * @Date: 2018/9/29 21:52
 * @Version: 1.0
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            String text = textMessage.getText();
            System.out.println("接收到消息 ：" + text);
            boolean flag = itemPageService.genItemHtml(Long.parseLong(text));

            System.out.println("网页生成结果 ： " + flag);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
