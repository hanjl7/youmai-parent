package com.youmai.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youmai.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: ItemSearchController
 * @Description:
 * @Author: 泊松
 * @Date: 2018/9/25 22:31
 * @Version: 1.0
 */
@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map searchMap){
        return itemSearchService.search(searchMap);
    }
}
