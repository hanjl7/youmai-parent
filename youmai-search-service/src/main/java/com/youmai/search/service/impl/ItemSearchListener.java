package com.youmai.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.youmai.pojo.TbItem;
import com.youmai.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ItemSearchListener
 * @Description:
 * @Author: 泊松
 * @Date: 2018/9/29 19:20
 * @Version: 1.0
 */
@Component
public class ItemSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("监听到消息");

        try {
            TextMessage textMessage= (TextMessage) message;
            String text = textMessage.getText();

            List<TbItem> itemList = JSON.parseArray(text, TbItem.class);
            for (TbItem item : itemList) {
                System.out.println(item.getId()+"  "+item.getTitle());
                //将item中spec字段中的json字符串转换成map
                Map specMap =JSON.parseObject(item.getSpec());
                //给带注解的字段赋值
                item.setSpecMap(specMap);
            }
            itemSearchService.importList(itemList);
            System.out.println("导入到Solr索引库");

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
