package com.youmai.cart.service;

import com.youmai.pojogroup.Cart;

import java.util.List;

/**
 * @ClassName: CartService
 * @Description: 购物车服务
 * @Author: 泊松
 * @Date: 2018/10/9 18:39
 * @Version: 1.0
 */
public interface CartService {

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 添加商品到购物车
     * @Date 18:46 2018/10/9
     * @Param [cartList, itemId, num]
     **/
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 从Redis中读取购物车列表
     * @Date 10:21 2018/10/10
     * @Param []
     **/
    public List<Cart> findCartListFromRedis(String username);

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 将购物车列表保存到Redis
     * @Date 10:22 2018/10/10
     * @Param []
     **/
    public void saveCartListToRedis(String username, List<Cart> cartList);

    /**
     * @Description 登录后合并redis和cookie里的购物车列表
     * @Date 11:06 2018/10/10
     * @Param [cartListRedis, cartListCookie]
     * @return java.util.List<com.youmai.pojogroup.Cart>
     **/
    public List<Cart> mergeCartList(List<Cart> cartListRedis,List<Cart> cartListCookie);
}
