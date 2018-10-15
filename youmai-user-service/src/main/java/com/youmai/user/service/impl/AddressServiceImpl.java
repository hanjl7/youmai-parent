package com.youmai.user.service.impl;

import java.util.List;

import com.youmai.mapper.TbAreasMapper;
import com.youmai.mapper.TbCitiesMapper;
import com.youmai.mapper.TbProvincesMapper;
import com.youmai.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youmai.mapper.TbAddressMapper;
import com.youmai.pojo.TbAddressExample.Criteria;
import com.youmai.user.service.AddressService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class AddressServiceImpl implements AddressService {


    @Autowired
    private TbAddressMapper addressMapper;

    @Autowired
    private TbProvincesMapper provincesMapper;

    @Autowired
    private TbCitiesMapper citiesMapper;

    @Autowired
    private TbAreasMapper areasMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @return java.util.List<com.youmai.pojo.TbProvinces>
     * @Description 找省份
     * @Date 19:08 2018/10/15
     * @Param []
     **/
    @Override
    public List<TbProvinces> findProvinces() {
        List<TbProvinces> provinces = (List<TbProvinces>) redisTemplate.boundHashOps("address").get("provinces");
        if (provinces == null || provinces.size() == 0) {
            provinces = provincesMapper.selectByExample(null);
            System.out.println("province存入缓存");
            redisTemplate.boundHashOps("address").put("provinces", provinces);
        } else {
            System.out.println("从缓存中查询Provinces");
        }
        return provinces;
    }

    /**
     * @return java.util.List<com.youmai.pojo.TbCities>
     * @Description 根据省份找区域
     * @Date 20:04 2018/10/15
     * @Param []
     **/
    @Override
    public List<TbCities> findCities(String provinceId) {
        List<TbCities> cities = (List<TbCities>) redisTemplate.boundHashOps("address").get(provinceId);
        if (cities == null || cities.size() == 0) {
            TbCitiesExample example = new TbCitiesExample();
            TbCitiesExample.Criteria criteria = example.createCriteria();
            criteria.andProvinceidEqualTo(provinceId);
            cities = citiesMapper.selectByExample(example);
            redisTemplate.boundHashOps("address").put(provinceId, cities);
            System.out.println("cities 存入缓存");
        }
        return cities;
    }

    /**
     * @return java.util.List<com.youmai.pojo.TbAreas>
     * @Description 根据城市找区域
     * @Date 20:05 2018/10/15
     * @Param []
     **/
    @Override
    public List<TbAreas> findAreas(String citiesId) {
        List<TbAreas> areas = (List<TbAreas>) redisTemplate.boundHashOps("address").get(citiesId);
        if (areas == null || areas.size() == 0) {
            TbAreasExample example = new TbAreasExample();
            TbAreasExample.Criteria criteria = example.createCriteria();
            criteria.andCityidEqualTo(citiesId);
            areas = areasMapper.selectByExample(example);
            redisTemplate.boundHashOps("address").put(citiesId, areas);
            System.out.println("areas 存储到缓存");
        }
        return areas;
    }

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
        isDefaultAddress(address);
        addressMapper.insert(address);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbAddress address) {
        isDefaultAddress(address);
        addressMapper.updateByPrimaryKey(address);
    }

    /**
     * @return void
     * @Description 判断是否是默认地址，如果是更新全部为不是默认值
     * @Date 16:29 2018/10/11
     * @Param [address]
     **/
    private void isDefaultAddress(TbAddress address) {
        if (address.getIsDefault().equals("1")) {
            String userId = address.getUserId();
            TbAddressExample example = new TbAddressExample();
            Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(userId);
            criteria.andIsDefaultEqualTo("1");
            List<TbAddress> addressList = addressMapper.selectByExample(example);

            for (TbAddress tbAddress : addressList) {
                tbAddress.setIsDefault("0");
                addressMapper.updateByPrimaryKey(tbAddress);
            }
        }
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
