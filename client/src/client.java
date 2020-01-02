
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class client {
    private Socket clientSocket;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private boolean stopClient;

    private Thread clientReader;

    private String hostName;
    private int portNumber;

    public static void main(String[] args) {

        if (args.length != 2){
            System.out.println("Error: This program requires two arguments host name and port");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try {
            Socket quizGame = new Socket(hostName, portNumber);
            System.out.println("Connected to " +hostName + " the port number is: " + portNumber);

            InputStream is = quizGame.getInputStream();
            OutputStream os = quizGame.getOutputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(quizGame.getInputStream()));
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);


            String sendMessage = "Hello\n";
            bw.write(sendMessage);
            bw.flush();
//read message


        } catch (UnknownHostException e) {
            System.out.println("Error: The hostname" + hostName + " is unknown");

        } catch (IOException e) {
            System.out.println("Error: Connection refused, is the server listening?");
        }
    }


}


