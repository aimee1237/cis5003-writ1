package edu.cmu.csot.aimeealexander.server;

import edu.cmu.csot.aimeealexander.player.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Server extends Thread {
    public static final int DEFAULT_PORT_NUMBER = 8081;
    public static final String MESSAGE_PREFIX = "M:";
    public static final String QUESTION_PREFIX = "Q:";
    protected Socket socket;

    protected Object sharedObject;
    protected CyclicBarrier cyclicBarrier;

    public Server(Socket socket, CyclicBarrier cyclicBarrier) throws IOException {
        this.socket = socket;
        this.cyclicBarrier = cyclicBarrier;
        this.start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            sendMessage(out, "Welcome");

            String firstName = getQuestionAnswer(in, out, "Enter your first name");
            String lastName = getQuestionAnswer(in, out, "Enter your last name");
            String age = getQuestionAnswer(in, out, "Enter your age");

            Player player = new Player(firstName, lastName, Integer.valueOf(age));

            sendMessage(out, "Preparing game");

            cyclicBarrier.await();
            sendMessage(out, "Starting game");

        } catch (IOException | InterruptedException | BrokenBarrierException e) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void sendMessage(OutputStream outputStream, String message) throws IOException {
        outputStream.write((MESSAGE_PREFIX + message + "\n").getBytes());
    }


    private String getQuestionAnswer(InputStream inputStream, OutputStream outputStream, String question) throws IOException {
        outputStream.write((QUESTION_PREFIX + question + "\n").getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String request = br.readLine();
        return request;
    }

}