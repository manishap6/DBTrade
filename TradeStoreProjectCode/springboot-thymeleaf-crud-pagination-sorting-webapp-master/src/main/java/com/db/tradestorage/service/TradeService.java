package com.db.tradestorage.service;

import com.db.tradestorage.dao.TradeDao;
import com.db.tradestorage.dao.TradeRepository;
import com.db.tradestorage.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    TradeDao tradeDao;

    @Autowired
    TradeRepository tradeRepository;

    public int isValid(Trade trade){
        if(validateMaturityDate(trade)) {
            // Trade exsitingTrade = tradeDao.findTrade(trade.getTradeId());
            Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
             if (exsitingTrade.isPresent()) {
                 return validateVersion(trade, exsitingTrade.get());
             }else{
                 return 2;
             }
         }else {
        	 return 1;
         }
         
    }

    private int validateVersion(Trade trade,Trade oldTrade) {
        //validation 1  During transmission if the
        // lower version is being received by the store it will reject the trade and throw an exception.
        if(trade.getVersion() >= oldTrade.getVersion()){
            return 0;
        }
        return 3;
    }

    //2.	Store should not allow the trade which has less maturity date then today date
    private boolean validateMaturityDate(Trade trade){
        return trade.getMaturityDate().isBefore(LocalDate.now())  ? false:true;
    }

    public Trade  persist(Trade trade){
       // tradeDao.save(trade);
        trade.setCreatedDate(LocalDate.now());
        tradeRepository.save(trade);
        return trade;
    }

    public List<Trade> findAll(){
       return  tradeRepository.findAll();
        //return tradeDao.findAll();
    }

    public void updateExpiryFlagOfTrade(){
      /* tradeDao.tradeMap.forEach(
               (k,v) -> {
                   if(!validateMaturityDate(v)){
                       v.setExpiredFlag("N");
                       log.info("Trade which needs to updated {}",v);
                   }
               }
       );*/
        tradeRepository.findAll().stream().forEach(t -> {
                if (!validateMaturityDate(t)) {
                    t.setExpiredFlag("Y");
                    log.info("Trade which needs to updated {}", t);
                    tradeRepository.save(t);
                }
            });
        }


}
