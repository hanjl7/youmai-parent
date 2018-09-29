package com.youmai.page.service;

/**
 * @Description 商品详情页接口
 * @Date 20:53 2018/9/28
 * @Param  * @param null
 * @return
 **/
public interface ItemPageService {


    /**
     * @Description 生成商品详情页
     * @Date 20:54 2018/9/28
     * @Param [goodsId]
     * @return boolean
     **/
    public boolean genItemHtml(Long goodsId);
}