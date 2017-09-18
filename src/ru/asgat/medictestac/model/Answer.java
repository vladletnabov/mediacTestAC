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

    /*@Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()){ return false; }
        if (o == this) {
            Answer answer = (Answer) o ;
            if (this.getId().equals(answer.getId())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int textHash = text != null ? text.hashCode() : 0;
        int idHash = getId()!= null ?getId().hashCode() : 0;

        int result = 31 * textHash + idHash;
        return result;

    }*/
}
