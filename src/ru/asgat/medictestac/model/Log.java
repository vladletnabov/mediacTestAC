package ru.asgat.medictestac.model;

import com.sun.javafx.collections.ElementObservableListDecorator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Skif on 22.08.2017.
 */
public class Log extends BaseNode  implements BaseInternalElementsNode {
    private String text;
    private Date date;
    private Worker tester;

    public Log(){
        this.date = new Date();
        setIdNode("");
        setNodeName("log");
        this.setText("");
    }

    @Override
    public Document setDocumetStructure(Document doc) {
        //Node rootElement = doc.getFirstChild();

        Element nodeLog = (Element) doc.createElement(getNodeName());
        nodeLog.setAttribute("id",getIdNode());
        nodeLog.setAttribute("date", getFormattedDate());

        Element tester = (Element) doc.createElement("tester");
        tester.setAttribute("family", getTester().getFamily());
        tester.setAttribute("name", getTester().getName());
        tester.setAttribute("year", getTester().getYear());
        nodeLog.appendChild(tester);

        Element question = (Element) doc.createElement("testresult");
        question.setAttribute("exam-questions", String.valueOf(getTester().getQuestionCount()));
        question.setAttribute("error-questions", String.valueOf(getTester().getQuestionCountError()));
        question.setAttribute("success-questions", String.valueOf(getTester().getQuestionCountSuccess()));

        Node logText = doc.createElement("debugtext");
        logText.setTextContent(getText());
        question.appendChild(logText);

        Element askedquestions = (Element) doc.createElement("askedquestions");
        if (getTester().getAskedQuestions().size()>0){
            for(int i=0; i<getTester().getAskedQuestions().size(); i++){
                Element askedquestion = (Element) doc.createElement("question");
                askedquestion.setAttribute("id", String.valueOf(i));
                Element elementText = (Element) doc.createElement("text");
                elementText.setTextContent(getTester().getAskedQuestions().get(i).getText());
                askedquestion.appendChild(elementText);

                Node answers = doc.createElement("answers");
                for(Answer answer: getTester().getAskedQuestions().get(i).getAnswers()){
                    Element element = (Element) doc.createElement("answer");
                    element.setAttribute("id",answer.getId());
                    element.setTextContent(answer.getText());
                    answers.appendChild(element);
                }
                askedquestion.appendChild(answers);

                askedquestions.appendChild(askedquestion);
            }
        }

        question.appendChild(askedquestions);

        Element newquestions = (Element) doc.createElement("newquestions");
        for(int i=0; i<getTester().getNewQuestions().size(); i++){
            Element newquestion = (Element) doc.createElement("question");
            newquestion.setAttribute("id", String.valueOf(i));
            Element elementText = (Element) doc.createElement("text");
            elementText.setTextContent(getTester().getNewQuestions().get(i).getText());
            newquestion.appendChild(elementText);

            Node answers = doc.createElement("answers");
            for(Answer answer: getTester().getNewQuestions().get(i).getAnswers()){
                Element element = (Element) doc.createElement("answer");
                element.setAttribute("id",answer.getId());
                element.setTextContent(answer.getText());
                answers.appendChild(element);
            }
            newquestion.appendChild(answers);

            newquestions.appendChild(newquestion);
        }
        question.appendChild(newquestions);
        nodeLog.appendChild(question);

        doc.getFirstChild().appendChild(nodeLog);

        return doc;
    }

    public String getFormattedDate(){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:SS");
        String date = DATE_FORMAT.format(getDate());

        return date;
    }

    @Override
    public void setValuesFromDocument(Document doc) {

    }

    @Override
    public void setValuesFromNode(Element node) {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Worker getTester() {
        return tester;
    }

    public void setTester(Worker tester) {
        this.tester = tester;
    }
}
