package ivanio1.exchangebot.scheduler;

import ivanio1.exchangebot.service.ExchangeRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvalidationScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(InvalidationScheduler.class);

    @Autowired
    private ExchangeRatesService service;

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        LOG.info("invalidateCache task: start");
        service.clearUSDCache();
        service.clearEURCache();
        LOG.info("invalidateCache task: end");
    }
}