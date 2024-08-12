package es.daily.pattern.service;

import es.daily.pattern.model.StockWrapper;
import yahoofinance.YahooFinance;

import java.io.IOException;

public class StockService {
    public StockWrapper findStock(String symbol) throws IOException {
        System.out.println("Getting " + symbol);
        try {
            return new StockWrapper(YahooFinance.get(symbol));
        } catch (IOException e) {
            System.out.println("Error getting " + symbol);
            throw new IOException(e);
        }
    }
}