package com.youmai.sellergoods.service;

import com.youmai.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {

    /**
     * 查询所有品牌
     *
     * @return
     */
    public List<TbBrand> findAll();



    public PageResult findPage(Integer pageNum, Integer pageSize);

    public PageResult findPage(TbBrand tbBrand,Integer pageNum, Integer pageSize);

    public void add(TbBrand tbBrand);

    public TbBrand findOne(Long id);

    public void update(TbBrand tbBrand);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
