package org.example;

import java.time.LocalDateTime;

public class FillInTheBlankQuestion extends Question<String> {

    private String correctAnswer;

    public FillInTheBlankQuestion(String text, String name, LocalDateTime date,
                                  double maxScore, int difficulty, String correctAnswer) {
        super(text, name, date, maxScore, difficulty);
        this.correctAnswer = correctAnswer;
    }

    //getter+setter
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    //functie pt a arata cat de corecta e o intrebare
    //daca e egal cu answer->CORRECT
    //daca dif de lungime +-2 si e un substring->PARTIAL
    //daca nu e gresit

    @Override
    public Correctness checkAnswer(String answer) {
        int difference;
        if (answer == null)
            return Correctness.WRONG;

        if (answer.equals(correctAnswer))
            return Correctness.CORRECT;

        difference = answer.length() - correctAnswer.length();

        if (difference >= -2 && difference <= 2 &&
                (answer.contains(correctAnswer) || correctAnswer.contains(answer))) {
            return Correctness.PARTIAL;
        }

        return Correctness.WRONG;
    }


    //functie care da procentajul in functie de corectitudinea intrebarii
    //daca e corecta->return1;
    //daca e partiala->50%
    //daca nu return 0
    @Override
    public double getGrade(Correctness correctness) {
        if (correctness == Correctness.CORRECT)
            return 1.0;
        if (correctness == Correctness.PARTIAL)
            return 0.5;
        return 0.0;
    }

}
