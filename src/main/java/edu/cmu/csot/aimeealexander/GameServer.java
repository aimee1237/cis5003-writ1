package edu.cmu.csot.aimeealexander;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.cmu.csot.aimeealexander.gamescript.QuestionBank;
import edu.cmu.csot.aimeealexander.server.Server;
import edu.cmu.csot.aimeealexander.server.actions.SimpleMessageAction;
import edu.cmu.csot.aimeealexander.server.actions.SimpleQuestionAction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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

                List<Server> clientConnections = new ArrayList<>();
                System.out.println("-> This game has " + questionBank.getQuestions().size() + " questions for " + questionBank.getPlayers() + " players");
                while (connectedPlayers < questionBank.getPlayers()) {
                    // Creates a new SocketServer object for each connection this will allow multiple client connections
                    Socket socket = serverSocket.accept();
                    Server server = new Server(socket, cyclicBarrier, questionBank);

                    clientConnections.add(server);
                    connectedPlayers += 1;

                    System.out.println("--> " + connectedPlayers + " of " + questionBank.getPlayers() + " players connected from " + socket.getInetAddress().getHostAddress());
                }

                //Wait for all players to get to the same point before starting the game
                cyclicBarrier.await();
                System.out.println("\n-> Starting game");

                //Wait for all players to complete the questions
                cyclicBarrier.await();

                //Sort the players based on the score. Adding to a list for multiple same scores
                TreeMap<Integer, LinkedList<Server>> result = new TreeMap<>(Collections.reverseOrder());
                for (Server s : clientConnections){

                    int score = s.getPlayer().getScore();
                    if (!result.containsKey(score)){
                        LinkedList<Server> ll = new LinkedList<>();
                        ll.add(s);
                        result.put(score, ll);
                    }else {
                        LinkedList<Server> ll = result.get(score);
                        ll.add(s);
                    }
                }

                int highestScore = result.firstEntry().getKey();
                LinkedList<Server> lll = result.firstEntry().getValue();

                //Capture the top scorers
                String topScorers = "";
                for (Server s: lll){
                    topScorers = topScorers + s.getPlayer().getFirstName() + " " + s.getPlayer().getLastName() + ", age " + s.getPlayer().getAge() + "\n";
                }


                int count = 0;
                for(Map.Entry<Integer, LinkedList<Server>> entry: result.entrySet()) {
                    if (count == 0 && entry.getKey()>0){
                        for (Server s : entry.getValue()){
                            s.sendSimpleMessage(new SimpleMessageAction( "Congratulations, you were a top scorer! The top scorers were - \n" + topScorers));
                        }
                    }else{
                        for (Server s : entry.getValue()){
                            s.sendSimpleMessage(new SimpleMessageAction( "The highest scorer(s) were - \n" + topScorers));
                        }
                    }
                    count+=1;
                }


                System.out.println("\n-> Stopping the game");
                cyclicBarrier.await();
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
