package mx.com.interware.arp.appender.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jorge Esteban Zaragoza Salazar
 * @version 1.0.0 - Clase que proveé utilierías para el manejo de EDN (Extensible
 * Data Notation)
 * 
 */
public class EdnUtil {

    /**
     * Método que permite crear EDN un EDN a partir de un mapa, una expresión
     * regular y un formato
     *
     * @param mapa Mapa con la información a generar en EDN
     * @param regexp Expresion regular a evaluar para cada mensaje del mapa
     * @param ednFormat Llaves para el formato de edn
     * @return Devuelve la cadena del EDN generado
     **/
    public static String createEDN(List<Map> mapa, String regexp, String ednFormat) {
        Pattern p = Pattern.compile(regexp);
        StringBuilder builder = new StringBuilder();
        Matcher m;
        builder.append("[");
        for (Map<String, String> linea : mapa) {
            List<String> allMatches = new ArrayList<>();
            m = p.matcher(linea.get("message"));
            if (m.find()) {
                for (Integer i = 1; i <= m.groupCount(); i++) {
                    allMatches.add(m.group(i));
                }
            }
            if (m.groupCount() > 0) {
                String formatCopy = ednFormat
                        .replace("%thread%", linea.get("thread"))
                        .replace("%timestamp%", linea.get("timestamp"))
                        .replace("%level%", linea.get("level"));
                formatCopy = String.format(formatCopy, allMatches.toArray());
                builder.append("{");
                String[] pairs = formatCopy.split(",");
                for (String pair : pairs) {
                    if (!pair.matches("^ [:].* \\\"null\\\"$")) {
                        builder.append(pair);
                    }
                }
                builder.append("} ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
