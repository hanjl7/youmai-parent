package com.yougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbBrandMapper;
import com.youmai.pojo.TbBrand;
import com.youmai.pojo.TbBrandExample;
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
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @return void
     * @Description 添加品牌
     * @Date 22:08 2018/9/10
     * @Param [tbBrand]
     **/
    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    /**
     * @return void
     * @Description 修改品牌
     * @Date 17:54 2018/9/11
     * @Param [tbBrand]
     **/
    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * @return com.youmai.pojo.TbBrand
     * @Description 根据id查询品牌
     * @Date 17:58 2018/9/11
     * @Param [id]
     **/
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * @return void
     * @Description 批量删除
     * @Date 18:39 2018/9/11
     * @Param [ids]
     **/
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * @return
     * @Description 品牌分页 + 查询
     * @Date 18:52 2018/9/10
     * @Param [pageNum=当前页面, pageSize=每页记录数]
     **/
    @Override
    public PageResult findPage(TbBrand tbBrand, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if (tbBrand != null) {
            if (tbBrand.getName() != null && tbBrand.getName().length() > 0) {
                criteria.andNameLike("%" + tbBrand.getName() + "%");
            }
            if (tbBrand.getFirstChar() != null && tbBrand.getFirstChar().length() > 0) {
                criteria.andFirstCharLike("%" + tbBrand.getFirstChar() + "%");
            }
        }

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(page.getTotal(), page.getResult());
    }
}
