package com.lym.zk.service;

import org.springframework.stereotype.Service;

@Service
public class PigService {

    private TiggerService tiggerService;

    /*public TiggerService getTiggerService() {
        return tiggerService;
    }

    public void setTiggerService(TiggerService tiggerService) {
        this.tiggerService = tiggerService;
    }*/

    public PigService(TiggerService tiggerService) {
        this.tiggerService = tiggerService;
    }
}
