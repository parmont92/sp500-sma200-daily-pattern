package es.daily.pattern;

import es.daily.pattern.model.StockWrapper;
import es.daily.pattern.service.StockService;
import es.daily.pattern.technical.MovingAverageByCircularBuffer;
import es.daily.pattern.util.Utils;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static es.daily.pattern.util.Utils.check;

/**
 * @author AndresParMont
 */
public class Main {
    private static final String defaultSymbol = "^SPX";

    public static void main(String[] args) {
        String symbol;

        if (args == null || args.length == 0) {
            symbol = defaultSymbol;
        } else {
            symbol = args[0];
        }

        try {
            StockWrapper stockWrapper = new StockService().findStock(symbol);
            Stock stock = stockWrapper.getStock();

            Calendar from = Calendar.getInstance();
            from.add(Calendar.DAY_OF_MONTH, -5);

            List<HistoricalQuote> historial = stock.getHistory(from, Calendar.getInstance(), Interval.DAILY);

            boolean pricesAreLower = pricesAreLowers(historial.subList(historial.size() - 3, historial.size()));
            /*
            Calendar c;
            String date;

            for (HistoricalQuote h : historial) {
                c = h.getDate();
                date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
                sb.append("Close price from: ");
                sb.append(date);
                sb.append(": ");
                sb.append(h.getClose().doubleValue());
                sb.append("\n");
            }

            //historial = stock.getHistory(calendarMinus200(), to, Interval.DAILY);
            // double ma = calculateMovingAverage(200, historial);
            */

            double ma = stock.getQuote().getPriceAvg200().doubleValue();

            boolean result = stock.getQuote().getPrice().doubleValue() > ma && pricesAreLower;

            StringBuilder sb = new StringBuilder(5);
            sb.append("200 Simple Moving Average: ");
            sb.append(ma);
            sb.append("\n");
            sb.append("Has pattern: ");
            sb.append(result);

            System.out.println(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean pricesAreLowers(List<HistoricalQuote> historial) {
        System.out.println("Historical size: " + historial.size());
        for (HistoricalQuote h : historial) {
            System.out.println("ClosePrice: " + h.getClose().doubleValue());
        }
        boolean hasPattern = true;
        for (int i = 1; i < historial.size(); i++) {
            if (check(historial.get(i - 1).getClose(), Utils.Operator.GREATER_THAN, historial.get(i).getClose())) {
                hasPattern = true;
            } else {
                hasPattern = false;
            }
        }
        return hasPattern;
    }

    private static double calculateMovingAverage(int size, List<HistoricalQuote> historical) {
        MovingAverageByCircularBuffer ma = new MovingAverageByCircularBuffer(200);
        for (HistoricalQuote h : historical) {
            ma.add(h.getClose().doubleValue());
        }
        return ma.getMovingAverage();
    }


}