package org.example;

import java.time.LocalDateTime;

public class OpenEndedQuestion extends Question<String> {

    //atribut suplimentar pt rasp corect
    private String correctAnswer;

    //constructor
    public OpenEndedQuestion(String text, String name, LocalDateTime date,
                             double maxScore, int difficulty, String correctAnswer) {
        super(text, name, date, maxScore, difficulty); //mostenite de la parinte->question
        this.correctAnswer = correctAnswer;
    }

    //getter+setter
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    //functia caree spune cat de corect e raspunsul
//daca este egal cu correctAnswer ->    CORRECT
//daca nu e egal + diferenat de lungime este maxim 30% din lung rasp corect +unul in contile pe celalat->PARTIAL
// daca nu ->WRONG
    @Override
    public Correctness checkAnswer(String answer) {
        int difference;
        int limit;
        if (answer == null)
            return Correctness.WRONG;

        if (answer.equals(correctAnswer))
            return Correctness.CORRECT;

//difference = answer.length() - correctAnswer.length();
        limit = (int) Math.round(correctAnswer.length() * 0.3);
        int diff = Math.abs(answer.length() - correctAnswer.length());
        if (diff <= limit && (answer.contains(correctAnswer) || correctAnswer.contains(answer))) {
            return Correctness.PARTIAL;
        }
        return Correctness.WRONG;
    }

//returneaza procentul de punctaj->override din interfata
// daca e corecta->return1
//partiala-> return 70%
//wrong->0
@Override
    public double getGrade(Correctness correctness) {
    if (correctness == Correctness.CORRECT)
            return 1.0;
    if (correctness == Correctness.PARTIAL)
            return 0.7;
        return 0.0;
    }

}
