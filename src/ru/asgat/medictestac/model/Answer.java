package ru.asgat.medictestac.model;

/**
 * Created by Skif on 31.08.2017.
 */
public class Answer {
    private String id;
    private String text;

    public Answer(){
        this.id = "";
        this.text = "";
    }

    public Answer(String id, String text){
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
