package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerApp {

    public static void main(String[] args) throws IOException {
        int port = 8089;
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        questions.add("Whats your name?");
        questions.add("Ready to answer 2 questions about capitals? (no to abort / any to start)");
        questions.add("Capital of Russia (Moscow, St.Petersburg, Kazan, other...)");
        questions.add("Capital of Israel (Haifa, Jerusalem, Tel-Aviv, other...)");

        System.out.println("server started");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept(); //ждем подключения
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {


                    out.println("Hi! Type start to start or end to end!");

                    String inputLine;
                    int questionNumber = 0;
                    boolean started = false;

                    while ((inputLine = in.readLine()) != null) {
                        if ("end".equals(inputLine.toLowerCase())) {
                            out.println("Good bye!");
                            break;
                        }
                        if (started) {
                            answers.add(inputLine);
                            if (questionNumber == 1 && "no".equals(inputLine.toLowerCase())) {
                                out.println(String.format("Ok, %s, you are the boss! See you!", answers.get(0)));
                                break;
                            }

                            questionNumber++;
                            if (questions.size() < questionNumber + 1) {
                                out.println(String.format("Dear %1$s! You replied, that capitals is %2$s and %3$s! Thanks! See you!", answers.get(0), answers.get(2), answers.get(3)));
                                break;
                            } else {
                                out.println(questions.get(questionNumber));
                            }
                        }
                        if (!started) {
                            if ("start".equals(inputLine.toLowerCase())) {
                                started = true;
                                out.println(questions.get(questionNumber));
                            } else {
                                out.println("Type start to start or end to end!");
                            }
                        }
                    }
                }
            }
        }
    }
}

