package com.youmai.pojogroup;

import com.youmai.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: Cart
 * @Description: 购物车实体类
 * @Author: 泊松
 * @Date: 2018/10/8 22:32
 * @Version: 1.0
 */
public class Cart implements Serializable {
    //商家id
    private String sellerId;
    //商家名称
    private String sellerName;
    //购物车明细
    private List<TbOrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
