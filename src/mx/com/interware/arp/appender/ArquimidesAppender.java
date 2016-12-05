package mx.com.interware.arp.appender;

import java.util.Calendar;
import mx.com.interware.arp.appender.remote.BufferedSender;
import mx.com.interware.arp.appender.remote.PersistentSocket;
import mx.com.interware.arp.appender.util.ArpUtil;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 *
 * @author Jorge Esteban Zaragoza Salazar
 * @version 1.0.0 - Clase que funciona como appender para Log4j
 *
 */
@Plugin(name = "ArquimedesAppender", category = "Core", elementType = "appender", printObject = true)
public class ArquimidesAppender extends AbstractAppender {

    private String regexp;
    private String ednFormat;
    private String host;
    private String port;
    private String reconnectionTime;
    private String sendDelta;
    private String maxQueue;
    private BufferedSender bSender;
    private PersistentSocket worker;
    private static Boolean initizalized = false;

    protected ArquimidesAppender(String name, String host,
            String port, String reconnectionTime, String maxQueue, String sendDelta, String regexp, String ednFormat) {
        super(name, null, null, true);
        this.host = host;
        this.port = port;
        this.reconnectionTime = reconnectionTime;
        this.maxQueue = maxQueue;
        this.sendDelta = sendDelta;
        this.regexp = regexp;
        this.ednFormat = ednFormat;
    }

    @Override
    public void append(LogEvent loggingEvent) {
        if (!ArquimidesAppender.initizalized) {
            synchronized (ArquimidesAppender.initizalized) {
                if (!ArquimidesAppender.initizalized) {
                    worker = new PersistentSocket(Integer.parseInt(this.reconnectionTime));
                    worker.start(this.host, Integer.parseInt(this.port));
                    ArquimidesAppender.initizalized = true;
                    bSender = new BufferedSender(Calendar.getInstance().getTimeInMillis(),
                            worker,
                            Long.parseLong(this.sendDelta),
                            Integer.parseInt(this.maxQueue),
                            regexp,
                            ednFormat
                    );
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        if (ArpUtil.matches(loggingEvent.getMessage().getFormattedMessage(), this.regexp) && worker.getIsConnected()) {
            bSender.add(loggingEvent);
        }
    }

    @PluginFactory
    public static ArquimidesAppender createAppender(@PluginAttribute("name") String name,
            @PluginAttribute("host") String host,
            @PluginAttribute("port") String port,
            @PluginAttribute("reconnectionTime") String reconnectionTime,
            @PluginAttribute("maxQueue") String maxQueue,
            @PluginAttribute("sendDelta") String sendDelta,
            @PluginAttribute("regexp") String regexp,
            @PluginAttribute("ednFormat") String ednFormat) {
        return new ArquimidesAppender(name, host, port, reconnectionTime, maxQueue, sendDelta,
                regexp, ednFormat);
    }

}
