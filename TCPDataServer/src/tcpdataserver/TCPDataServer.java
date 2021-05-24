/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpdataserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arigato
 */
public class TCPDataServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            
            ServerSocket serverSocket = new ServerSocket(8756);
        
            while (true) {
                /*    Socket mySocket = serverSocket.accept();
                 ClientWorker cw = new ClientWorker(mySocket);
                 Thread t1 = new Thread(cw);
                 ClientWorker cwHandler = new ClientWorker(t1);
                 */
                //new Thread(new ClientWorker(serverSocket.accept())).start();
                new Thread(new ClientWorker(serverSocket.accept())).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

    class ClientWorker implements Runnable {

        private Socket targetSocket;
        private DataInputStream din;
        private DataOutputStream dout;
        //private Thread myThread;
        //private boolean goDie = false;

        public ClientWorker(Socket receivedSocket) {
            try {
                targetSocket = receivedSocket;
                din = new DataInputStream(targetSocket.getInputStream());
                dout = new DataOutputStream(targetSocket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public ClientWorker(Thread cwThread) {
            cwThread.start();
        }

        @Override
        public void run() {
            while (true) {
                byte[] initialise = {0};
                try {
                    if (din.available() > 0) {
                        din.read(initialise, 0, initialise.length);
                    } else {
                        Thread.sleep(2);
                    }

                    if (initialise[0] == 2) {
                        System.out.println(new String(ReadStream()));
                        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                        int counter = 0;
                        for (Thread t : threadSet) {
                            if (t.getState() == Thread.State.RUNNABLE) {
                                counter++;
                            }
                        }
                        System.out.println(counter + "Threads");
                        //goDie = true;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        private byte[] ReadStream() {
            byte[] dataBuffer = null;
            try {
                int b = 0;
                String bufferLength = "";
                while ((b = din.read()) != 4) {
                    bufferLength += (char) b;
                }
                int dataLength = Integer.parseInt(bufferLength);
                dataBuffer = new byte[dataLength];
                int byteRead = 0;
                int byteOffset = 0;
                while (byteOffset < dataLength) {
                    byteRead = din.read(dataBuffer, byteOffset, dataLength - byteOffset);
                    byteOffset += byteRead;
                }
            } catch (IOException ex) {
                Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            return dataBuffer;
        }

    }
