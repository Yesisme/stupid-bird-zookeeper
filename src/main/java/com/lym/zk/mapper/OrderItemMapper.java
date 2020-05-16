package com.lym.zk.mapper;

import com.lym.zk.bean.OrderItemPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

public interface OrderItemMapper extends BaseMapper<OrderItemPO>{

    void updateById (@Param("quantityCount") int quantityCount,@Param("id") int id);

    int findQuantityById(@Param("id") int id);
}
