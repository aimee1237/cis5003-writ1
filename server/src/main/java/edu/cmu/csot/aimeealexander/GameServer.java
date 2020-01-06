package edu.cmu.csot.aimeealexander;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.cmu.csot.aimeealexander.gamescript.QuestionBank;
import edu.cmu.csot.aimeealexander.server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GameServer {

    public static void main(String args[]) throws IOException, BrokenBarrierException, InterruptedException {

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
        QuestionBank questionBank = loadQuestionBank(inputFile);


        if (questionBank != null && questionBank.getPlayers() > 0 && questionBank.getQuestions().size() > 0) {

            final CyclicBarrier cyclicBarrier = new CyclicBarrier(questionBank.getPlayers() + 1);

            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(serverPort);
                int connectedPlayers = 0;


                List<Server> serverConnections = new ArrayList<>();
                System.out.println("-> This game has " + questionBank.getQuestions().size() + " questions for " + questionBank.getPlayers() + " players");
                while (connectedPlayers < questionBank.getPlayers()) {
                    // Creates a new SocketServer object for each connection this will allow multiple client connections
                    Socket socket = serverSocket.accept();
                    Server server = new Server(socket, cyclicBarrier, questionBank);

                    serverConnections.add(server);
                    connectedPlayers += 1;
                    System.out.println("--> " + connectedPlayers + " of " + questionBank.getPlayers() + " players connected from " + socket.getInetAddress().getHostAddress());
                }


                //Waits for all threads to catch-up
                cyclicBarrier.await();

                System.out.println("\n-> Starting game");


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

    }

    private static QuestionBank loadQuestionBank(String inputFile) throws IOException {
        String gameScriptXml = readGameScript(inputFile);
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(gameScriptXml, QuestionBank.class);
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
