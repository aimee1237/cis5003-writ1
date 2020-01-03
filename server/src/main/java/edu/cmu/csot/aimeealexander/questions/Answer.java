package edu.cmu.csot.aimeealexander.questions;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;


public class Answer implements Comparable<Answer> {

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
