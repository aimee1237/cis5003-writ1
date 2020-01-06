package edu.cmu.csot.aimeealexander.server;

import edu.cmu.csot.aimeealexander.gamescript.Question;
import edu.cmu.csot.aimeealexander.gamescript.QuestionBank;
import edu.cmu.csot.aimeealexander.player.Player;
import edu.cmu.csot.aimeealexander.server.actions.ClientAction;
import edu.cmu.csot.aimeealexander.server.actions.QuestionBankAction;
import edu.cmu.csot.aimeealexander.server.actions.SimpleMessageAction;
import edu.cmu.csot.aimeealexander.server.actions.SimpleQuestionAction;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server extends Thread {
    public static final int DEFAULT_PORT_NUMBER = 8081;
    public static final String MESSAGE_PREFIX = "M:";
    public static final String QUESTION_PREFIX = "Q:";
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
            sendSimpleMessage(oos,"Welcome to the GAME.  This game will be for " + questionBank.getPlayers() + "player(s)");

            //Capture the players details
            ClientAction firstNameAction = new SimpleQuestionAction("Enter your first name:");
            String firstName = sendQuestion(in,oos,firstNameAction);

            ClientAction lastNameAction = new SimpleQuestionAction("Enter your last name:");
            String lastName = sendQuestion(in,oos,lastNameAction);

            ClientAction ageAction = new SimpleQuestionAction("Enter your age:");
            String age = sendQuestion(in,oos,firstNameAction);

            Player player = new Player(firstName,lastName, Integer.valueOf(age));



            //Wait for all clients to get to this point before starting the game
            if (cyclicBarrier.getNumberWaiting() < cyclicBarrier.getParties()-1){
                cyclicBarrier.await();
                sendSimpleMessage(oos,"Waiting for other players to get to join before starting game");
            }

            sendSimpleMessage(oos,"Waiting for other players to get to join before starting game");

            //Send the game questions and options
            for (Question question:questionBank.getQuestions()){
                ClientAction questionBankAction = new QuestionBankAction(question);
                String response = sendQuestion(in,oos,questionBankAction);
                System.out.println(question.getId() + ") You entered :" + response);
            }


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

    private void sendSimpleMessage(ObjectOutputStream oos, String message) throws IOException {
        ClientAction action = new SimpleMessageAction(message);
        oos.writeObject(action);
    }

    private String sendQuestion(InputStream in, ObjectOutputStream oos, ClientAction action) throws IOException {
        oos.writeObject(action);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String response = br.readLine();
        return response;
    }

}