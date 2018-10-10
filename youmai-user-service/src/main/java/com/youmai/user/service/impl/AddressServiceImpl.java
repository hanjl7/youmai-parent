package com.youmai.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbAddressMapper;
import com.youmai.pojo.TbAddress;
import com.youmai.pojo.TbAddressExample;
import com.youmai.pojo.TbAddressExample.Criteria;
import com.youmai.user.service.AddressService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class AddressServiceImpl implements AddressService {


    @Autowired
    private TbAddressMapper addressMapper;


    /**
     * @return java.util.List<com.youmai.pojo.TbAddress>
     * @Description 按用户ID查找地址列表
     * @Date 17:01 2018/10/10
     * @Param [userId]
     **/
    @Override
    public List<TbAddress> findAddressListByUserId(String userId) {
        TbAddressExample example = new TbAddressExample();
        Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<TbAddress> addressList = addressMapper.selectByExample(example);
        return addressList;
    }

    /**
     * 查询全部
     */
    @Override
    public List<TbAddress> findAll() {
        return addressMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbAddress> page = (Page<TbAddress>) addressMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbAddress address) {
        if (address.getIsDefault().equals("是")) {
            address.setIsDefault("1");
        } else {
            address.setIsDefault("0");
        }
        addressMapper.insert(address);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbAddress address) {
        addressMapper.updateByPrimaryKey(address);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbAddress findOne(Long id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            addressMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbAddress address, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbAddressExample example = new TbAddressExample();
        Criteria criteria = example.createCriteria();

        if (address != null) {
            if (address.getUserId() != null && address.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + address.getUserId() + "%");
            }
            if (address.getProvinceId() != null && address.getProvinceId().length() > 0) {
                criteria.andProvinceIdLike("%" + address.getProvinceId() + "%");
            }
            if (address.getCityId() != null && address.getCityId().length() > 0) {
                criteria.andCityIdLike("%" + address.getCityId() + "%");
            }
            if (address.getTownId() != null && address.getTownId().length() > 0) {
                criteria.andTownIdLike("%" + address.getTownId() + "%");
            }
            if (address.getMobile() != null && address.getMobile().length() > 0) {
                criteria.andMobileLike("%" + address.getMobile() + "%");
            }
            if (address.getAddress() != null && address.getAddress().length() > 0) {
                criteria.andAddressLike("%" + address.getAddress() + "%");
            }
            if (address.getContact() != null && address.getContact().length() > 0) {
                criteria.andContactLike("%" + address.getContact() + "%");
            }
            if (address.getIsDefault() != null && address.getIsDefault().length() > 0) {
                criteria.andIsDefaultLike("%" + address.getIsDefault() + "%");
            }
            if (address.getNotes() != null && address.getNotes().length() > 0) {
                criteria.andNotesLike("%" + address.getNotes() + "%");
            }
            if (address.getAlias() != null && address.getAlias().length() > 0) {
                criteria.andAliasLike("%" + address.getAlias() + "%");
            }

        }

        Page<TbAddress> page = (Page<TbAddress>) addressMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
