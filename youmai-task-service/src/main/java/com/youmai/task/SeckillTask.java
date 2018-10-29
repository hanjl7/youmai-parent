package com.youmai.task;

import com.youmai.mapper.TbSeckillGoodsMapper;
import com.youmai.pojo.TbSeckillGoods;
import com.youmai.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SeckillTask
 * @Description: 秒杀定时任务
 * @Author: 泊松
 * @Date: 2018/10/16 15:21
 * @Version: 1.0
 */
@Component
public class SeckillTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    /**
     * 刷新秒杀商品
     */
    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods() {
        System.out.println("执行了refreshSeckillGoods任务调度" + new Date());


        List ids = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());


        //缓存中没有添加进缓存
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        //库存大于0 大于
        criteria.andStockCountGreaterThan(0);
        //开始时间小于当前时间
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        //结束时间不能大于当前时间
        criteria.andEndTimeGreaterThan(new Date());
        //排除已添加的列表
        criteria.andIdNotIn(ids);
        List<TbSeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

        for (TbSeckillGoods tbSeckillGood : seckillGoods) {
            redisTemplate.boundHashOps("seckillGoods").put(tbSeckillGood.getId(), tbSeckillGood);
        }
        System.out.println("将秒杀列表" + seckillGoods.size() + "存入redis缓存");
    }

    @Scheduled(cron = "0 * * * * ?")
    public void removerSecKillTask() {
        System.out.println("移除秒杀任务开始");

        List<TbSeckillGoods> seckillGoods = redisTemplate.boundHashOps("seckillGoods").values();
        for (TbSeckillGoods seckillGood : seckillGoods) {
            if (seckillGood.getEndTime().getTime() < System.currentTimeMillis()) {
                seckillGoodsMapper.updateByPrimaryKey(seckillGood);
                redisTemplate.boundHashOps("seckillGoods").delete(seckillGood.getId());
                System.out.println("正在移除秒杀" + seckillGood.getId());
            }
        }
        System.out.println("移除秒杀任务结束");
    }
}


