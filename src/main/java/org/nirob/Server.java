package org.nirob;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nirob
 */
// File Name GreetingServer.java
import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import static org.nirob.ClientForm.pause;
import static org.nirob.Main.urlM;

public class Server extends Thread {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(50000);
    }

    @Override
    public void run() {
        System.out.println(urlM);
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(new File(urlM));
        Java2DFrameConverter frameConverter = new Java2DFrameConverter();
        frameGrabber.setFormat("mp4");

        System.out.println("Waiting for client on port "
                + serverSocket.getLocalPort() + "...");

        try (Socket socket = serverSocket.accept()) {

            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
            //DataInputStream in = new DataInputStream(socket.getInputStream());
           // System.out.println(in.readUTF());

            try (InputStream is = socket.getInputStream();
                    ObjectInputStream oiclient = new ObjectInputStream(is);   
                    OutputStream out = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out)
                    ) {

                frameGrabber.start();
                while (true) {
                    Frame frame = frameGrabber.grab();
                    BufferedImage bi = frameConverter.convert(frame);
            
                    if (bi != null) {
                        oos.writeObject(new Packet(bi));
                        oos.flush();
                    }
                    ClientToServer cts = (ClientToServer) oiclient.readObject();
                    pause = cts.pause;
                    //String str;
                   // try(str = in.readUTF()){
                        
                   // }
                   // System.out.println(in.readUTF());
                    //String str = in.readUTF();
                    while(pause == 1){
                        System.out.println(pause);
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            try {
                frameGrabber.stop();
            } catch (FrameGrabber.Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println("Server Closed!!!");
    }

    public static void start(int port) {
        try {
            Thread t = new Server(port);
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
