package edu.cmu.csot.aimeealexander.questions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonPropertyOrder({"text,answers"})
public class Question implements Comparable {

    @XmlAttribute(name = "id")
    @XmlID
    Integer id;

    @XmlAttribute(name = "answerid")
    @XmlID
    Integer answerId;

    String text;
    Set<Answer> answers;

    public Question(Integer id, Integer answerId, String text, Set<Answer> answers) {
        this.id = id;
        this.answerId = answerId;
        this.text = text;
        this.answers = answers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
