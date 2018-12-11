package com.test.processing;

import com.test.processing.models.MarketCap;
import com.test.processing.models.Result;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
public class MarketCapService {
    private KieContainer kieContainer;

    public MarketCapService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public Boolean isAlert(MarketCap marketCap) {
        Result result = new Result();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("r",result);
        kieSession.insert(marketCap);
        kieSession.fireAllRules();
        kieSession.dispose();
        return result.getIsAlert();
    }
}


