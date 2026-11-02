package org.example;

import java.time.LocalDateTime;

public class MultipleChoiceQuestion extends Question<ResponseOption> {

    private ResponseOption correctAnswer;

    public MultipleChoiceQuestion(String text, String name, LocalDateTime data, double maxScore,
                                  int difficulty, ResponseOption correctAnswer) {
        super(text,name,data,maxScore,difficulty);
        this.correctAnswer = correctAnswer;
    }

    //getter+settere
    public ResponseOption getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(ResponseOption correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


//metoda care arata cat de corecta e o intrebare
//daca e egal answer-> CORRECT
//daca nu ->WRONG
    @Override
    public Correctness checkAnswer(ResponseOption answer) {
        if (answer == null)
            return Correctness.WRONG;

        if (answer == correctAnswer)
            return Correctness.CORRECT;

        return Correctness.WRONG;
    }

//metoda care da procentajul in functie de cat de corecta e intrebarea
//daca e corecta->return1, daca nu 0
   @Override
public double getGrade(Correctness correctness) {
    if (correctness == Correctness.CORRECT)
        return 1.0;
    return 0.0;
}

}
