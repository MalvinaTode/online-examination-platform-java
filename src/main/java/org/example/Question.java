package org.example;

import java.time.LocalDateTime;
//clasa abstracta care implemeteaza interfata Gradable
// si simuleaza o intrebare de examen
public abstract class Question<T> implements Gradable {
    //adaug campurile cerute

    private String text;
    private String name;
    private LocalDateTime data;
    private double maxScore;
    private int difficulty;

    public Question(String text, String name, LocalDateTime data, double maxScore, int difficulty) {
        this.text = text;
        this.name = name;
        this.data = data;
        this.maxScore = maxScore;
        this.difficulty = difficulty;
    }

    //metoda abstracta ceruta
    public abstract Correctness checkAnswer(T answer);

    //getter+settere

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public double getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
