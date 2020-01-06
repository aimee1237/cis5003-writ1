package edu.cmu.csot.aimeealexander.player;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {

    String firstName;
    String lastName;
    int age;
    int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map<Integer, Integer> getAnswers() {
        return answers;
    }

    Map<Integer, Integer> answers = new LinkedHashMap<>();

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
