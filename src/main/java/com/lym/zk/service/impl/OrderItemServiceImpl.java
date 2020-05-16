package com.lym.zk.service.impl;

import com.lym.zk.mapper.OrderItemMapper;
import com.lym.zk.service.OrderService;
import com.lym.zk.utils.ZkLockUtil;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderService {

    @Autowired
    private OrderItemMapper orderItemMapper;


    @Override
    public void updateStockById(int quantityCount, int id) {
        orderItemMapper.updateById(quantityCount,id);
    }

    @Override
    public int queryStockById(int id) {
        int count = orderItemMapper.findQuantityById(id);

        return count;
    }
}
