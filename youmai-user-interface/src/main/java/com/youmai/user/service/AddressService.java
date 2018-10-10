package com.youmai.user.service;
import java.util.List;
import com.youmai.pojo.TbAddress;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface AddressService {

    /**
     * @Description 按用户ID查找地址列表
     * @Date 17:01 2018/10/10
     * @Param [userId]
     * @return java.util.List<com.youmai.pojo.TbAddress>
     **/
    public List<TbAddress> findAddressListByUserId(String userId);

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAddress> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbAddress address);
	
	
	/**
	 * 修改
	 */
	public void update(TbAddress address);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAddress findOne(Long id);
	
	
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
	public PageResult findPage(TbAddress address, int pageNum, int pageSize);
	
}
