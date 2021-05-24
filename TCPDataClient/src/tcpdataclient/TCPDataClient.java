/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpdataclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arigato
 */
public class TCPDataClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            TCPDataClient obj = new TCPDataClient();
            Socket objClient = new Socket(InetAddress.getByName("127.0.0.1"), 8756);
            DataInputStream din = new DataInputStream(objClient.getInputStream());
            DataOutputStream dout = new DataOutputStream(objClient.getOutputStream());
            byte[] buffer = obj.CreateDataPacket("I Own CodeVLOG".getBytes("UTF8"));//encoding
            dout.write(buffer);
            dout.flush();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private byte[] CreateDataPacket(byte[] data) {
        byte[] packet = null;
        try {
            byte[] initialise = new byte[1];
            initialise[0] = 2;
            byte[] separator = new byte[1];
            separator[0] = 4;
            byte[] dataLength = String.valueOf(data.length).getBytes("UTF8"); //shayad beshe bedune encoding har fily ro transfer konam.
            packet = new byte[initialise.length + separator.length + dataLength.length+data.length];

            System.arraycopy(initialise, 0, packet, 0, initialise.length);
            System.arraycopy(dataLength, 0, packet, initialise.length, dataLength.length);
            System.arraycopy(separator, 0, packet, initialise.length + dataLength.length, separator.length);
            System.arraycopy(data, 0, packet, initialise.length + dataLength.length + separator.length , data.length);



        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

}
