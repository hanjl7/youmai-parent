package com.youmai.order.service;
import java.util.List;
import com.youmai.pojo.TbOrder;

import com.youmai.pojo.TbPayLog;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
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
	public PageResult findPage(TbOrder order, int pageNum, int pageSize);


	/**
	 * @Description 从Redis搜索支付日志
	 * @Date 21:55 2018/10/12
	 * @Param [userId]
	 * @return com.youmai.pojo.TbPayLog
	 **/
	public TbPayLog searchPayLogFromRedis(String userId);


	public void updateOrderStatus(String out_trade_no, String transaction_id);
}
