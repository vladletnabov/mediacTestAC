package ru.asgat.medictestac.model;

import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by Skif on 02.09.2017.
 */
public class TableResultNewQuestion {
    private Long id;
    private String name;
    private String question;
    private String answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setName(String family, String name, String year) {
        this.name = (family + " " + name + ", " + year);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public void setAnswers(List<Answer> answers) {
        String text="";
        for (int i=0; i<answers.size();i++){
            text += (i+1) + ". " + answers.get(i).getText()+"\n";
        }
        this.answers = text;
    }
}
