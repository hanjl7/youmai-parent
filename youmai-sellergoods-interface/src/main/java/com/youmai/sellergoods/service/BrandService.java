package com.youmai.sellergoods.service;

import com.youmai.pojo.TbBrand;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {

    /**
     * 查询所有品牌
     * @return
     */
    public List<TbBrand> findAll();
}
