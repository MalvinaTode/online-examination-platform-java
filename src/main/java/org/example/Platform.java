package org.example;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/*
 * Clasa Platform realizeaza comenzile primite de la input
 * Am folosit Map si List folosind  Labul 10, colectii
 */
public class Platform {

    // Map pt nume examen si Exam
    private Map<String, Exam> exams;

    // Map pt nume student si Student
    private Map<String, Student> students;

    // testul curent
    private String test;

    // formatul timp
    private DateTimeFormatter fmt;

    public Platform(String testFolder) {
        this.test = testFolder;

        // Am ales LinkedHashMap ca sa pastrez ordinea de inserare
        this.exams = new LinkedHashMap<>();
       this.students = new LinkedHashMap<>();

       fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
    }

    /*
     * comanda create_exam:
     * creez examenul si in salvez in map
     */
    public void createExam(String name, LocalDateTime start, LocalDateTime end) {
        exams.put(name, new Exam(name, start, end));
    }

    /*
     * comanda add_question:
     * creez o intrebare si o adaug in examen conform tipului ei
     * setez datele specifice, pt fiecare
     */
    public void addQuestion(LocalDateTime timestamp, String type, String examName,
                            String author, int difficulty, double maxScore,
                            String text, String correctAnswer) {

        Exam exam = exams.get(examName);
        Question<?> q = null;

        if (type.equals("open_ended")) {
            q = new OpenEndedQuestion(text, author, timestamp, maxScore, difficulty, correctAnswer);
        } else if (type.equals("fill_in_the_blank")) {
            q = new FillInTheBlankQuestion(text, author, timestamp, maxScore, difficulty, correctAnswer);
        } else if (type.equals("multiple_choice")) {
            //cazul multiplechoice
            q = new MultipleChoiceQuestion(text, author, timestamp, maxScore, difficulty,
                    ResponseOption.valueOf(correctAnswer));
        }
        exam.addQuestion(q);

    }

    /*
     * comanda list_questions_history:
    *printez in fisierul de out intrebarile luate din examen sortate cronologic dupa data
    * in caz de egalitate dupa autor
     */
    public void listQuestionsHistory(LocalDateTime timestamp, String examName) {
        String outName = test + "questions_history_" + examName + "_" + timestamp.format(fmt) + ".out";

        Exam exam = exams.get(examName);
        List<Question<?>> qs = exam.listQuestionsHistory();

        // scrierea in fisier luata din lab8
        try {
            FileWriter w = new FileWriter(outName);

            for (Question<?> q : qs) {

                //rasp corect in functie de intrebare
                String answer;
                if (q instanceof MultipleChoiceQuestion) {
                    answer = ((MultipleChoiceQuestion) q).getCorrectAnswer().toString();
                } else if (q instanceof OpenEndedQuestion) {
                    answer = ((OpenEndedQuestion) q).getCorrectAnswer();
                } else {
                    answer = ((FillInTheBlankQuestion) q).getCorrectAnswer();
                }

                w.write(q.getData().format(fmt) + " | " +
                        q.getText() + " | " +
                        answer + " | " +
                        q.getDifficulty() + " | " +
                        q.getMaxScore() + " | " +
                        q.getName() + "\n");
            }

            w.close();
        } catch (Exception e) {
        }
    }
    /*
     *comanda  print_exam:
     * afisez intreb sortate dupa dificultate si text
     */
    public void printExam(LocalDateTime timestamp, String examName) {
        String outname = test + "print_exam_" + examName + "_" +
                timestamp.format(fmt) + ".out";

        Exam exam = exams.get(examName);
        List<Question<?>> questions = exam.printExamOrder(); //lista de intreb ordonate

        try {
            FileWriter w = new FileWriter(outname);

            for (Question<?> q : questions) {
              //pt fiecare intrebare , extrag rasp corect
                // il convertesc in string daca e enum
                String answer;
                if (q instanceof MultipleChoiceQuestion) {
                    answer = ((MultipleChoiceQuestion) q).getCorrectAnswer().toString();
                } else if (q instanceof OpenEndedQuestion) {
                    answer = ((OpenEndedQuestion) q).getCorrectAnswer();
                } else {
                    answer = ((FillInTheBlankQuestion) q).getCorrectAnswer();
                }

                w.write(q.getMaxScore() + " | " +
                        q.getText() + " | " +
                        q.getDifficulty() + " | " +
                        answer + "\n");
            }

            w.close();
        } catch (Exception e) {
        }
    }

    /*
     * comamda register_student:
     *inregistrez un student nou
     */
    public void registerStudent(String name, String grupa) {
        Student student = new Student(name, grupa);
        students.put(name, student);
    }

    /*
     * comanda submit_exam:
     * trimit rasp studentului
     *daca e in afara intervalului afiseaza mesaj in console.out
     */
    public void submitExam(LocalDateTime timestamp, String examName,
                           String studentName, List<String> answers) {

        //caut examenul in mapa
        Exam exam = exams.get(examName);
        Student student = students.get(studentName);

        try { //calculez scorul/arunc exceptie->apeland submitExam din Exam
            double score = exam.submitExam(studentName, timestamp, answers);
            student.addExamScore(examName, score);  //salvez scorul

        } catch (SubmissionOutsideTimeIntervalException e) {
            student.addExamScore(examName, 0.0);

            try { //scriu msj in fisier, exact cum se cere
                FileWriter w = new FileWriter(test + "console.out", true);
                w.write(timestamp.format(fmt) +
                        " | Submission outside of time interval for student " +
                        studentName + "\n");
                w.close();
            } catch (Exception ex) {
            }
        }
    }

    /*
     * comanda show_student_score:
     * afisez scorul studentului cum se cere
     */
    public void showStudentScore(LocalDateTime timestamp, String studentName, String examName) {
        Exam exam = exams.get(examName);
        double score = exam.getStudentScore(studentName);

        try {
            FileWriter w = new FileWriter(test + "console.out", true);
            w.write(timestamp.format(fmt) +
                    " | The score of " + studentName +
                    " for exam " + examName +
                    " is " + String.format(Locale.US, "%.2f", score) + "\n");
            w.close();
        } catch (Exception e) {

        }
    }

    /*
     * comanda generate_report:
     * scriu in fisier studentii sortati alfabetic
     * am realizat sortarea ca in lab10 cu Collection.sort si Comparator
     */
    public void generateReport(LocalDateTime timestamp, String examName) {
        String filename = test + "exam_report_" + examName + "_" +
                timestamp.format(fmt) + ".out";

        Exam exam = exams.get(examName);
        Map<String, Double> scores = exam.getScores();

        //fac lista cu nume a studentilor
        List<String> names = new ArrayList<>(scores.keySet());

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        try {
            FileWriter w = new FileWriter(filename);

            //scriu scorul cu 2 zecimale
            for (String name : names) {
                w.write(name + " | " + String.format(Locale.US, "%.2f", scores.get(name)) + "\n");
            }

            w.close();
        } catch (Exception e) {
        }
    }

    /*
     * comanda resetConsole:
     * golesc console.out la inceputul fiecarui test
     */
    public void resetConsole() {
        try {
            FileWriter w = new FileWriter(test+ "console.out");
            w.close();
        } catch (Exception e) {
        }
    }
}
