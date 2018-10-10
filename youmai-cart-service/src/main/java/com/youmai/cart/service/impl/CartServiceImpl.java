package com.youmai.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youmai.cart.service.CartService;
import com.youmai.mapper.TbItemMapper;
import com.youmai.pojo.TbItem;
import com.youmai.pojo.TbOrderItem;
import com.youmai.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CartServiceImpl
 * @Description: 购物车服务
 * @Author: 泊松
 * @Date: 2018/10/9 18:40
 * @Version: 1.0
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 添加商品到购物车
     * @Date 18:46 2018/10/9
     * @Param [cartList, itemId, num]
     **/
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1 根据商品SKU itemId 查询 SKU 信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            throw new RuntimeException("没有该商品");
        }
        if (!item.getStatus().equals("1")) {
            throw new RuntimeException("商品状态无效");
        }
        //2 根据sku信息查询商家
        String sellerId = item.getSellerId();
        //3 根据商家id查询购物车列表里是否有该商家的商品购物车
        Cart cart = searchCartBySellerId(cartList, sellerId);
        if (cart == null) {
            //4 购物车列表里没有该商家的购物车
            System.out.println("购物车列表里没有该商家的购物车,新建购物车");
            //4.1 新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            TbOrderItem orderItem = createOrderItem(item, num);
            List<TbOrderItem> orderItemList = new ArrayList();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //4.2 将新建的购物车对象 添加到购物车列表
            cartList.add(cart);
        } else {
            //5 购物车列表有该商家的购物车
            //5.1.1 查询购物车列表里的该商家的购物车里是否有该商品
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (orderItem == null) {
                //5.1.2 查询购物车列表里的该商家的购物车里没有该商品，新增商品明细到购物车
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
                System.out.println("购物车列表有该商家的购物车,新增商品明细到购物车");
            } else {
                //5.1.3 查询购物车列表里的该商家的购物车里有该商品，添加该商品的数量和价格
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().longValue()));
                //如果 商品明细数量 小于 0 则移除
                if (orderItem.getNum() <= 0) {
                    //移除购物车明细
                    cart.getOrderItemList().remove(orderItem);
                }
                //如果 移除后 商家购物车 没有商品明细 则删除当前商家购物车
                if (cart.getOrderItemList().size() == 0) {
                    cartList.remove(cart);
                }
                System.out.println("购物车列表有该商家的购物车,添加该商品的数量和价格");
            }
        }
        return cartList;
    }

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 登录后合并redis和cookie里的购物车列表
     * @Date 11:07 2018/10/10
     * @Param [cartListRedis, cartListCookie]
     **/
    @Override
    public List<Cart> mergeCartList(List<Cart> cartListRedis, List<Cart> cartListCookie) {
        System.out.println("登录后合并redis和cookie里的购物车列表");
        for (Cart cart : cartListRedis) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartListRedis = addGoodsToCartList(cartListCookie, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartListRedis;
    }

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 从Redis找到购物车清单
     * @Date 10:24 2018/10/10
     * @Param [username]
     **/
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从Redis找到购物车清单" + username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null) {
            cartList = new ArrayList();
        }
        return cartList;
    }

    /**
     * @return java.util.List<com.youmai.pojogroup.Cart>
     * @Description 将购物车列表保存到Redis
     * @Date 10:24 2018/10/10
     * @Param [username, cartList]
     **/
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("将购物车列表保存到Redis" + username);
        redisTemplate.boundHashOps("cartList").put(username, cartList);
    }

    /**
     * @return com.youmai.pojo.TbOrderItem
     * @Description 判断购物车列表里商家购物车是否有同一类商品
     * @Date 19:38 2018/10/9
     * @Param [orderItemList, itemId]
     **/
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId) {
                return orderItem;
            }
        }
        //说明商家购物车里没有该商品
        return null;
    }

    /**
     * @return com.youmai.pojo.TbOrderItem
     * @Description 创建商家购物车明细列表
     * @Date 19:30 2018/10/9
     * @Param [item, num]
     **/
    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        if (num == 0) {
            throw new RuntimeException("数量非法");
        }
        //创建商家购物车明细列表
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));


        return orderItem;
    }

    /**
     * @return com.youmai.pojogroup.Cart
     * @Description 搜索购物车列表按卖家ID查询是否有该商家的购物车
     * @Date 19:09 2018/10/9
     * @Param [cartList, sellerId]
     **/
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        //说明购物车列表没有该商家的购物车
        return null;
    }
}
