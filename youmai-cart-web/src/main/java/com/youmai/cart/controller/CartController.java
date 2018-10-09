package com.youmai.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.youmai.cart.service.CartService;
import com.youmai.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CartController
 * @Description: 购物车
 * @Author: 泊松
 * @Date: 2018/10/9 18:40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 从 cookie 中读取 购物车列表
     * @Date 20:08 2018/10/9
     * @Param []
     **/
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.equals("")) {
            cartListString = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        return cartList_cookie;
    }

    /**
     * @return entity.Result
     * @Description 添加到商品到购物车列表
     * @Date 20:19 2018/10/9
     * @Param [itemId, num]
     **/
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        try {
            //从 cookie 中读取 购物车列表
            List<Cart> cartList = findCartList();
            //新购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            //将新的购物车列表存入cookie
            util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
            return new Result(true, "添加到商品到购物车列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加到商品到购物车列表失败");
        }
    }
}
