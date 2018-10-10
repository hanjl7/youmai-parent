package com.youmai.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.youmai.cart.service.CartService;
import com.youmai.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Reference(timeout = 6000)
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录名查询" + username);

        //不管等不登陆都要查询cookie里的购物车
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.equals("")) {
            cartListString = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

        if (username.equals("anonymousUser")) {
            //说明没有登录
            return cartList_cookie;
        } else {
            //已登录,从redis中查询
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            if (cartList_cookie.size() > 0) {
                //如果本地存在cookie里有购物车列表，合并
                cartList_redis = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //清除cookie里的购物车
                util.CookieUtil.deleteCookie(request, response, "cartList");
                //将合并后的购物车列表存入redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }
            return cartList_redis;
        }

    }

    /**
     * @return entity.Result
     * @Description 添加到商品到购物车列表
     * @Date 20:19 2018/10/9
     * @Param [itemId, num]
     **/
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = "http://localhost:9105")
    public Result addGoodsToCartList(Long itemId, Integer num) {

        /*//springMVC的版本在4.2或以上版本，可以使用注解实现跨域
        //CORS跨域解决方案  它允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制  相当头信息
        //第一个可以访问的域，不需要cookie
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
        //第二个可以访问cookie，必须加上这句话
        response.setHeader("Access-Control-Allow-Credentials", "true");*/


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录名添加" + username);
        try {
            //从 cookie 中读取 购物车列表
            List<Cart> cartList = findCartList();
            //新购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if (username.equals("anonymousUser")) {
                //将新的购物车列表存入cookie
                util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                return new Result(true, "添加到商品到购物车列表成功");
            } else {
                //已登录，向redis中添加
                cartService.saveCartListToRedis(username, cartList);
                return new Result(true, "添加到商品到购物车列表成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加到商品到购物车列表失败");
        }
    }
}
