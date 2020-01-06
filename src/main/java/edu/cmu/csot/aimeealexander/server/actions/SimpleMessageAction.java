package edu.cmu.csot.aimeealexander.server.actions;

import java.io.*;

public class SimpleMessageAction implements ClientAction, Serializable {

    String message;

    public SimpleMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void doAction(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println(message);
    }
}
