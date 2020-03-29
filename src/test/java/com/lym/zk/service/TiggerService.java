package com.lym.zk.service;

import org.springframework.stereotype.Service;

@Service
public class TiggerService {

    private PigService pigService;

   /* public TiggerService (PigService pigService){
        this.pigService = pigService;
    }

    public PigService getPigService() {
        return pigService;
    }

    public void setPigService(PigService pigService) {
        this.pigService = pigService;
    }*/

    public TiggerService(PigService pigService) {
        this.pigService = pigService;
    }
}
