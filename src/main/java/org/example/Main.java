package org.example;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* citeste comenzile din main si le trasmite catre Platform pot executare*/
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }

        String testFolder = "src/main/resources/" + args[0]; //folderul testului
        String inputFile = testFolder + "/input.in"; //fisier input

        Platform platform = new Platform(testFolder + "/"); //creex platforma
        platform.resetConsole();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

        try { //deschid fisierul de input fol scanner ca in lab
            Scanner scanner = new Scanner(new File(inputFile));

            //citesc linie cu linie
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.endsWith("exit")) {
                    break;
                }

                //impart linia dupa seprataor
                //elimin spatile
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                LocalDateTime timestamp = LocalDateTime.parse(parts[0], fmt); //timestampul
                String cmd = parts[1]; //comanda

                //iau fiecare comanda pe rand
                if (cmd.equals("create_exam")) {
                    platform.createExam(parts[2],
                            LocalDateTime.parse(parts[3], fmt),
                            LocalDateTime.parse(parts[4], fmt));

                } else if (cmd.equals("add_question")) {
                    platform.addQuestion(timestamp, parts[2], parts[3], parts[4],
                            Integer.parseInt(parts[5]),
                            Double.parseDouble(parts[6]),
                            parts[7], parts[8]);

                } else if (cmd.equals("list_questions_history")) {
                    platform.listQuestionsHistory(timestamp, parts[2]);

                } else if (cmd.equals("print_exam")) {
                    platform.printExam(timestamp, parts[2]);

                } else if (cmd.equals("register_student")) {
                    platform.registerStudent(parts[2], parts[3]);

                } else if (cmd.equals("submit_exam")) {
                    List<String> answers = new ArrayList<>();
                    for (int i = 4; i < parts.length; i++) {
                        answers.add(parts[i]);
                    }
                    platform.submitExam(timestamp, parts[2], parts[3], answers);

                } else if (cmd.equals("show_student_score")) {
                    platform.showStudentScore(timestamp, parts[2], parts[3]);

                } else if (cmd.equals("generate_report")) {
                    platform.generateReport(timestamp, parts[2]);
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}