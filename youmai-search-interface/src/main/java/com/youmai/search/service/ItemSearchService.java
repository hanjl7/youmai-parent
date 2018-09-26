package com.youmai.search.service;

import java.util.Map;

public interface ItemSearchService {

    /**
     * @Description 搜索
     * @Date 21:41 2018/9/25
     * @Param [searchMap]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String,Object> search(Map searchMap);
}
