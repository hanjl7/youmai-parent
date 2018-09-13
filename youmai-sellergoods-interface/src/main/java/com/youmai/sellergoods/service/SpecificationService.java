package com.youmai.sellergoods.service;


import com.youmai.pojogroup.Specification;
import entity.PageResult;



/**
 * @Description 规格接口
 * @Date 16:21 2018/9/13
 * @Param  * @param null
 * @Author xixi
 * @return
 **/
public interface SpecificationService {

    PageResult findAll(Integer page,Integer size);

    void add(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);

    void delete(Long[] ids);
}
