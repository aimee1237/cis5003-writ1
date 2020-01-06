package edu.cmu.csot.aimeealexander.server.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class EndGameAction implements ClientAction, Serializable {
    @Override
    public void doAction(BufferedReader in, PrintWriter out) throws IOException {
    }

    @Override
    public boolean terminate(){
        return true;
    }
}
