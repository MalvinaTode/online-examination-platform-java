package org.example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * simuleaza un examen :
 * nume, interval de timp, lista de intrebari si scoruruile studentilor
 * Am folosit List si Map dupa lab10-colectii
 *implementarile de linkedlist si linkedhashmap sunt luate din lab 3
 */
public class Exam {

    // nume examen
    private String nume;

    //intervalul de timp pt examen
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // lista de intrebari ale examenului
    private List<Question<?>> questions;

    // map intre nume student si scor
    private Map<String, Double> scores;

    //constructorul
    public Exam(String nume, LocalDateTime startDate, LocalDateTime endDate) {
        this.nume = nume;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questions = new LinkedList<>();
        this.scores = new LinkedHashMap<>();
    }

    //adauga intrebare la examen
    public void addQuestion(Question<?> q) {
        questions.add(q);
    }

    /*
     * folosit pt comanda list_questions_history
     *sortez intreb dupa data si dupa numele autorului
     *am fct sortarea cu  Collections.sort si Comparator, luata din labul10-Colectii
     */
    public List<Question<?>> listQuestionsHistory() {
        List<Question<?>> copy = new LinkedList<>(questions);

        Collections.sort(copy, new Comparator<Question<?>>() {
            @Override
            public int compare(Question<?> q1, Question<?> q2) {
                int c = q1.getData().compareTo(q2.getData());
                if (c != 0) {
                    return c;
                }
                return q1.getName().compareTo(q2.getName());
            }
        });

        return copy;
    }

    /*
     * fol pt comanda  print_exam.
     * sortez  intrebarile dupa dificultate si apoi dupa text,
     * implementare folosita din lab10-colectii
     * daca sunt egale dificultatile compar dupa text daca nu dupa dificultate
     */
    public List<Question<?>> printExamOrder() {
        List<Question<?>> copy = new LinkedList<>(questions);

        Collections.sort(copy, new Comparator<Question<?>>() {
            @Override
            public int compare(Question<?> q1, Question<?> q2) {
                if (q1.getDifficulty() != q2.getDifficulty()) {
                    return q1.getDifficulty() - q2.getDifficulty();
                }
                return q1.getText().compareTo(q2.getText());
            }
        });

        return copy;
    }

    /*
     * fol pt comanda submit_exam.
     * verific daca studentul e in interval si ii calculez scorul
     * arunc exceptia daca trebuie
     */
    public double submitExam(String studentName, LocalDateTime timestamp, List<String> answers)
            throws SubmissionOutsideTimeIntervalException {

        // verific daca timetamp e in interval, daca nu arunc exceptia
            boolean before = timestamp.isBefore(startDate);
            boolean after = timestamp.isAfter(endDate);

            if (before || after) {
                throw new SubmissionOutsideTimeIntervalException();
            }

            //lista intrebarilor in ordinea date la examen
            List<Question<?>> ordered = printExamOrder();
            double total; //variabila pentru scor
            int i;
            total = 0.0;
     /*pt fiecare intrebare si rasp parcurg
iau fiecare intrebare si raspuns
 */
        for ( i = 0; i < ordered.size(); i++) {
            Question<?> q = ordered.get(i);
            String answer = answers.get(i);

            Correctness c = Correctness.WRONG;
            //presupun initial ca rasp e gresit

            // pun raspunsul in functie de tipul intrebarii
            if (q instanceof MultipleChoiceQuestion) {
                //pt multiplechoice transform stringul in enum ResponseOption
                MultipleChoiceQuestion m = (MultipleChoiceQuestion) q; //fac downcast ca sa pot apela checkanswer
                ResponseOption opt = ResponseOption.valueOf(answer); //convertesc stringul in rasp
                c = m.checkAnswer(opt);
            } else if (q instanceof OpenEndedQuestion) {
                //pt openended verific direct stringul
                OpenEndedQuestion o = (OpenEndedQuestion) q;
                c = o.checkAnswer(answer);
            } else if (q instanceof FillInTheBlankQuestion) {
                //la fel pt fill in the blank,direct stringul
                FillInTheBlankQuestion f = (FillInTheBlankQuestion) q;
                c = f.checkAnswer(answer);
            }

            // adaug punctajul la scor
            total += q.getMaxScore() * q.getGrade(c);
        }

        scores.put(studentName, total);
        return total;
    }

    /*
     * functie care returneaza scorul studentului
     */
    public double getStudentScore(String studentName) {
        if (scores.containsKey(studentName)) {
            return scores.get(studentName);
        }
        return 0.0;
    }

    /*
     * return la mapul cu scoruri
     */
    public Map<String, Double> getScores() {
        return scores;
    }

    public String getNume() {
        return nume;
    }
}
