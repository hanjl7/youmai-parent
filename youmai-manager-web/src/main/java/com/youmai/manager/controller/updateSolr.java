package com.youmai.manager.controller;

import entity.Result;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.SolrUtil;

/**
 * @ClassName: updateSolr
 * @Description: 更新列表
 * @Author: 泊松
 * @Date: 2018/9/25 20:14
 * @Version: 1.0
 */
@RestController
@RequestMapping
public class updateSolr {

    @RequestMapping("/updateSolr")
    public Result updateSolr() {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
            SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
            solrUtil.importItemDate();
            return new Result(true, "更新列表成功");
        } catch (BeansException e) {
            e.printStackTrace();
            return new Result(false, "更新列表失败");
        }

    }
}
