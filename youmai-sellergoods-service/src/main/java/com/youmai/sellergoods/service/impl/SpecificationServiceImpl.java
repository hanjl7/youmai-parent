package com.youmai.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbSpecificationMapper;
import com.youmai.mapper.TbSpecificationOptionMapper;
import com.youmai.pojo.TbSpecification;
import com.youmai.pojo.TbSpecificationOption;
import com.youmai.pojo.TbSpecificationOptionExample;
import com.youmai.pojogroup.Specification;
import com.youmai.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SpecificationServiceImpl
 * @Description:
 * @Author: 泊松
 * @Date: 2018/9/12 20:16
 * @Version: 1.0
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    @Override
    public PageResult findAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);

        Page<TbSpecification> pages = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);

        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public void add(Specification specification) {

        tbSpecificationMapper.insert(specification.getSpecification());

        //获取规格选项
        List<TbSpecificationOption> tbSpecificationOptions = specification.getSpecificationOptionList();
        for (TbSpecificationOption option : tbSpecificationOptions) {
            option.setSpecId(specification.getSpecification().getId());
            tbSpecificationOptionMapper.insert(option);
        }


    }

    @Override
    public Specification findOne(Long id) {
        //查询规格
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);

        //查询规格列表
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        //根据id查询
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptions = tbSpecificationOptionMapper.selectByExample(example);

        //添加进specification
        Specification specification = new Specification();
        specification.setSpecification(tbSpecification);
        specification.setSpecificationOptionList(tbSpecificationOptions);

        return specification;
    }

    @Override
    public void update(Specification specification) {
        //添加规格
        tbSpecificationMapper.updateByPrimaryKey(specification.getSpecification());

        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(specification.getSpecification().getId());

        //根据id删除 然后在增加
        tbSpecificationOptionMapper.deleteByExample(example);

        //添加规格列表
        List<TbSpecificationOption> tbSpecificationOptions = specification.getSpecificationOptionList();
        for (TbSpecificationOption option : tbSpecificationOptions) {
            option.setSpecId(specification.getSpecification().getId());
            tbSpecificationOptionMapper.insert(option);
        }
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //先删除规格
            tbSpecificationMapper.deleteByPrimaryKey(id);
            //在删除规格列表
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            //以id为条件
            criteria.andSpecIdEqualTo(id);
            tbSpecificationOptionMapper.deleteByExample(example);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return tbSpecificationMapper.selectOptionList();
    }
}
