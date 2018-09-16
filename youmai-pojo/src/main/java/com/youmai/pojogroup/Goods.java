package com.youmai.pojogroup;

import com.youmai.pojo.TbGoods;
import com.youmai.pojo.TbGoodsDesc;
import com.youmai.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: Goods
 * @Description: 组合实体类
 * @Author: 泊松
 * @Date: 2018/9/16 10:53
 * @Version: 1.0
 */
public class Goods implements Serializable {
    //商品spu
    private TbGoods goods;
    //商品扩展
    private TbGoodsDesc goodsDesc;
    //商品sku列表
    private List<TbItem> itemList;



    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
