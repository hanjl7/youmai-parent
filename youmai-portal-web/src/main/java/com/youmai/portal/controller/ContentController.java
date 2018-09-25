package com.youmai.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.content.service.ContentService;
import com.youmai.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ContentController
 * @Description: 广告
 * @Author: 泊松
 * @Date: 2018/9/22 19:56
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId) {
        return contentService.findByCategoryId(categoryId);
    }
}
