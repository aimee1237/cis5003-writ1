package edu.cmu.csot.aimeealexander.server.actions;

import java.io.*;

public interface ClientAction {
    public void doAction(BufferedReader in, PrintWriter out) throws IOException;

    default public boolean terminate(){
        return false;
    }
}
