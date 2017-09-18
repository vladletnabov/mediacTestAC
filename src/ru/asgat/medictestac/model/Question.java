package ru.asgat.medictestac.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Skif on 21.08.2017.
 */
public class Question extends BaseNode implements BaseInternalElementsNode{ //implements BaseInternalElementsNode {
    /*private int id;
    private String questionGroup;*/
    private String text;
    //private Map<String, List<GroupedElement>> groups; // (group name), (list element of group)
    private List<Answer> answers;

    private static final Logger logger = Logger.getLogger( XMLReader.class.getName() );
    //private List<GroupedElement> groups; //Временно отключаем
    /*
    * <question id=""> --- class BaseNode
    *   <text>...</text>
    *   <answers>
    *       <answer id="...">...</answer>
    *       <answer id="...">...</answer>
    *   </answers>
    *   <groups>
    *       <group id="...">...</group>
    *       <group id="...">...</group>
    *   </answers>
    * </question>
    * */

    public Question(){
        setIdNode("");
        setNodeName("question");
        this.answers = new ArrayList<Answer>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /*public Map<String, List<GroupedElement>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<GroupedElement>> groups) {
        this.groups = groups;
    }*/

    @Override
    public Document setDocumetStructure(Document doc) {
        logger.info("setDocumetStructure");
        logger.info("!----------------------------------------------------!");
        logger.info("PARENT: "+ doc.getFirstChild().getNodeName());
        //boolean create questio
        /*if(doc.getFirstChild().getChildNodes()!=null){
            logger.info("doc.getFirstChild().getChildNodes()!=null");
            logger.info("doc.getFirstChild().getChildNodes().getLength() = " + doc.getFirstChild().getChildNodes().getLength());

            while (doc.getFirstChild().getChildNodes().getLength()>0){
                doc.getFirstChild().removeChild((Element) doc.getFirstChild().getChildNodes().item(0));
            }
        }
        else{
            logger.info("doc.getFirstChild().getChildNodes()==null");
        }*/

        /*for(int ind=0; ind < doc.getFirstChild().getChildNodes().getLength(); ind++){
            Node question = doc.getFirstChild().getChildNodes().item(ind);
            for(int chInd=0; chInd< question.getChildNodes().getLength(); chInd++){
                if (question.getChildNodes().item(chInd).getNodeType()==Node.ELEMENT_NODE) {
            }
        }*/
        int createNode = -1;
        /*NodeList questionText = doc.getElementsByTagName("text");
        for(int qtInd=0; qtInd< questionText.getLength(); qtInd++){
            if (compareTextByNeuroNetAlgorithm(getText(), questionText.item(qtInd).getTextContent())){
                createNode = qtInd;
                break;
            }
        }*/

        if (createNode> -1){
            //logger.info("question exist! nodename: " + questionText.item(createNode).getParentNode().getNodeName());
        }
        else{
            logger.info("----------------------------------------------------");
            Element questionNode = doc.createElement(getNodeName());
            questionNode.setAttribute("id", getIdNode());

            logger.info("set text fo question node: " + getText());
            Element textQuestion = doc.createElement("text");
            textQuestion.setTextContent(getText());
            questionNode.appendChild(textQuestion);

            Element answersNode = doc.createElement("answers");
            for(Answer answer: getAnswers()) {
                Element answerNode = doc.createElement("answer");
                answerNode.setAttribute("id", "");
                answerNode.setTextContent(answer.getText());
                answersNode.appendChild(answerNode);
            }

            questionNode.appendChild(answersNode);

            doc.getFirstChild().appendChild(questionNode);
        }


        return doc;
    }

    @Override
    public void setValuesFromDocument(Document doc) {

    }

    @Override
    public void setValuesFromNode(Element node) {
        setNodeName(node.getNodeName());
        setIdNode(node.getAttribute("id"));
        for(int ind=0; ind <node.getChildNodes().getLength(); ind++){
            Node currentNode = node.getChildNodes().item(ind);

            switch (currentNode.getNodeName().toLowerCase()) {
                case "text":
                    setText(currentNode.getTextContent());
                    break;
                case "answers":
                    logger.info("reading answers....");
                    NodeList answerList = currentNode.getChildNodes();
                    logger.info("answer count: " + answerList.getLength());
                    if (answerList!=null){
                        for(int pInd = 0; pInd< answerList.getLength(); pInd++){
                            Node answerNode = answerList.item(pInd);
                            if (answerNode.getNodeType()==Node.ELEMENT_NODE) {
                                logger.info("ANSWER!");
                                logger.info("nodeName: " + answerNode.getNodeName());
                                if(answerNode.getNodeName().compareTo("answer")==0){
                                    logger.warning("ADDED!");
                                    String answerID = ((Element)answerNode).getAttribute("id");
                                    Answer answer = new Answer(answerID, answerNode.getTextContent());
                                    getAnswers().add(answer);
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean compareTextByNeuroNetAlgorithm(String str1, String str2){
        /*
        *
        * Временная заглушка для алгоритма сравнения
        *
        * */

        boolean result = false;

        if(str1.compareTo(str2)==0){
            result = true;
        }

        return result;
    }

    public void setValueFromHtmlNode(Node node){
        logger.info(node.toString());
        if (node!=null){
            for(int i=0; i<node.getChildNodes().getLength(); i++){
                //logger.warning("............................................................");
                switch (((Element)node.getChildNodes().item(i)).getAttribute("class")){
                    case "heading":
                        //logger.info("v-team-member-info");
                        NodeList textNodes = node.getChildNodes().item(0).getChildNodes().item(i).getChildNodes();
                        for(int indText =0; indText< textNodes.getLength(); indText++){
                            if (textNodes.item(indText).getNodeName().compareTo("span")==0){
                                Element textQuestion = (Element) textNodes.item(indText);
                                //logger.warning("SET TEXT for question: " + textQuestion.getTextContent());
                                setText(textQuestion.getTextContent().replaceAll("\n",""));
                            }
                        }
                        break;
                    case "v-team-member-desc":
                        //logger.info("v-team-member-desc");
                        NodeList answerNodes = node.getChildNodes().item(i).getChildNodes();
                        List<Answer> listAnswers = new ArrayList<Answer>();
                        for(int indAns =0; indAns< answerNodes.getLength(); indAns++){
                            //if (answerNodes.item(indAns).getNodeName().compareTo("span")==0){}
                            Element answerR_O = (Element)answerNodes.item(indAns);
                            if(answerR_O.getNodeName().compareTo("div")==0){
                                //logger.info(answerR_O.getNodeName() + " class=" + answerR_O.getAttribute("class"));

                                Element checkboxClass = (Element) answerR_O.getChildNodes().item(0);
                                //logger.info(checkboxClass.getNodeName() + " class=" + checkboxClass.getAttribute("class"));
                                NodeList listTextAnswer = checkboxClass.getChildNodes();
                                //logger.info("listTextAnswer: " + listTextAnswer.getLength());
                                //logger.info("listTextAnswer: " + listTextAnswer.item(0).getNodeName());
                                for (int indRO=0; indRO<checkboxClass.getChildNodes().getLength(); indRO++){
                                    //logger.info(((Element)checkboxClass.item(0)).getAttribute("class"));

                                    Element answerNode = (Element) checkboxClass.getChildNodes().item(indRO);
                                    ///logger.warning("answerNode: " + answerNode.getNodeName() );
                                    if (answerNode.getNodeName().compareTo("label")==0){
                                        Answer quesAnswer = new Answer();
                                        //logger.info("SET ANSWER for question: " + answerNode.getTextContent());
                                        quesAnswer.setText(answerNode.getTextContent());
                                        quesAnswer.setId(answerNode.getAttribute("for"));
                                        listAnswers.add(quesAnswer);
                                    }
                                }
                            }

                        }

                        setAnswers(listAnswers);

                        break;
                    default:
                        break;
                }

            }
        }
    }

    public int getCurrentRandom(int min, int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    /*@Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()){ return false; }
        if (o == this) {
            Question question = (Question) o ;
            if (this.getIdNode().equals(question.getIdNode())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int textHash = text != null ? text.hashCode() : 0;
        int idHash = getIdNode()!= null ?getIdNode().hashCode() : 0;
        int answerHash = answers!= null ? answers.hashCode() : 0;

        int result = 31 * textHash + idHash + answerHash;
        return result;

    }*/
}
