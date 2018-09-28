package com.youmai.sellergoods.service;

import java.util.List;

import com.youmai.pojo.TbGoods;

import com.youmai.pojo.TbItem;
import com.youmai.pojogroup.Goods;
import entity.PageResult;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Goods goods);


    /**
     * 修改
     */
    public void update(Goods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public Goods findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * @return
     * @Description 批量修改状态
     * @Date 19:07 2018/9/21
     * @Param * @param null
     **/
    public void updateStatus(Long[] ids, String status);


    public void updateMarketable(Long[] ids, String status);


    public List<TbItem> findItemListByGoodsIdAndStatus(Long[] goodsIds, String status);


}
