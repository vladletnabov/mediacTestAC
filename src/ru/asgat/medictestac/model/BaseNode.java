package ru.asgat.medictestac.model;

import javax.xml.soap.Node;

/**
 * Created by Skif on 21.08.2017.
 */
public abstract class BaseNode {
    private String idNode; // определяем имя ID элемента и его значение.
    private String nodeName; // определяем имя ноды <nodeName ...>

    public String getIdNode() {
        return idNode;
    }

    public void setIdNode(String idNode) {
        this.idNode = idNode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
