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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static es.daily.pattern.util.FechasUtils.calendarMinus200;
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
            StockService stockService = new StockService();
            StockWrapper stockWrapper = stockService.findStock(symbol);
            Stock stock = stockWrapper.getStock();

            Calendar from = Calendar.getInstance();
            from.add(Calendar.DAY_OF_MONTH, -4);

            Calendar to = Calendar.getInstance(); // default to now

            List<HistoricalQuote> historial = stock.getHistory(from, to, Interval.DAILY);

            boolean pricesAreLower = pricesAreLowers(historial);
            Calendar c;
            String date;

            StringBuilder sb = new StringBuilder();
            for (HistoricalQuote h : historial) {
                c = h.getDate();
                date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
                sb.append("Close price from: ");
                sb.append(date);
                sb.append(": ");
                sb.append(h.getClose().doubleValue());
                sb.append("\n");
            }

            historial = stock.getHistory(calendarMinus200(), to, Interval.DAILY);
            double ma = calculateMovingAverage(200, historial);

            boolean result = stock.getQuote().getPrice().doubleValue() > ma && pricesAreLower;

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
        return (check(historial.get(0).getClose(), Utils.Operator.GREATER_THAN, historial.get(1).getClose())
                && check(historial.get(1).getClose(), Utils.Operator.GREATER_THAN, historial.get(2).getClose()));
    }

    private static double calculateMovingAverage(int size, List<HistoricalQuote> historical) {
        MovingAverageByCircularBuffer ma = new MovingAverageByCircularBuffer(200);
        for (HistoricalQuote h : historical) {
            ma.add(h.getClose().doubleValue());
        }
        return ma.getMovingAverage();
    }


}