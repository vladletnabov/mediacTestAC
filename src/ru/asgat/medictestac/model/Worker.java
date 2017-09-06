package ru.asgat.medictestac.model;

import java.util.List;

/**
 * Created by Skif on 21.08.2017.
 */
public class Worker {
    private String family;
    private String name;
    private String year;
    private int questionCount;
    private int questionCountError;
    private int questionCountSuccess;
    private List<Question> askedQuestions;
    private List<Question> newQuestions;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getQuestionCountError() {
        return questionCountError;
    }

    public void setQuestionCountError(int questionCountError) {
        this.questionCountError = questionCountError;
    }

    public int getQuestionCountSuccess() {
        return questionCountSuccess;
    }

    public void setQuestionCountSuccess(int questionCountSuccess) {
        this.questionCountSuccess = questionCountSuccess;
    }

    public List<Question> getAskedQuestions() {
        return askedQuestions;
    }

    public void setAskedQuestions(List<Question> askedQuestions) {
        this.askedQuestions = askedQuestions;
    }

    public List<Question> getNewQuestions() {
        return newQuestions;
    }

    public void setNewQuestions(List<Question> newQuestions) {
        this.newQuestions = newQuestions;
    }
}
