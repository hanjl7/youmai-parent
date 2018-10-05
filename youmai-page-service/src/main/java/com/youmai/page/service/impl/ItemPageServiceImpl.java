package com.youmai.page.service.impl;

import com.youmai.mapper.TbGoodsDescMapper;
import com.youmai.mapper.TbGoodsMapper;
import com.youmai.mapper.TbItemCatMapper;
import com.youmai.mapper.TbItemMapper;
import com.youmai.page.service.ItemPageService;
import com.youmai.pojo.TbGoods;
import com.youmai.pojo.TbGoodsDesc;
import com.youmai.pojo.TbItem;
import com.youmai.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {

        Configuration configuration = freeMarkerConfig.getConfiguration();
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

            //商品分类
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dateMap.put("itemCat1", itemCat1);
            dateMap.put("itemCat2", itemCat2);
            dateMap.put("itemCat3", itemCat3);

            //SUK列表
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            //状态为有效
            criteria.andStatusEqualTo("1");
            //查询指定SPU id
            criteria.andGoodsIdEqualTo(goodsId);
            //按照状态排序，指定为第一个
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            dateMap.put("itemList", itemList);


            //输出
            Writer out = new FileWriter(pagedir + goodsId + ".html");
            template.process(dateMap, out);
            out.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public boolean deleteItemHtml(Long[] goodsId) {
        try {
            for (Long id : goodsId) {
                new File(pagedir + id + ".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
