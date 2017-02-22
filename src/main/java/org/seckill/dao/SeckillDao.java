package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangcheng on 2016/6/9.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);

    /**
     * 查询秒杀库存
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀库存
     * @param offer
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset")int offer,@Param("limit")int limit);

    /**
     * 使用存储过程
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);

}
