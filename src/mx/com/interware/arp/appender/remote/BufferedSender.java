package mx.com.interware.arp.appender.remote;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.interware.arp.appender.util.EdnUtil;
import org.apache.logging.log4j.core.LogEvent;

/**
 *
 * @author Jorge Esteban Zaragoza Salazar
 * @version 1.0.0 - Clase que permite realizar envío de entradas de log por
 * tiempo y por numero de entradas en cola
 *
 */
public class BufferedSender {

    private static Long timestampNow;
    private PersistentSocket socket;
    private Long delta;
    private CopyOnWriteArrayList<Map> linesLst;
    private Integer maxQueue;
    private String regexp;
    private String ednFormat;

    /**
     *
     * Constructor que inicializa los atributos de la clase y levanta un worker
     * que se reconecta al tener un error o desconexión.
     *
     * @param timestampNow timestamp en milisegundos del tiempo de la operación.
     * @param socket Socket persistente por el cuál se realizará la
     * comunicación.
     * @param delta tiempo de espera de renvío en milisegundos.
     * @param maxQueue numero máximo de elementos en espera de envío.
     * @param regexp expresión regular para comprobar los mensajes del log.
     * @param ednFormat formato del edn de retorno
     *
     *
     */
    public BufferedSender(Long timestampNow,
            PersistentSocket socket,
            Long delta,
            Integer maxQueue,
            String regexp,
            String ednFormat) {
        BufferedSender.timestampNow = timestampNow;
        this.socket = socket;
        this.delta = delta;
        this.linesLst = new CopyOnWriteArrayList<Map>();
        this.maxQueue = maxQueue;
        this.regexp = regexp;
        this.ednFormat = ednFormat;
        final PersistentSocket socketThread = socket;
        final Long deltaThread = delta;
        Thread senderWorker = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (BufferedSender.timestampNow < (Calendar.getInstance().getTimeInMillis() + deltaThread)
                                && socketThread.getIsConnected()
                                && linesLst.size() > 0) {
                            sendMessage();
                        } else {
                            sleep(deltaThread);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BufferedSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        senderWorker.start();
    }

    /**
     * Método que agrega a la cola de envío un evento del appender
     *
     * @param event evento del logging obtenido desde el appender
     */
    public synchronized void add(LogEvent event) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("thread", event.getThreadName());
        data.put("level", event.getLevel().toString());
        data.put("timestamp", String.valueOf(event.getTimeMillis()));
        data.put("message", event.getMessage().getFormattedMessage().toString());
        linesLst.add(data);
        BufferedSender.timestampNow = Calendar.getInstance().getTimeInMillis();
        if (linesLst.size() >= maxQueue && socket.getIsConnected()) {
            sendMessage();
        }

    }

    /**
     * Método que envía y limpia la cola de envío al socket
     */
    private void sendMessage() {
        List<Map> envio = new CopyOnWriteArrayList<Map>();
        for(Map mapa : linesLst){
            envio.add(mapa);
        }
        this.flush();
        socket.sendMessage(EdnUtil.createEDN(envio, regexp, ednFormat));
    }

    /**
     * Método que limpia la cola de envío
     */
    public synchronized void flush() {
        linesLst.clear();
    }
}
