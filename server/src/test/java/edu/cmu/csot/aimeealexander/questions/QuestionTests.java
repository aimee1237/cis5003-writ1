package edu.cmu.csot.aimeealexander.questions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class QuestionTests {

    @Test
    public void serialiseToXml() {

        //Question#1
        Set<Answer> questionOneOptions = new TreeSet();
        questionOneOptions.add(new Answer(1, "Biro Brothers"));
        questionOneOptions.add(new Answer(2, "Waterman Brothers"));
        questionOneOptions.add(new Answer(3, "Bicc Brothers"));
        questionOneOptions.add(new Answer(4, "Write Brothers"));
        Question questionOne = new Question(1, "Who invented the Ballpoint Pen?", 1, questionOneOptions);

        //Question#2
        Set<Answer> questionTwoOptions = new TreeSet();
        questionTwoOptions.add(new Answer(1, "1950s"));
        questionTwoOptions.add(new Answer(2, "1960s"));
        questionTwoOptions.add(new Answer(3, "1970s"));
        questionTwoOptions.add(new Answer(4, "1980s"));
        Question questionTwo = new Question(2, "In which decade was the first solid state integrated circuit demonstrated?", 1, questionTwoOptions);

        //Question#3
        Set<Answer> questionThreeOptions = new TreeSet();
        questionThreeOptions.add(new Answer(1, "Isaac Newton"));
        questionThreeOptions.add(new Answer(2, "Albert Einstein"));
        questionThreeOptions.add(new Answer(3, "Benjamin Franklin"));
        questionThreeOptions.add(new Answer(4, "Marie Curie"));
        Question questionThree = new Question(3, "Which scientist discovered the radioactive element radium?", 4, questionThreeOptions);

        //QuestionBank
        Set<Question> questions = new TreeSet<>();
        questions.add(questionOne);
        questions.add(questionTwo);
        questions.add(questionThree);
        QuestionBank questionBank = new QuestionBank();
        questionBank.setQuestions(questions);
        questionBank.setPlayers(3);


        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String xml = null;
        try {
            xml = xmlMapper.writeValueAsString(questionBank);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(3, questionBank.getQuestions().size());

        Assert.assertTrue(xml.contains("<QuestionBank players=\"3\">"));
        Assert.assertTrue(xml.contains("<Question id=\"1\" answerid=\"1\">"));
        Assert.assertTrue(xml.contains("<Question id=\"2\" answerid=\"1\">"));
        Assert.assertTrue(xml.contains("<Question id=\"3\" answerid=\"4\">"));
    }


    @Test
    public void deserializeFromXML() throws IOException {

        InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("game-script.xml");
        String xml = inputStreamToString(inputStream);

        XmlMapper xmlMapper = new XmlMapper();
        QuestionBank questionBank = xmlMapper.readValue(xml, QuestionBank.class);

        Assert.assertEquals(Integer.valueOf(3), questionBank.getPlayers());
        Assert.assertEquals(3, questionBank.getQuestions().size());

        //Load into a map for easier assertion
        Map<Integer, Question> questionMap = new HashMap<>();
        for (Question question : questionBank.getQuestions()) {
            questionMap.put(question.getId(), question);
        }

        Assert.assertEquals("Who invented the Ballpoint Pen?", questionMap.get(1).text);
        Assert.assertEquals("Which scientist discovered the radioactive element radium?", questionMap.get(3).text);
        Assert.assertEquals(Integer.valueOf(4), questionMap.get(3).getAnswerId());
    }


    public String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
