package com.yougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbBrandMapper;
import com.youmai.pojo.TbBrand;
import com.youmai.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Description 品牌接口
 * @Date 18:49 2018/9/10
 **/
@Service
@Transactional
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
     * @return
     * @Description 品牌分页
     * @Date 18:52 2018/9/10
     * @Param [pageNum=当前页面, pageSize=每页记录数]
     **/
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        System.out.println(page);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * @Description 添加品牌
     * @Date 22:08 2018/9/10
     * @Param [tbBrand]
     * @return void
     **/
    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    /**
     * @Description 修改品牌
     * @Date 17:54 2018/9/11
     * @Param [tbBrand]
     * @return void
     **/
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * @Description 根据id查询品牌
     * @Date 17:58 2018/9/11
     * @Param [id]
     * @return com.youmai.pojo.TbBrand
     **/
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
