package ru.asgat.medictestac.model;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by Skif on 22.08.2017.
 */
public class XMLstructure {
    //private String rootElementName;
    private String rootName;
    private List<BaseInternalElementsNode> lstNodes;

    public XMLstructure(String name){
        //this.rootElementName = root;
        this.rootName = name;
    }


    public List<BaseInternalElementsNode> getLstNodes() {
        return lstNodes;
    }

    public void setLstNodes(List<BaseInternalElementsNode> lstNodes) {
        this.lstNodes = lstNodes;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }
}
