package edu.cmu.csot.aimeealexander.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            BufferedReader in = null;

            try {
                echoSocket = new Socket(serverHostname, 8081);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                String s = in.readLine();

                if (s.startsWith(QUESTION_PREFIX)){
                    System.out.println(s.substring(QUESTION_PREFIX.length()));
                    String userInput = stdIn.readLine();
                    out.println(userInput);

                } else if (s.startsWith(MESSAGE_PREFIX)){
                    System.out.println(s.substring(MESSAGE_PREFIX.length()));

                } else if (s.equals(GAME_OVER)) {
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