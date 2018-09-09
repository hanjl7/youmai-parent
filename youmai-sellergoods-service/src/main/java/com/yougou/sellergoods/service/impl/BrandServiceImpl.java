package com.yougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youmai.mapper.TbBrandMapper;
import com.youmai.pojo.TbBrand;
import com.youmai.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 品牌的服务类
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }
}
