package com.youmai.user.service;
import java.util.List;
import com.youmai.pojo.TbUser;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
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
	public PageResult findPage(TbUser user, int pageNum, int pageSize);


	/**
	 * @Description 生成短信验证码
	 * @Date 19:21 2018/10/5
	 * @Param [phone]
	 * @return void
	 **/
	public void createSmsCode(String phone);

	/**
	 * @Description 判断验证码是否存在
	 * @Date 19:40 2018/10/5
	 * @Param [phone, code]
	 * @return boolean
	 **/
	public boolean checkSmsCode(String phone,String code);


}
