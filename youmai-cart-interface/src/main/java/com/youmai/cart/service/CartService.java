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
     * @Description 添加商品到购物车
     * @Date 18:46 2018/10/9
     * @Param [cartList, itemId, num]
     * @return java.util.List<com.youmai.pojogroup.Cart>
     **/
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);
}
