package edu.cmu.csot.aimeealexander.gamescript;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@JsonPropertyOrder({"questions"})
public class QuestionBank implements Serializable {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Question")
    Set<Question> questions;

    @JacksonXmlProperty(isAttribute = true)
    Integer players;

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public QuestionBank() {
    }

    public QuestionBank(TreeSet<Question> questions) {
        this.questions = questions;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}
