package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

@Component
public class ItemSearchListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("创建查询索引...");

        try {
            TextMessage textMessage=(TextMessage)message;
            String text = textMessage.getText();
            List<TbItem> items = JSON.parseArray(text, TbItem.class);
            for (TbItem item : items) {
                //将 spec 字段中的 json字符串转换为 map
                Map specMap=JSON.parseObject(item.getSpec());
                item.setSpecMap(specMap);
            }

            itemSearchService.importList(items);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
