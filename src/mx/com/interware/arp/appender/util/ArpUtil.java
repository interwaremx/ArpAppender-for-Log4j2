package mx.com.interware.arp.appender.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jorge Esteban Zaragoza Salazar
 * @version 1.0.0 - Clase que funciona como appender para Log4j
 *
 */
public class ArpUtil {

    /**
     * Método que comprueba si una cadena de texto cumple con una expresión
     * regular
     *
     * @param strEval Cadena de texto a evaluar
     * @param regexp Expresión regular para evaluar
     * @return Devuelve un boleano que indica si la cadena cumple con la
     * expresión regular
     */
    public static Boolean matches(String strEval, String regexp) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(strEval);
        return m.matches();
    }
}
