package edu.cmu.csot.aimeealexander.server.actions;

import edu.cmu.csot.aimeealexander.gamescript.Answer;
import edu.cmu.csot.aimeealexander.gamescript.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class SimpleQuestionAction implements ClientAction, Serializable {

    String question;

    public SimpleQuestionAction(String question) {
        this.question = question;
    }

    @Override
    public void doAction(BufferedReader in, PrintWriter out) throws IOException {

        System.out.println(question);
        String userInput = in.readLine();
        out.println(userInput);
    }

}
