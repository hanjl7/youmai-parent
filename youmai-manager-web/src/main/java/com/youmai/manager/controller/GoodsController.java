package com.youmai.manager.controller;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.youmai.pojo.TbItem;
import com.youmai.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.pojo.TbGoods;
import com.youmai.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    //@Reference  使用 activeMQ 解耦合
    // private ItemSearchService itemSearchService;

    /**
     * @Description 发送solr导入的数据
     * @Date 19:09 2018/9/29
     * @Param * @param null
     * @return
     **/
    @Autowired
    private Destination queueSolrDestination;

    @Autowired
    private Destination queueDeleteDestination;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        //获取登录名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //设置商家id
        goods.getGoods().setSellerId(name);
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            //   itemSearchService.deleteByGoodsIds(Arrays.asList(ids));

            jmsTemplate.send(queueDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });

            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * @return entity.Result
     * @Description 批量修改状态
     * @Date 19:12 2018/9/21
     * @Param [ids, status]
     **/
    @RequestMapping("updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            //按照SPU ID查询 SKU列表(状态为1)
            if (status.equals("1")) {
                List<TbItem> list = goodsService.findItemListByGoodsIdAndStatus(ids, status);
                //调用搜索接口实现数据批量导入
                if (list.size() > 0) {
                    //itemSearchService.importList(list);

                    final String jsonString = JSON.toJSONString(list);
                    jmsTemplate.send(queueSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {

                            return session.createTextMessage(jsonString);
                        }
                    });

                } else {
                    System.out.println("没有更新数据");
                }
            }
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }


    //@Reference(timeout = 50000)
    //private ItemPageService itemPageService;

    @RequestMapping("/genHtml")
    public void getHtml(Long goodsId) {
        //itemPageService.genItemHtml(goodsId);
    }
}
