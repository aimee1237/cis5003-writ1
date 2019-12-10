package edu.cmu.csot.aimeealexander.questions;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;


public class Answer implements Comparable {

    @XmlAttribute(name = "id")
    @XmlID
    Integer id;

    @XmlElement
    String option;

    public Answer(Integer id, String option) {
        this.id = id;
        this.option = option;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
