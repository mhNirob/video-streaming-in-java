package org.nirob;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 *
 * @author mehedi hasan nirob
 */
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.nirob.Main.pause;
import static org.nirob.Main.play;

public class Client extends Thread {

    final ClientForm frame;
    String serverIP;
    int port;

    public Client(String serverIP, int port) {
        this.frame = new ClientForm();
        this.serverIP = serverIP;
        this.port = port;
    }

    @Override
    public void run() {

        System.out.println("Connecting to " + serverIP + " on port" + port);
        try (Socket client = new Socket(serverIP, port) ) {

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            //OutputStream outToServer = client.getOutputStream();
            //DataOutputStream out = new DataOutputStream(outToServer);
         
            //out.writeUTF(Integer.toString(play)+Integer.toString(pause));

            try (InputStream is = client.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    OutputStream outToserver = client.getOutputStream();
                    ObjectOutputStream ooServer = new ObjectOutputStream(outToserver)
                ){
                while (true) {
                    try {
                        //if(play == 0){
                        //    break;
                       // }
                        Packet packet = (Packet) ois.readObject();
                        //frame.setSize(100, 100);
                        frame.setImage(packet.getImage());
                        //if(play == )
                        //packet.pplay = play;
                        //packet.ppause = pause;
                       // ooServer.writeObject(new Packet(pause,play));
                        //out.writeUTF(Integer.toString(play)+Integer.toString(pause));
                        //System.out.println("1");
                        //ooServer.writeObject(new ClientToServer(play,pause));
                        //ooServer.flush();
                    } catch (IOException | ClassNotFoundException ex) {
                        //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client Closed!!!");
    }

    public static void start(String serverIP, int port) {
        Thread t = new Client(serverIP, port);
        t.setDaemon(true);
        t.start();
    }
}
