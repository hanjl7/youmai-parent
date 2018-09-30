package com.youmai.page.service.impl;

import com.youmai.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @ClassName: PageDeleteListener
 * @Description:
 * @Author: 泊松
 * @Date: 2018/9/29 22:45
 * @Version: 1.0
 */
@Component
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;


        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("PageDeleteListener监听到信息"+goodsIds);
            boolean flag = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("网页删除结果"+flag);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
