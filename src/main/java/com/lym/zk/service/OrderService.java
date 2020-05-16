package com.lym.zk.service;

public interface OrderService {

    void updateStockById (int quantityCount,int id);

    int queryStockById(int id);
}
