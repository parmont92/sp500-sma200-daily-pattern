package es.daily.pattern.util;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author AndresParMont
 */
public class FechasUtils {

    public static boolean esDiaLaboral(Calendar calendar, Set<String> diasFestivos) {
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

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

    public static Set<String> obtenerDiasFestivos(int year) {
        Set<String> diasFestivos = new HashSet<>();

        diasFestivos.add(year + "-1-1");   // Año Nuevo
        diasFestivos.add(year + "-7-4");   // Día de la Independencia
        diasFestivos.add(year + "-12-25"); // Navidad
        diasFestivos.add(year + "-6-19"); // Día de la liberarción
        diasFestivos.add(year + "-11-11"); // Día de los veteranos

        diasFestivos.add(calcularMemorialDay(year));
        diasFestivos.add(calcularDiaMLK(year));
        diasFestivos.add(calcularDiaPresidentes(year));
        diasFestivos.add(calcularDiaTrabajo(year));
        diasFestivos.add(calcularDiaRaza(year));
        diasFestivos.add(calcularThanksGiving(year));

        // TODO: ¿Hay más festivos?

        return diasFestivos;
    }

    /**
     * Calcula el día de thanks giving, cuarto jueves de noviembre
     *
     * @param year
     * @return
     */
    public static String calcularThanksGiving(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.NOVEMBER, 31);

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        //System.out.println("El Día de acción de gracias " + year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        return year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula el día de la raza, segundo lunes de octubre
     *
     * @param year
     * @return
     */
    public static String calcularDiaRaza(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.OCTOBER, 1);

        // Encontrar el primer lunes de octubre
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Avanzar una semana para llegar al segundo lunes
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Mostrar la fecha
        //System.out.println("El Día de la raza es " + year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        return year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula el día de los presidentes, tercer lunes de febrero
     *
     * @param year
     * @return
     */
    public static String calcularDiaPresidentes(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.FEBRUARY, 1);

        // Encontrar el primer lunes de febrero
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Avanzar dos semanas para llegar al tercer lunes
        calendar.add(Calendar.DAY_OF_MONTH, 14);

        // Mostrar la fecha
        //System.out.println("El Día del Presidente es " + year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        return year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula el día del trabajo, primer lunes de septiembre
     *
     * @param year
     * @return
     */
    public static String calcularDiaTrabajo(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.SEPTEMBER, 1);

        // Encontrar el primer lunes de septiembre
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Mostrar la fecha
        //System.out.println("El Día del Trabajo es " + year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        return year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula el día de Martin Luther King, tercer lunes de enero
     *
     * @param year
     * @return
     */
    public static String calcularDiaMLK(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1);

        // Encontrar el primer lunes de enero
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Avanzar dos semanas para llegar al tercer lunes
        calendar.add(Calendar.DAY_OF_MONTH, 14);

        //System.out.println("El Día de Martin Luther King es " + year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        return year + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calcula el día de los caídos, último lunes de mayo
     *
     * @param year
     * @return
     */
    public static String calcularMemorialDay(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.MAY, 31);  // Comienza con el 31 de mayo
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        return year + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Calendar calendarMinus200() {
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
