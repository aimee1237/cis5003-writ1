package edu.cmu.csot.aimeealexander.gamescript;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.io.Serializable;


public class Answer implements Comparable<Answer>, Serializable {

    @JacksonXmlProperty(isAttribute = true)
    Integer id;

    @JacksonXmlText
    String text;

    public Answer(){
    }

    public Answer(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Answer other){
        return Integer.compare(this.id, other.id);
    }
}
