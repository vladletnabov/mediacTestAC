package ru.asgat.medictestac.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Skif on 02.09.2017.
 */
public class TableResultTesterExam {
    private long id;
    private Date date;
    private String formattedDate;
    private String fioFamily;
    private String fioName;
    private String fioYear;
    private int countQuestions;
    private int countRightQuestions;
    private int countWrongQuestions;
    private int countMaxErrors;
    private boolean autoGenAnswers;
    private int countQuestionsWautoGenAnswers;
    private String result;
    private String wrongQuestions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public void setFormattedDate(Date formattedDate){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = DATE_FORMAT.format(formattedDate);

        this.formattedDate = date;
    }

    public String getFioFamily() {
        return fioFamily;
    }

    public void setFioFamily(String fioFamily) {
        this.fioFamily = fioFamily;
    }

    public String getFioName() {
        return fioName;
    }

    public void setFioName(String fioName) {
        this.fioName = fioName;
    }

    public String getFioYear() {
        return fioYear;
    }

    public void setFioYear(String fioYear) {
        this.fioYear = fioYear;
    }

    public int getCountQuestions() {
        return countQuestions;
    }

    public void setCountQuestions(int countQuestions) {
        this.countQuestions = countQuestions;
    }

    public int getCountRightQuestions() {
        return countRightQuestions;
    }

    public void setCountRightQuestions(int countRightQuestions) {
        this.countRightQuestions = countRightQuestions;
    }

    public int getCountWrongQuestions() {
        return countWrongQuestions;
    }

    public void setCountWrongQuestions(int countWrongQuestions) {
        this.countWrongQuestions = countWrongQuestions;
    }

    public boolean isAutoGenAnswers() {
        return autoGenAnswers;
    }

    public void setAutoGenAnswers(boolean autoGenAnswers) {
        this.autoGenAnswers = autoGenAnswers;
    }

    public int getCountQuestionsWautoGenAnswers() {
        return countQuestionsWautoGenAnswers;
    }

    public void setCountQuestionsWautoGenAnswers(int countQuestionsWautoGenAnswers) {
        this.countQuestionsWautoGenAnswers = countQuestionsWautoGenAnswers;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCountMaxErrors() {
        return countMaxErrors;
    }

    public void setCountMaxErrors(int countMaxErrors) {
        this.countMaxErrors = countMaxErrors;
    }

    public String getWrongQuestions() {
        return wrongQuestions;
    }

    public void setWrongQuestions(String wrongQuestions) {
        this.wrongQuestions = wrongQuestions;
    }
}
