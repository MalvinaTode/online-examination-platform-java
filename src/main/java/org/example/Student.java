package org.example;

import java.util.HashMap;
import java.util.Map;

public class Student {

    private String name;
    private String group;
    private Map<String, Double> examScores; //colectie pt nume examen + scor

    //constructor
    public Student(String name, String group) {
        this.name = name;
        this.group = group;
        this.examScores = new HashMap<>();
    }

    //metoda prin care adaug un nou examen
    public void addExamScore(String examName, double score) {
        examScores.put(examName, score);
    }
//gettere
    public Double getScoreForExam(String examName) {
        return examScores.get(examName);
    }

    public Map<String, Double> getExamScores() {
        return examScores;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
}
