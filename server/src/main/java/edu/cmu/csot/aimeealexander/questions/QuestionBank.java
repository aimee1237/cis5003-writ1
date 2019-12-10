package edu.cmu.csot.aimeealexander.questions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@JsonPropertyOrder({"questions"})
public class QuestionBank {

    @XmlElement(name = "Question")
    Set<Question> questions;

    public QuestionBank() {
        this.questions = new TreeSet<Question>();
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}
