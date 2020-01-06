package edu.cmu.csot.aimeealexander;

import edu.cmu.csot.aimeealexander.server.Server;
import edu.cmu.csot.aimeealexander.server.actions.ClientAction;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String args[]) {

        String hostIp = GameServer.DEFAULT_HOST_IP;
        int serverPort = GameServer.DEFAULT_PORT_NUMBER;

        if (args.length == 1) {
            hostIp = args[0];
        } else if (args.length == 2) {
            hostIp = args[0];
            serverPort = Integer.valueOf(args[1]);
        } else if (args.length > 2){
            System.exit(1);
        }

        new Client(hostIp, serverPort);
    }

    public Client(String host, int port) {
        try {
            String serverHostname = new String(host);

            System.out.println("Connecting to host " + serverHostname + " on port " + port);

            Socket echoSocket = null;
            PrintWriter out = null;
            ObjectInputStream in = null;

            try {
                echoSocket = new Socket(serverHostname, port);
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new ObjectInputStream(echoSocket.getInputStream());
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