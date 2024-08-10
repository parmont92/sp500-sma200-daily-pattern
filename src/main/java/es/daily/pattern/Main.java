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
            from.add(Calendar.DAY_OF_MONTH, -3);

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

            System.out.println(sb);
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

    private static boolean esDiaLaboral(Calendar calendar, Set<String> diasFestivos) {
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        // Verificar si es sábado o domingo
        if (diaSemana == Calendar.SATURDAY || diaSemana == Calendar.SUNDAY) {
            return false;
        }

        // Verificar si es un día festivo
        String fecha = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        if (diasFestivos.contains(fecha)) {
            return false;
        }

        return true;
    }

    // Método para obtener los días festivos de EE.UU.
    private static Set<String> obtenerDiasFestivos(int year) {
        Set<String> diasFestivos = new HashSet<>();

        diasFestivos.add(year + "-1-1");   // Año Nuevo
        diasFestivos.add(year + "-7-4");   // Día de la Independencia
        diasFestivos.add(year + "-12-25"); // Navidad

        diasFestivos.add(calcularMemorialDay(year)); // Día de los Caídos (último lunes de mayo)

        // TODO: add more holidays

        return diasFestivos;
    }

    private static String calcularMemorialDay(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.MAY, 31);  // Comienza con el 31 de mayo
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        return year + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar calendarMinus200() {
        Calendar calendar = Calendar.getInstance();
        Set<String> diasFestivos = obtenerDiasFestivos(calendar.get(Calendar.YEAR));

        int diasARestar = 200;
        while (diasARestar > 0) {
            // Restar un día
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            if (esDiaLaboral(calendar, diasFestivos)) {
                diasARestar--;
            }
        }

        return calendar;
    }
}