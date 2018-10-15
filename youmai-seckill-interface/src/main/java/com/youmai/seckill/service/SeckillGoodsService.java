package com.youmai.seckill.service;
import java.util.List;
import com.youmai.pojo.TbSeckillGoods;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SeckillGoodsService {


    /**
     * @Description 从redis中查询实体
     * @Date 16:47 2018/10/15
     * @Param [id]
     * @return com.youmai.pojo.TbSeckillGoods
     **/
    public TbSeckillGoods findOneFromRedis(Long id);
    /**
     * @Description 返回正在秒杀的商品
     * @Date 21:21 2018/10/14
     * @Param []
     * @return java.util.List<com.youmai.pojo.TbSeckillGoods>
     **/
    public List<TbSeckillGoods> findList();
	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeckillGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeckillGoods seckillGoods);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeckillGoods seckillGoods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeckillGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSeckillGoods seckillGoods, int pageNum, int pageSize);
	
}
