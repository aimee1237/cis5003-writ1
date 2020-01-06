package edu.cmu.csot.aimeealexander.server;

import edu.cmu.csot.aimeealexander.gamescript.Question;
import edu.cmu.csot.aimeealexander.gamescript.QuestionBank;
import edu.cmu.csot.aimeealexander.player.Player;
import edu.cmu.csot.aimeealexander.server.actions.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server extends Thread {

    protected Socket socket;

    protected final CyclicBarrier cyclicBarrier;
    protected final QuestionBank questionBank;
    protected Player player;

    InputStream in = null;
    OutputStream out = null;
    ObjectOutputStream oos = null;

    public Server(Socket socket, CyclicBarrier cyclicBarrier, QuestionBank questionBank) throws IOException {
        this.socket = socket;
        this.cyclicBarrier = cyclicBarrier;
        this.questionBank = questionBank;
        this.start();
    }

    public void run() {

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            oos = new ObjectOutputStream(out);

            //Initial greeting to the player
            sendSimpleMessage(new SimpleMessageAction("+ Welcome to the GAME + \nThis game will be for "
                        + questionBank.getPlayers() + " player(s)\n"
                        + "Complete the following registration before starting the game"));

            //Capture the players details
            ClientAction firstNameAction = new SimpleQuestionAction("Enter your first name:");
            String firstName = sendMessageAndGetResponse(firstNameAction);

            ClientAction lastNameAction = new SimpleQuestionAction("Enter your last name:");
            String lastName = sendMessageAndGetResponse(lastNameAction);

            ClientAction ageAction = new SimpleQuestionAction("Enter your age:");
            String age = sendMessageAndGetResponse(ageAction);

            player = new Player(firstName, lastName, Integer.valueOf(age));

            //Wait for all clients to get to this point before starting the game
            if (cyclicBarrier.getNumberWaiting() < cyclicBarrier.getParties() - 1) {
                sendSimpleMessage(new SimpleMessageAction("Waiting for other players to join and register before starting game"));
            }

            cyclicBarrier.await();

            //Start the game
            sendSimpleMessage(new SimpleMessageAction("\n++ Starting the game ++\n"));

            //Send the game questions and options
            for (Question question : questionBank.getQuestions()) {
                ClientAction questionBankAction = new QuestionBankAction(question);
                String response = sendMessageAndGetResponse(questionBankAction);

                //Record the result
                player.getAnswers().put(question.getId(), Integer.valueOf(response));
                if (Integer.valueOf(response) == question.getAnswerId()) {
                    player.setScore(player.getScore() + 1);
                }
            }

            sendSimpleMessage(new SimpleMessageAction("You scored " + player.getScore() +
                    " out of " + questionBank.getQuestions().size()));

            //Wait for all clients to finish
            cyclicBarrier.await();

            //Wait for the server to send the winner
            cyclicBarrier.await();

            //Disconnect the client from the game
            sendSimpleMessage(new EndGameAction());

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

    public void sendSimpleMessage(ClientAction action) throws IOException {
        oos.writeObject(action);
    }

    public String sendMessageAndGetResponse(ClientAction action) throws IOException {
        oos.writeObject(action);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String response = br.readLine();
        return response;
    }

    public Player getPlayer() {
        return player;
    }
}