package com.youpin.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.pojo.TbBrand;
import com.youmai.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * @Description 返回分页列表
     * @Date 19:03 2018/9/10
     * @Param [pageNum, pageSize]
     * @return entity.PageResult
     **/
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int size) {
        System.out.println(page);
        System.out.println(size);
        return brandService.findPage(page, size);
    }
}
