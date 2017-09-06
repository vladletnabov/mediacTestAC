package ru.asgat.medictestac.model;

import org.w3c.dom.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by Skif on 21.08.2017.
 */
public class Settings extends BaseNode implements BaseInternalElementsNode {
    private String hostname;
    private HashMap<String, Boolean> proto;
    private boolean autoAnswer;
    private double minTimout;
    private double maxTimout;
    private double coincedence;
    final private int deltaTimout = 10; // разница между минимальным и максимальным значениеми в секундах
    private static final Logger logger = Logger.getLogger( XMLReader.class.getName() );

    public Settings() {
        this.minTimout = 5;
        this.maxTimout = minTimout + deltaTimout;
        this.autoAnswer = false;
        this.proto = new HashMap<String, Boolean>();
        this.proto.put("http", true);
        this.proto.put("https", false);
        this.hostname = "http://hostname.ru";
        this.coincedence = 70;
        setIdNode("");
        setNodeName("setting");
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public HashMap<String, Boolean> getProto() {
        return proto;
    }

    public void setProto(HashMap<String, Boolean> proto) {
        this.proto = proto;
    }

    public boolean isAutoAnswer() {
        return autoAnswer;
    }

    public boolean getAutoAnswer() {
        return this.autoAnswer;
    }

    public void setAutoAnswer(boolean autoAnswer) {
        this.autoAnswer = autoAnswer;
    }

    public double getMinTimout() {
        return minTimout;
    }

    public void setMinTimout(double minTimout) {
        this.minTimout = minTimout;
        if (this.minTimout > (this.maxTimout - deltaTimout)) {
            setMaxTimout(minTimout + deltaTimout);
        }
    }

    public double getMaxTimout() {
        return maxTimout;
    }

    public void setMaxTimout(double maxTimout) {
        this.maxTimout = maxTimout;
        if (this.minTimout > (this.maxTimout - deltaTimout)) {
            this.maxTimout = minTimout + deltaTimout;
        }
    }

    public double getCoincedence() {
        return coincedence;
    }

    public void setCoincedence(double coincedence) {
        this.coincedence = coincedence;
    }

    @Override
    public Document setDocumetStructure(Document doc) {
        logger.info("setDocumetStructure");
        logger.info("----------------------------------------------------");
        logger.info("PARENT: "+ doc.getFirstChild().getNodeName());
        if(doc.getFirstChild().getChildNodes()!=null){
            logger.info("doc.getFirstChild().getChildNodes()!=null");
            logger.info("doc.getFirstChild().getChildNodes().getLength() = " + doc.getFirstChild().getChildNodes().getLength());
            /*for (int ind = 0; ind < doc.getFirstChild().getChildNodes().getLength(); ind++){
                // удаляем все старые ноды, чтобы с ними не было мороки в дальнейшем
                logger.info("\n" + ind + "\n");
                logger.warning("try to remove node: " + doc.getFirstChild().getChildNodes().item(ind).getNodeName() +
                        " value " + doc.getFirstChild().getChildNodes().item(ind).getTextContent()+
                        " in " + doc.getFirstChild().getNodeName());
                doc.getFirstChild().removeChild((Element) doc.getFirstChild().getChildNodes().item(ind));
                logger.warning("cur ind: " + ind);

            }*/

            while (doc.getFirstChild().getChildNodes().getLength()>0){
                doc.getFirstChild().removeChild((Element) doc.getFirstChild().getChildNodes().item(0));
            }

            logger.info("END foreach");
        }
        else{
            logger.info("doc.getFirstChild().getChildNodes()==null");
        }
        logger.info("----------------------------------------------------");
        Element settingNode = doc.createElement(getNodeName());
        settingNode.setAttribute("id", getIdNode());

        Element node = doc.createElement("hostname");
        node.setTextContent(getHostname());
        settingNode.appendChild(node);

        node = doc.createElement("autoanswer");
        node.setTextContent(String.valueOf(getAutoAnswer()));
        settingNode.appendChild(node);

        node = doc.createElement("mintimout");
        node.setTextContent(String.valueOf(getMinTimout()));
        settingNode.appendChild(node);

        node = doc.createElement("maxtimout");
        node.setTextContent(String.valueOf(getMaxTimout()));
        settingNode.appendChild(node);


        node = doc.createElement("coincedence");
        node.setTextContent(String.valueOf(getCoincedence()));
        settingNode.appendChild(node);

        node = doc.createElement("protocols");
        for(Map.Entry<String, Boolean> entry : getProto().entrySet()) {
            String key = entry.getKey();
            boolean value = entry.getValue();
            logger.info("PROTO: key: " + key + " value: " + String.valueOf(value));
            if(key.toLowerCase().contains("#")==false){
                Node protoNode = doc.createElement(key);
                protoNode.setTextContent(String.valueOf(value));
                node.appendChild(protoNode);
            }
        }

        settingNode.appendChild(node);

        doc.getFirstChild().appendChild(settingNode);
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
                case "hostname":
                    setHostname(currentNode.getTextContent());
                    break;
                case "autoanswer":
                    setAutoAnswer(Boolean.valueOf(currentNode.getTextContent()));
                    break;
                case "mintimout":
                    setMinTimout(Double.valueOf(currentNode.getTextContent()));
                    break;
                case "maxtimout":
                    setMaxTimout(Double.valueOf(currentNode.getTextContent()));
                    break;
                case "coincedence":
                    setCoincedence(Double.valueOf(currentNode.getTextContent()));
                    break;
                case "protocols":
                    NodeList protoList = currentNode.getChildNodes();
                    if (protoList!=null){
                        for(int pInd = 0; pInd< protoList.getLength(); pInd++){
                            if (node.getNodeType()==Node.ELEMENT_NODE) {
                                getProto().put(protoList.item(pInd).getNodeName(), Boolean.valueOf(protoList.item(pInd).getTextContent()));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public double getCurrentTimout(){
        double max = getMaxTimout();
        double min = getMinTimout();
        max -= min;
        return (double) (Math.random() * ++max) + min;
    }
}

