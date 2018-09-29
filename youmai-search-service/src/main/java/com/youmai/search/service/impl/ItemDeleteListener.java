package com.youmai.search.service.impl;

import com.youmai.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * @ClassName: ItemDeleteListener
 * @Description: 监听 删除索引库
 * @Author: 泊松
 * @Date: 2018/9/29 20:53
 * @Version: 1.0
 */
@Component
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("itemDeleteListener监听收到消息" + ids);

            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));

            System.out.println("删除索引库");
            System.out.println("了");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
