package edu.cmu.csot.aimeealexander.player;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {

    String firstName;
    String lastName;
    Integer age;

    Map<Integer, Integer> answers = new LinkedHashMap<>();

    public final static String[] FIELDS = new String[]{"first name", "last name", "age"};

    public Player() {
    }

    public Player(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
