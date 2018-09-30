package com.youmai.page.service;

/**
 * @Description 商品详情页接口
 * @Date 20:53 2018/9/28
 * @Param * @param null
 * @return
 **/
public interface ItemPageService {


    /**
     * @return boolean
     * @Description 生成商品详情页
     * @Date 20:54 2018/9/28
     * @Param [goodsId]
     **/
    public boolean genItemHtml(Long goodsId);


    /**
     * @Description 删除详情页
     * @Date 22:41 2018/9/29
     * @Param [goodsId]
     * @return boolean
     **/
    public boolean deleteItemHtml(Long[] goodsId);
}