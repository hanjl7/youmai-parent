package com.youmai.content.service.impl;

import java.util.List;

import com.youmai.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbContentMapper;
import com.youmai.pojo.TbContent;
import com.youmai.pojo.TbContentExample;
import com.youmai.pojo.TbContentExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //添加成功后清除缓存redis
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //查询修改前的分类id
        Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        //根据以前的id清楚缓存
        redisTemplate.boundHashOps("content").delete(categoryId);
        contentMapper.updateByPrimaryKey(content);
        //如果分类id发生了修改，清除修改后的分类id
        if (categoryId.longValue() != content.getCategoryId().longValue()) {
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {

        for (Long id : ids) {
            //清除缓存
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
            redisTemplate.boundHashOps("content").delete(categoryId);
            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @return java.util.List<com.youmai.pojo.TbContent>
     * @Description 根据广告类型id查询列表
     * @Date 19:45 2018/9/22
     * @Param [categoryId]
     **/
    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {

        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        if (contentList == null) {
            //contentList为空，从数据库查询，添加到redis
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            //根据广告类型
            criteria.andCategoryIdEqualTo(categoryId);
            //根据状态为开启
            criteria.andStatusEqualTo("1");
            //排序
            example.setOrderByClause("sort_order");
            //获取广告列表
            contentList = contentMapper.selectByExample(example);
            //存入缓存
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
        }
        return contentList;
    }
}
