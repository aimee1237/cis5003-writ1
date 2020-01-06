package edu.cmu.csot.aimeealexander.server.actions;

import edu.cmu.csot.aimeealexander.gamescript.Answer;
import edu.cmu.csot.aimeealexander.gamescript.Question;

import java.io.*;

public class QuestionBankAction implements ClientAction, Serializable {

    Question question;

    public QuestionBankAction(Question question) {
        this.question = question;
    }

    @Override
    public void doAction(BufferedReader in, PrintWriter out) throws IOException {

        System.out.println(question.getText());
        for (Answer answer: question.getAnswers()){
            System.out.println(answer.getId() + ")" + answer.getText() );
        }

        String userInput = in.readLine();
        out.println(userInput);
    }

}
