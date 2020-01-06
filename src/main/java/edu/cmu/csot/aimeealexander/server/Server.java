package edu.cmu.csot.aimeealexander.server;

import edu.cmu.csot.aimeealexander.client.Client;
import edu.cmu.csot.aimeealexander.gamescript.Question;
import edu.cmu.csot.aimeealexander.gamescript.QuestionBank;
import edu.cmu.csot.aimeealexander.player.Player;
import edu.cmu.csot.aimeealexander.server.actions.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server extends Thread {
    public static final int DEFAULT_PORT_NUMBER = 8081;
    protected Socket socket;

    protected final CyclicBarrier cyclicBarrier;
    protected  final QuestionBank questionBank;

    public Server(Socket socket, CyclicBarrier cyclicBarrier, QuestionBank questionBank) throws IOException {
        this.socket = socket;
        this.cyclicBarrier = cyclicBarrier;
        this.questionBank = questionBank;
        this.start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        ObjectOutputStream oos = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            oos = new ObjectOutputStream(out);

            //Initial greeting to the player
            sendSimpleMessage(oos,new SimpleMessageAction("Welcome to the GAME.  This game will be for " + questionBank.getPlayers() + "player(s)"));

            //Capture the players details
            ClientAction firstNameAction = new SimpleQuestionAction("Enter your first name:");
            String firstName = sendMessageAndGetResponse(in,oos,firstNameAction);

            ClientAction lastNameAction = new SimpleQuestionAction("Enter your last name:");
            String lastName = sendMessageAndGetResponse(in,oos,lastNameAction);

            ClientAction ageAction = new SimpleQuestionAction("Enter your age:");
            String age = sendMessageAndGetResponse(in,oos,ageAction);

            Player player = new Player(firstName,lastName, Integer.valueOf(age));

            //Wait for all clients to get to this point before starting the game
            if (cyclicBarrier.getNumberWaiting() < cyclicBarrier.getParties()-1){
                cyclicBarrier.await();
                sendSimpleMessage(oos,new SimpleMessageAction("Waiting for other players to get to join before starting game"));
            }

            sendSimpleMessage(oos,new SimpleMessageAction("\n++ Starting the game ++\n"));


            //Send the game questions and options
            for (Question question:questionBank.getQuestions()){
                ClientAction questionBankAction = new QuestionBankAction(question);
                String response = sendMessageAndGetResponse(in,oos,questionBankAction);

                //Record the result
                player.getAnswers().put(question.getId(),Integer.valueOf(response) );
                if (Integer.valueOf(response) == question.getAnswerId()){
                    player.setScore(player.getScore()+1);
                }
            }

            sendSimpleMessage(oos, new SimpleMessageAction("You scored " + player.getScore() +
                    " out of " + questionBank.getQuestions().size()));

            //Disconnect the client from the game
            sendSimpleMessage(oos, new EndGameAction());

        } catch (IOException | InterruptedException | BrokenBarrierException e) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                oos.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendSimpleMessage(ObjectOutputStream oos, ClientAction action) throws IOException {
        oos.writeObject(action);
    }

    private String sendMessageAndGetResponse(InputStream in, ObjectOutputStream oos, ClientAction action) throws IOException {
        oos.writeObject(action);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String response = br.readLine();
        return response;
    }

}