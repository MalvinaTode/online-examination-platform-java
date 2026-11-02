Alegerea colectilor:

1)Am folosit ArrayList pt a stoca intrebarile fiecraui examen deoarece:
-Operatia addQuestion() se executa in O(1), intrebarile fiind adaugate doar la sfarsitul listei
-flexibilitatea la sortare:
  --list_questions_history-sortare cronologica dupa data adaugarii, apoi alfabetic dupa autor
  --print_exam: sortare duupa dificultate crescator,apoi alfabetic dupa text

-Am creeat copii ale listei si le am sortat folosind Collectios.sort() cu Comparator, fara a modifica lista originala, implemntare luata din lab10
-cand verific rasp in submitExam(), parcurg lista sortata, cu acces O1() la fiecare element prin index

2)Am fol HashMap pt a stoca examenle si studentii deoarece ma ajuta sa am un acces rapid la elemente pe baza unui identificator unic
Accesul O1() este ideal pt eficientizare
Implementarea HashMap am luat-o din Laboratorul10->pt studenti in Platform.java pt operatile:
 --add_question
 --submit_exam
 --show_student_score
 --generate_report

3)Am fol pt stocarea scorurilor studentilor tot HashMap in clasa Student cat si in Exam
datorita complexitatii O(1),care ma ajuta sa am un acces rapid la scorul pt un anumit examen
Aceasta eficientizeaza operatiile:addExamScore,getStudentScore,show_student_score

Pentru generate_report ma ajuta deoarece, extrag cheile din HashMap, le sortez alfabetic fol Collections,sort(), parcurg lista sortata si afisez fiecare student + scorul sau

QUESTION SI CLASELE DERIVATE:

clasa abstracta Question, implementata dupa laboratorul5 si laboratorul9, folosind genericitatea:
-permite metodei checkAnswer(T answer), sa accepte tipuri diferite de raspunsuri
-MultipleChoiceQuestion fol Question<ResponseOption>
-OpenEndedQuestion si FillInTheBlankQuestion folosesc Question<String>

Atributele clasei :
text: textul intrebarii
name: numele autorului care a adaugat intrebarea
data: data la care s-a adaugat intrebarea
maxScore: punctajul maxim asociat intrebarii
difficulty: dificultatea intrebarii

Metode principale:
--checkAnswer(T answer): metoda abstracta (din Lab 5) care verifica corectitudinea unui raspuns
Implementarea difera in functie de tipul intrebarii
--getGrade(Correctness correctness): metoda din interfata Gradable (Lab 5), returneaza procentul de punctaj in functie de corectitudine

Clasele derivate: OpenEndedQuestion, FillInTheBlankQuestion, MultipleChoiceQuestion:

1) OpenEndedQuestion:

Verifica raspunsul:

CORRECT: daca raspunsul este exact egal cu raspunsul corect
PARTIAL: daca lungimea raspunsului este in intervalul ±30% fata de raspunsul corect si unul contine pe celalalt ca substring
WRONG: in rest


Punctaj:

CORRECT: 100% (1.0)
PARTIAL: 70% (0.7)
WRONG: 0% (0.0)

2) FillInTheBlankQuestion:

Verifica raspunsul:

CORRECT: daca raspunsul este exact egal cu raspunsul corect
PARTIAL: daca lungimea raspunsului este in intervalul ±2 litere fata de raspunsul corect si unul contine pe celalalt
WRONG: in rest


Punctaj:

CORRECT: 100% (1.0)
PARTIAL: 50% (0.5)
WRONG: 0% (0.0)

3) MultipleChoiceQuestion:

Raspunsul corect este de tip ResponseOption (enum: A, B, C, D)
verif raspunsul:

CORRECT: daca raspunsul este egal cu optiunea corecta
WRONG: altfel


Punctaj:

CORRECT: 100% (1.0)
WRONG: 0% (0.0)


CLASA EXAM:
Atribute:

-nume: numele examenului
-startDate: data de inceput a examenului
-endDate: data de final a examenului 
-questions: lista de intrebari (ArrayList<Question<?>> - implementare din Lab 10)
-scores: scorurile studentilor (HashMap<String, Double> - implementare din Lab 10)

Metode principale:
--addQuestion
Aceasta metoda adauga o intrebare in lista de intrebari. Complexitate O(1)

--getQuestionsHistory
Sortare cu Collections.sort() dupa Laborator 10
Aceasta metoda returneaza intrebarile sortate cronologic dupa data adaugarii, iar in caz de egalitate, alfabetic dupa numele autorului

--getPrintExam
Sortare cu Collections.sort() dupa Laborator 10
Aceasta metoda returneaza intrebarile sortate dupa dificultate crescator, iar in caz de egalitate, alfabetic dupa textul intrebarii

--submitExam
Aceasta metoda permite unui student sa dea examenul:
-verific daca se afla timestampul in intervalul de timp, daca nu setez scorul la ) si arunc exceptia
-folosesc getprintexam pt a obt lista sortata dupa dificultate
-apoi verific raspunsul folosind instanceof
-salvez scorul in HashMap

--getStudentScore
Returnez scorul unui student din HashMap


CLASA STUDENT:
Atribute:

-name: numele studentului
-group: grupa studentului
-examScores: scorurile la examene (HashMap<String, Double>)

Metode:
--addExamScore
--getScoreForExam
folosind HashMap, ajung sa eficientizez aceste metode

CLASA PLATFORM:
Atribute:

-exams: HashMap<String, Exam> cu toate examenele
-students: HashMap<String, Student> cu toti studentii
-testFolder: calea catre folderul de test
-fmt: format pentru timestamp-uri

Metode principale:
--createExam
--addQuestion
--submitExam- gestionarea exceprilot cu blocuri try-catch, implemnetare luata din laboratorul7
--generateReport-sortare cu Collection.sort()


EXCEPTII IMPLEMENTATE:
SubmissionOutsideTimeIntervalException-este aruncata cand un student incearca sa dea examen in afara intervalului de timp definit pentru examen


Cum functioneaza prelucrarea comenzilor
Parsarea comenzilor in Main.java

1)Citesc linia din fisier si o impart in tokenuri
2)Analizez comanda cu if-else
3_inchid fisierele- implemenatrea fisierelor, am folosit sintaxa din Laborator7
