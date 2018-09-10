package com.yougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbBrandMapper;
import com.youmai.pojo.TbBrand;
import com.youmai.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Description 品牌接口
 * @Date 18:49 2018/9/10
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    /**
     * @return java.util.List<com.youmai.pojo.TbBrand>
     * @Description 返回TbBrand
     * @Date 22:08 2018/9/9
     * @Param []
     **/
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * @return entity.PageResult
     * @Description 品牌分页
     * @Date 18:52 2018/9/10
     * @Param [pageNum=当前页面, pageSize=每页记录数]
     **/
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(page.getTotal(),page.getResult());
    }

}
