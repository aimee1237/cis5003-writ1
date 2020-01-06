package edu.cmu.csot.aimeealexander.client;

import edu.cmu.csot.aimeealexander.server.actions.ClientAction;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static final String MESSAGE_PREFIX = "M:";
    public static final String QUESTION_PREFIX = "Q:";
    public  static  final  String GAME_OVER = "GAME OVER";

    public static void main(String args[]) {
        String host = "127.0.0.1";
        int port = 8081;
        new Client(host, port);
    }

    public Client(String host, int port) {
        try {
            String serverHostname = new String("127.0.0.1");

            System.out.println("Connecting to host " + serverHostname + " on port " + port + ".");

            Socket echoSocket = null;
            PrintWriter out = null;
            //BufferedReader in = null;
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

                if (serverMessage == null){
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