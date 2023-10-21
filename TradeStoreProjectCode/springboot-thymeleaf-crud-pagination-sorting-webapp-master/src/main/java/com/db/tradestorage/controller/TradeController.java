package com.db.tradestorage.controller;

import com.db.tradestorage.exception.InvalidTradeException;
import com.db.tradestorage.model.Trade;
import com.db.tradestorage.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {
    @Autowired
    TradeService tradeService;
    /*


     */
    @PostMapping("/trade")
    public ResponseEntity<String> tradeValidateStore(@RequestBody Trade trade){
    	int result = tradeService.isValid(trade);
       if(result == 0) {
    	   
           tradeService.persist(trade);
       }else{
         
    	   switch(result) {
    	   case 1:  return new ResponseEntity<>("Trade Maturity date is not valid", HttpStatus.NOT_ACCEPTABLE);
    	   case 2:  return new ResponseEntity<>("Trade is not available in the store ", HttpStatus.NOT_ACCEPTABLE);
    	   case 3:  return new ResponseEntity<>("Trade Version is lesser than old one", HttpStatus.NOT_ACCEPTABLE);
    	   }
    		   
         
       }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trade")
    public List<Trade> findAllTrades(){
        return tradeService.findAll();
    }
}
