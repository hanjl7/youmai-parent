package com.youpin.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.pojo.TbBrand;
import com.youmai.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @return
     * @Description 返回分页列表
     * @Date 19:03 2018/9/10
     * @Param [pageNum, pageSize]
     **/
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer size) {
        PageResult tbBrands = brandService.findPage(page, size);
        return tbBrands;
    }

    /**
     * @return entity.Result
     * @Description 添加品牌
     * @Date 22:16 2018/9/10
     * @Param [tbBrand]
     **/
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            brandService.add(tbBrand);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * @return com.youmai.pojo.TbBrand
     * @Description 根据id查询品牌
     * @Date 18:00 2018/9/11
     * @Param [id]
     **/
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * @return entity.Result
     * @Description 修改
     * @Date 18:01 2018/9/11
     * @Param [tbBrand]
     **/
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * @Description 批量删除
     * @Date 18:39 2018/9/11
     * @Param [ids]
     * @return void
     **/
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    /**
     * @return
     * @Description 返回分页列表 + 查询
     * @Date 19:03 2018/9/10
     * @Param [pageNum, pageSize]
     **/
    @RequestMapping("/search")
    public PageResult searchPage(@RequestBody TbBrand tbBrand,Integer page, Integer size) {
        PageResult tbBrands = brandService.findPage(tbBrand,page, size);
        return tbBrands;
    }

}
