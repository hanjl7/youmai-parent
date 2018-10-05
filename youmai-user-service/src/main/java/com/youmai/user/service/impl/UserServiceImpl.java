package com.youmai.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbUserMapper;
import com.youmai.pojo.TbUser;
import com.youmai.pojo.TbUserExample;
import com.youmai.pojo.TbUserExample.Criteria;
import com.youmai.user.service.UserService;

import entity.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination smsDestination;

    @Value("${template_code}")
    private String template_code;

    @Value("${sign_name}")
    private String sign_name;

    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbUser user) {
        //创建日期
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //注册来源
        user.setSourceType("1");
        //密码md5加密
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);

        userMapper.insert(user);
    }


    /**
     * @return void
     * @Description 生成短信验证码
     * @Date 19:22 2018/10/5
     * @Param [phone]
     **/
    @Override
    public void createSmsCode(String phone) {
        //6位验证码
        String code = (long) (Math.random() * 1000000) + "";
        System.out.println("验证码" + code);

        //存入redis缓存
        redisTemplate.boundHashOps("smsCode").put(phone, code);

        //将验证码发送到activeMq消息服务
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                //手机号
                message.setString("mobile", phone);
                System.out.println("手机号" + phone);
                //模板号
                message.setString("template_code", template_code);
                System.out.println("模板号" + template_code);
                //签名
                message.setString("sign_name", sign_name);
                System.out.println("签名" + sign_name);
                //验证码参数
                Map map = new HashMap();
                map.put("code",code);
                System.out.println("验证码参数" + map);
                message.setString("param", JSON.toJSONString(map));
                return message;
            }
        });
    }

    /**
     * @return boolean
     * @Description 判断验证码是否存在
     * @Date 19:41 2018/10/5
     * @Param [phone, code]
     **/
    @Override
    public boolean checkSmsCode(String phone, String code) {
        //得到缓存中的验证码
        String smsCode = (String) redisTemplate.boundHashOps("smsCode").get(phone);

        if (smsCode == null) {
            return false;
        }

        if (!smsCode.equals(code)) {
            return false;
        }
        return true;
    }

    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser findOne(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbUser user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + user.getPassword() + "%");
            }
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andPhoneLike("%" + user.getPhone() + "%");
            }
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andEmailLike("%" + user.getEmail() + "%");
            }
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
            }
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + user.getNickName() + "%");
            }
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andStatusLike("%" + user.getStatus() + "%");
            }
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
            }
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andQqLike("%" + user.getQq() + "%");
            }
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
            }
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
            }
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andSexLike("%" + user.getSex() + "%");
            }

        }

        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
