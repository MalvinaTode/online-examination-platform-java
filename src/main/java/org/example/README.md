# Online Examination Platform (Java)

## Project Overview

The Online Examination Platform is a Java application that manages exams, questions, students, submissions, and student scores. It supports multiple question types, automatic answer evaluation, time-restricted exam submissions, and report generation.

## Data Structures and Design Decisions

### 1. ArrayList — Exam Questions

`ArrayList` is used to store the questions associated with each exam.

**Reasons for choosing `ArrayList`:**
- **Efficient insertion:** `addQuestion()` appends questions to the end of the list, with amortized \(O(1)\) time complexity.
- **Flexible sorting:** Questions must be sorted according to different criteria:
  - `list_questions_history`: chronological order by insertion date, followed by alphabetical order by author.
  - `print_exam`: ascending order by difficulty, followed by alphabetical order by question text.
- **Preserving the original list:** Copies of the question list are sorted using `Collections.sort()` and custom `Comparator` implementations, without modifying the original list. This approach was based on Laboratory 10.
- **Efficient element access:** During `submitExam()`, the sorted list is traversed using indices, providing \(O(1)\) access to individual elements.

### 2. HashMap — Exams and Students

`HashMap` is used in `Platform` to store exams and students, allowing them to be retrieved using unique identifiers.

Average-case \(O(1)\) lookup makes this structure suitable for efficient access.

The implementation follows the concepts covered in Laboratory 10 and supports operations such as:
- `add_question`
- `submit_exam`
- `show_student_score`
- `generate_report`

### 3. HashMap — Student Scores

`HashMap<String, Double>` is used to store exam scores in both the `Student` and `Exam` classes.

This provides efficient average-case \(O(1)\) access to a student's score for a particular exam.

The structure supports:
- `addExamScore()`
- `getStudentScore()`
- `show_student_score`

For `generate_report()`, the student identifiers are extracted from the map, sorted alphabetically using `Collections.sort()`, and traversed to display each student and their score.

## Question Hierarchy

The abstract `Question<T>` class defines the common behavior of all question types. Its generic implementation follows the concepts covered in Laboratories 5 and 9.

Generics allow `checkAnswer(T answer)` to accept different answer types:
- `MultipleChoiceQuestion` extends `Question<ResponseOption>`.
- `OpenEndedQuestion` extends `Question<String>`.
- `FillInTheBlankQuestion` extends `Question<String>`.

### Question Attributes

Each question contains:
- `text`: The question text.
- `name`: The name of the author who added the question.
- `date`: The date when the question was added.
- `maxScore`: The maximum score assigned to the question.
- `difficulty`: The question's difficulty level.

### Main Methods

- `checkAnswer(T answer)`: An abstract method that evaluates the correctness of a submitted answer. Its implementation depends on the question type.
- `getGrade(Correctness correctness)`: A method defined by the `Gradable` interface (Laboratory 5) that returns the score percentage associated with a correctness level.

## Question Types and Grading Rules

### 1. OpenEndedQuestion

Evaluates a free-text answer by comparing it with the expected answer.

| Correctness | Evaluation rule | Score |
|---|---|---:|
| `CORRECT` | The submitted answer exactly matches the expected answer. | 100% (1.0) |
| `PARTIAL` | The answer length is within ±30% of the expected answer's length, and one string contains the other as a substring. | 70% (0.7) |
| `WRONG` | Neither of the above conditions is satisfied. | 0% (0.0) |

### 2. FillInTheBlankQuestion

Evaluates an answer entered into a blank.

| Correctness | Evaluation rule | Score |
|---|---|---:|
| `CORRECT` | The submitted answer exactly matches the expected answer. | 100% (1.0) |
| `PARTIAL` | The answer length differs by at most two characters from the expected answer's length, and one string contains the other. | 50% (0.5) |
| `WRONG` | Neither of the above conditions is satisfied. | 0% (0.0) |

### 3. MultipleChoiceQuestion

The expected answer is represented by the `ResponseOption` enum, which contains the options `A`, `B`, `C`, and `D`.

| Correctness | Evaluation rule | Score |
|---|---|---:|
| `CORRECT` | The submitted option matches the correct option. | 100% (1.0) |
| `WRONG` | The submitted option does not match the correct option. | 0% (0.0) |

## Exam Class

The `Exam` class manages an exam's questions, time interval, and student scores.

### Attributes

- `name`: The exam name.
- `startDate`: The exam start date and time.
- `endDate`: The exam end date and time.
- `questions`: An `ArrayList<Question<?>>` containing the exam questions.
- `scores`: A `HashMap<String, Double>` mapping student identifiers to their scores.

The collections follow the implementation concepts covered in Laboratory 10.

### Main Methods

**`addQuestion()`**

Adds a question to the exam's question list. Appending to an `ArrayList` has amortized \(O(1)\) time complexity.

**`getQuestionsHistory()`**

Returns the questions sorted chronologically by insertion date. If two questions have the same date, they are sorted alphabetically by author name.

The sorting is implemented using `Collections.sort()`, following Laboratory 10.

**`getPrintExam()`**

Returns the questions sorted by ascending difficulty. If two questions have the same difficulty, they are sorted alphabetically by question text.

The original question list is preserved.

**`submitExam()`**

Processes a student's exam submission:
1. Checks whether the submission timestamp falls within the exam's allowed time interval.
2. If the submission is outside the interval, sets the score to zero and throws an exception.
3. Uses `getPrintExam()` to obtain the questions sorted by difficulty.
4. Evaluates the submitted answers using `instanceof` to identify the question type.
5. Stores the resulting score in the `HashMap`.

**`getStudentScore()`**

Retrieves a student's score from the exam's score map.

## Student Class

The `Student` class stores student information and exam scores.

### Attributes

- `name`: The student's name.
- `group`: The student's group.
- `examScores`: A `HashMap<String, Double>` mapping exam identifiers to scores.

### Main Methods

- `addExamScore()`: Adds or updates a student's score for an exam.
- `getScoreForExam()`: Retrieves the student's score for a specific exam.

Using a `HashMap` provides efficient average-case access to exam scores.

## Platform Class

The `Platform` class coordinates exam management, student management, submissions, and report generation.

### Attributes

- `exams`: A `HashMap<String, Exam>` containing all exams.
- `students`: A `HashMap<String, Student>` containing all students.
- `testFolder`: The path to the test folder.
- `fmt`: The timestamp formatting configuration.

### Main Methods

- `createExam()`: Creates an exam.
- `addQuestion()`: Adds a question to an exam.
- `submitExam()`: Handles exam submissions and manages exceptions using `try-catch` blocks, following the concepts covered in Laboratory 7.
- `generateReport()`: Generates a student score report by sorting student identifiers alphabetically using `Collections.sort()` and displaying each student with their score.

## Exception Handling

### `SubmissionOutsideTimeIntervalException`

A custom exception thrown when a student attempts to submit an exam outside its defined time interval.

The exception is handled by the platform to ensure that submissions respect the exam's scheduling constraints.

## Command Processing

Commands are parsed and processed in `Main.java`.

The implementation follows these steps:

1. Reads a line from the input file.
2. Splits the line into tokens.
3. Identifies and processes the command using an `if-else` structure.
4. Closes the files after processing.

File handling follows the syntax and concepts covered in Laboratory 7.

## Concepts and Techniques

The project applies several Java programming concepts and techniques:

- Object-oriented programming and inheritance.
- Abstract classes and interfaces.
- Generics and type-safe question handling.
- Collections: `ArrayList` and `HashMap`.
- Sorting with `Collections.sort()` and custom comparators.
- Exception handling with `try-catch` blocks.
- File input processing and command parsing.
- Exam evaluation and score management.
