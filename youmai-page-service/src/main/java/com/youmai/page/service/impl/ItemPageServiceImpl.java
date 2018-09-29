package com.youmai.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youmai.mapper.TbGoodsDescMapper;
import com.youmai.mapper.TbGoodsMapper;
import com.youmai.page.service.ItemPageService;
import com.youmai.pojo.TbGoods;
import com.youmai.pojo.TbGoodsDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ItemPageServiceImpl
 * @Description: 生成网站详情页
 * @Author: 泊松
 * @Date: 2018/9/28 21:04
 * @Version: 1.0
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public boolean genItemHtml(Long goodsId) {

        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate("item.ftl");
            //创建数据模型
            Map dateMap = new HashMap<>();
            //商品主表数据SPU
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dateMap.put("goods", goods);
            //商品扩展表数据SPU
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dateMap.put("goodsDesc", goodsDesc);

            //输出
            Writer out = new FileWriter(pagedir + goodsId + ".html");
            out.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
