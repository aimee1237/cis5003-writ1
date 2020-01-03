package edu.cmu.csot.aimeealexander;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.cmu.csot.aimeealexander.questions.QuestionBank;
import edu.cmu.csot.aimeealexander.server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    public static void main(String args[]) throws IOException {

        int serverPort = Server.DEFAULT_PORT_NUMBER;
        String inputFile = "";

        if (args.length == 1) {
            inputFile = args[0];
        } else if (args.length == 2) {
            serverPort = Integer.valueOf(args[1]);
            inputFile = args[0];
        } else {
            System.exit(1);
        }

        //Read the game-script
        String gameScriptXml = readGameScript(inputFile);
        XmlMapper xmlMapper = new XmlMapper();
        QuestionBank questionBank = xmlMapper.readValue(gameScriptXml, QuestionBank.class);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            int connectedPlayers = 0;

            List<Server> serverConnections = new ArrayList<>();
            while (connectedPlayers < questionBank.getPlayers()) {
                // Creates a new SocketServer object for each connection this will allow multiple client connections
                serverConnections.add(new Server(serverSocket.accept()));
                connectedPlayers += 1;
                System.out.println(connectedPlayers + " players");
            }

            for (Server clientConnection : serverConnections) {
                clientConnection.start();
            }


        } catch (IOException ex) {
            System.out.println("Failed to start server");
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }


    static String readGameScript(String gameScript) throws IOException {

        File gameScriptFile = new File(gameScript);
        if (gameScriptFile.exists() && gameScriptFile.isFile()) {
            InputStream inputStream = new FileInputStream(gameScriptFile);
            return inputStreamToString(inputStream);

        }
        return null;
    }


    static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
