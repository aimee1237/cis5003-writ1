package edu.cmu.csot.aimeealexander.gamescript;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.Set;

@JsonPropertyOrder({"text,answers"})
public class Question implements Comparable<Question>, Serializable {

    @JacksonXmlProperty(isAttribute = true)
    Integer id;

    @JacksonXmlProperty(isAttribute = true, localName = "answerid")
    Integer answerId;

    String text;

    @JacksonXmlElementWrapper(localName = "answers")
    @JacksonXmlProperty(localName = "option")
    Set<Answer> answers;

    public Question(){
    }

    public Question(Integer id, String text, Integer answerId,  Set<Answer> answers) {
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
    public int compareTo(Question other){
        return Integer.compare(this.id, other.id);
    }
}
