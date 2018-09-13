package com.youmai.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbSpecificationMapper;
import com.youmai.mapper.TbSpecificationOptionMapper;
import com.youmai.pojo.TbSpecification;
import com.youmai.pojo.TbSpecificationOption;
import com.youmai.pojogroup.Specification;
import com.youmai.sellergoods.service.SpecificationService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        PageHelper.startPage(page,size);

        Page<TbSpecification> pages = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);

        return new PageResult(pages.getTotal(),pages.getResult());
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
}
