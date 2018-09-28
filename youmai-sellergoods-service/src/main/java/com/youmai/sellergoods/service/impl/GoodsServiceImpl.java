package com.youmai.sellergoods.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.youmai.mapper.*;
import com.youmai.pojo.*;
import com.youmai.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.pojo.TbGoodsExample.Criteria;
import com.youmai.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        //设置未申请状态
        goods.getGoods().setAuditStatus("0");
        goodsMapper.insert(goods.getGoods());
        //设置id
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        //插入商品扩展属性
        goodsDescMapper.insert(goods.getGoodsDesc());

        //添加sku
        saveItemList(goods);

    }

    //提取重复代码块
    private void setItemValues(TbItem item, Goods goods) {

        //商品分类
        item.setCategoryid(goods.getGoods().getCategory3Id());
        //创建日期
        item.setCreateTime(new Date());
        //更新日期
        item.setUpdateTime(new Date());
        //商品id
        item.setGoodsId(goods.getGoods().getId());
        //商家id
        item.setSellerId(goods.getGoods().getSellerId());

        //分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());

        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());

        //商家名称(店铺名称)
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getNickName());

        //图片名称
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList.size() > 0) {
            item.setImage((String) imageList.get(0).get("url"));
        }

    }


    //插入SKU列表数据
    private void saveItemList(Goods goods) {
        //是否启用规格
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {


            for (TbItem item : goods.getItemList()) {
                //标题
                //SPU名称
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> specMap = JSON.parseObject(item.getSpec());
                for (String key : specMap.keySet()) {
                    //规格选项名称
                    title += "" + specMap.get(key);
                }

                item.setTitle(title);

                setItemValues(item, goods);


                itemMapper.insert(item);
            }
        } else {
            //没有启用规格
            TbItem item = new TbItem();
            //标题
            item.setTitle(goods.getGoods().getGoodsName());
            //价格
            item.setPrice(goods.getGoods().getPrice());
            //库存
            item.setNum(9999);
            //状态
            item.setStatus("1");
            //是否默认
            item.setIsDefault("1");

            item.setSpec("{}");

            setItemValues(item, goods);

            itemMapper.insert(item);

        }
    }

    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {
        //设置未申请状态，如果是经过修改的商品,需要重新设定
        goods.getGoods().setAuditStatus("0");

        //更新扩展表基本数据
        goodsMapper.updateByPrimaryKey(goods.getGoods());
        //更新扩展表数据
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

        //删除原有的SKU列表数据
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);

        //插入新的SKU列表数据
        saveItemList(goods);

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {

        Goods goods = new Goods();
        //添加查询的商品
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        //添加商品详情
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        //查询SKU商品列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        goods.setItemList(tbItems);
        goods.setGoodsDesc(tbGoodsDesc);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //逻辑删除
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(tbGoods);
            //真实删除
            /*//删除商品表
            goodsMapper.deleteByPrimaryKey(id);
            //删除商品信息表
            goodsDescMapper.deleteByPrimaryKey(id);
            //删除SKU列表
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(id);
            itemMapper.deleteByExample(example);*/

        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();//非删除状态
        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                //模糊查询变成精确匹配
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @return void
     * @Description 批量修改状态
     * @Date 19:09 2018/9/21
     * @Param [ids, status]
     **/
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
     * @return void
     * @Description 商家修改上下架
     * @Date 21:39 2018/9/21
     * @Param [ids, status]
     **/
    @Override
    public void updateMarketable(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsMarketable(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    /**
     * @Description 按商品ID和状态查找商品列表
     * @Date 19:30 2018/9/28
     * @Param [goodsIds, status]
     * @return java.util.List<com.youmai.pojo.TbItem>
     **/
    @Override
    public List<TbItem> findItemListByGoodsIdAndStatus(Long[] goodsIds, String status) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdIn(Arrays.asList(goodsIds));
        criteria.andStatusEqualTo(status);

        return itemMapper.selectByExample(example);
    }
}
