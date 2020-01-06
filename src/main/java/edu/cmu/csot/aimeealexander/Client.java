package edu.cmu.csot.aimeealexander;

import edu.cmu.csot.aimeealexander.server.Server;
import edu.cmu.csot.aimeealexander.server.actions.ClientAction;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static final String DEFAULT_HOST_IP = "127.0.0.1";
    public static void main(String args[]) {
        new Client(DEFAULT_HOST_IP, Server.DEFAULT_PORT_NUMBER);
    }

    public Client(String host, int port) {
        try {
            String serverHostname = new String(host);

            System.out.println("Connecting to host " + serverHostname + " on port " + port);

            Socket echoSocket = null;
            PrintWriter out = null;
            ObjectInputStream in = null;

            try {
                echoSocket = new Socket(serverHostname, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new ObjectInputStream(echoSocket.getInputStream());
                //in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                ClientAction serverMessage = (ClientAction) in.readObject();
                serverMessage.doAction(stdIn, out);

                if (serverMessage.terminate()){
                    break;
                }
            }

            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}