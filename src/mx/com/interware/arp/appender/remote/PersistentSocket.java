package mx.com.interware.arp.appender.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

/**
 *
 * @author Jorge Esteban Zaragoza Salazar
 * @version 1.0.0 - Clase que realiza abre la conexión con un socket y trata de
 * mantenerla en caso de error o desconexión
 *
 */
public class PersistentSocket {

    private Socket socket;
    private Thread socketThread;
    private final Long reconnectDelay;
    private Boolean isConnected = false;
    private PrintWriter output;

    /**
     *
     * Constructor que inicializa el tiempo de reconexión al tener un error o
     * desconexión
     *
     * @param reconnectDelay Tiempo de espera para reconexión
     *
     *
     */
    public PersistentSocket(long reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }

    /**
     * Método que inicializa el worker de reconexión
     *
     * @param server Host o dirección IP del servidor a donde será enviado el
     * EDN del log
     * @param port Puerto del servidor a donde será enviado el
     * EDN del log
     */
    public void start(final String server, final int port) {
        socketThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (isConnected) {
                            sleep(reconnectDelay);
                        } else {
                            connect(server, port);
                            isConnected = true;
                            break;
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        try {
                            isConnected = false;
                            System.out.println("Tratando de reconectar en " + reconnectDelay);
                            sleep(reconnectDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        };
        socketThread.start();
    }
    
    /**
     * 
     * Método que conecta al servidor y puerto especificados
     * 
     * @param server Host o dirección IP del servidor a donde será enviado el
     * EDN del log
     * @param port Puerto del servidor a donde será enviado el
     * EDN del log
     * @throws UnknownHostException
     * @throws IOException 
     */
    private void connect(String server, int port) throws UnknownHostException, IOException {
        this.socket = new Socket(server, port);
        this.output = new PrintWriter(socket.getOutputStream());
    }
    /**
     * Método que envía un mensaje de texto al servidor
     * @param message mensaje a enviar
     */
    public void sendMessage(String message) {
        if (this.output != null) {
            this.output.write(message + "\n");
            this.output.flush();
        }
    }
    /**
     * Método que funciona como getter del atributo isConnected
     * @return Devuelve si el socket se encuentra conectado
     */
    public Boolean getIsConnected() {
        return isConnected;
    }

}
