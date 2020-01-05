package edu.cmu.csot.aimeealexander.server;

import java.io.*;
import java.net.Socket;

public class Server extends Thread {
    public static final int DEFAULT_PORT_NUMBER = 8081;
    protected Socket socket;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        this.start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();



            out.write("Enter your first name:\n".getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;
            while ((request = br.readLine()) != null) {
                System.out.println("Message received:" + request);
                request += '\n';
                out.write(request.getBytes());
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}