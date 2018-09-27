package com.youmai.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youmai.pojo.TbItem;
import com.youmai.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ItemSearchServiceImpl
 * @Description:
 * @Author: 泊松
 * @Date: 2018/9/25 21:53
 * @Version: 1.0
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        Map<String, Object> map = new HashMap<>();

        System.out.println(searchMap);

       /* SimpleQuery query = new SimpleQuery();
        //查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        map.put("rows", page.getContent());*/

        //1.查询高亮列表
        map.putAll(searchList(searchMap));
        //2.根据关键字查询商品分类
        List categoryList = searchCategory(searchMap);
        map.put("categoryList", categoryList);
        //3.根据模板id查询品牌规格列表
        String categoryName = (String) searchMap.get("category");
        if (!categoryName.equals("")) {
            //分类有名称，按照分类名称查询
            map.putAll(searchBrandAndSpecList(categoryName));
        } else {
            //没有分类名称
            if (categoryList.size() > 0) {
                map.putAll(searchBrandAndSpecList((String) categoryList.get(0)));
            }
        }
        return map;
    }

    /**
     * @return java.util.List
     * @Description 返回高亮查询列表
     * @Date 20:12 2018/9/26
     * @Param [searchMap]
     **/
    private Map searchList(Map searchMap) {
        Map map = new HashMap();
        HighlightQuery query = new SimpleHighlightQuery();
        //高亮初始化
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//设置高亮的域
        highlightOptions.setSimplePrefix("<em style='color:red'>");//高亮前缀
        highlightOptions.setSimplePostfix("</em>");//高亮后缀
        query.setHighlightOptions(highlightOptions);//设置高亮选项
        //1.按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //2.按照商品分类过滤
        if (!"".equals(searchMap.get("category"))) {
            //从前端传过来的结果中查询过滤  条件名称
            Criteria criteriaCategory = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFacetQuery(criteriaCategory);
            query.addFilterQuery(filterQuery);
        }

        //3.按照品牌过滤
        if (!"".equals(searchMap.get("brand"))) {
            //从前端传过来的结果中查询过滤  条件名称
            Criteria criteriaBrand = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFacetQuery(criteriaBrand);
            query.addFilterQuery(filterQuery);
        }

        //4.按照规格过滤 规格有多项，需要循环过滤。循环规格查询条件，根据key得到域名城，根据value设置过滤条件
        if (searchMap.get("spec") != null) {
            //从前端传过来的结果中查询过滤  条件名称
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria criteriaSpec = new Criteria("item_spec_" + key).is(searchMap.get(searchMap.get(key)));
                FilterQuery filterQuery = new SimpleFacetQuery(criteriaSpec);
                query.addFilterQuery(filterQuery);
            }
        }

        //5.按照价格过滤
        if (!searchMap.get("price").equals("")) {
            String[] price = ((String) searchMap.get("price")).split("-");
            if (!price[0].equals("0")) {
                //如果不是0开始  查询大于等于的价格
                Criteria criteriaPrice = new Criteria("item_price").greaterThanEqual(price[0]);
                FilterQuery filterQuery = new SimpleFacetQuery(criteriaPrice);
                query.addFilterQuery(filterQuery);
            }

            if (!price[1].equals("*")) {
                //如果不是*  查询小于等于的价格
                Criteria criteriaPrice = new Criteria("item_price").lessThanEqual(price[1]);
                FilterQuery filterQuery = new SimpleFacetQuery(criteriaPrice);
                query.addFilterQuery(filterQuery);

            }
        }

        //



        /*高亮结果集*/
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        for (HighlightEntry<TbItem> h : page.getHighlighted()) {//循环高亮入口集合
            TbItem item = h.getEntity();//获取原实体类
            if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果
            }
        }
        map.put("rows", page.getContent());
        return map;
    }


    /**
     * @return java.util.List
     * @Description 查询分类列表 分组查询
     * @Date 20:00 2018/9/26
     * @Param [searchMap]
     **/
    private List searchCategory(Map searchMap) {
        List list = new ArrayList();
        Query query = new SimpleQuery();
        //根据关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //得到分页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //根据列得到结果集
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //得到分组结果入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //分组入口集合
        List<GroupEntry<TbItem>> content = groupEntries.getContent();
        for (GroupEntry<TbItem> entry : content) {
            //将分组的结果封装到返回值中
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * @return java.util.Map
     * @Description 查询品牌列表和规格列表
     * @Date 16:03 2018/9/27
     * @Param [category]
     **/
    private Map searchBrandAndSpecList(String category) {
        Map map = new HashMap<>();
        //查询出typeid
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (typeId != null) {
            //根据模板id查询列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);
            //根据模板id查询列表规格
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }

        return map;
    }

}
