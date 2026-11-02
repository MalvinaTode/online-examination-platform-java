package org.example;

public class SubmissionOutsideTimeIntervalException extends Exception {

    // construcctor fara paramterii dupa lab7->exceptii
    public SubmissionOutsideTimeIntervalException() {
        super("Submission outside of time interval.");
    }

    // constructor cu mesaj
    public SubmissionOutsideTimeIntervalException(String studentName) {
        super("Submission outside of time interval for student " + studentName);
    }
}
