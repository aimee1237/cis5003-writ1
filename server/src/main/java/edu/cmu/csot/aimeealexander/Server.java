package edu.cmu.csot.aimeealexander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.cmu.csot.aimeealexander.questions.Answer;
import edu.cmu.csot.aimeealexander.questions.Question;
import edu.cmu.csot.aimeealexander.questions.QuestionBank;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

public class Server {



    public static void deserializeFromXML() {
        try {
            XmlMapper xmlMapper = new XmlMapper();

            //ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            //InputStream is = classloader.getResourceAsStream("game-script.xml");

            // read file and put contents into the string
            String readContent = new String(Files.readAllBytes(Paths.get("D:\\aimee\\cis5003-writ1\\server\\build\\resources\\main\\game-script.xml")));

            // deserialize from the XML into a Phone object
            QuestionBank deserializedData = xmlMapper.readValue(readContent, QuestionBank.class);

            // Print object details
            System.out.println("Deserialized data: ");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void serialiseToXML(){

        Answer answer = new Answer(1, "the answer");
        Set<Answer> set = new TreeSet();
        set.add(answer);

        Question question = new Question(1,1,"the question", set);
        QuestionBank questionBank = new QuestionBank();

        Set<Question> questions = new TreeSet<>();
        questions.add(question);
        questionBank.setQuestions(questions);


        ObjectMapper objectMapper = new XmlMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String xml = null;
        try {
            xml = objectMapper.writeValueAsString(questionBank);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(xml);
    }

    public static void main(String[] args) {
        System.out.println("Deserializing from XML...");
        //deserializeFromXML();
        serialiseToXML();
    }
}
