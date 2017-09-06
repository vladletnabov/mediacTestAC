package ru.asgat.medictestac.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by Skif on 21.08.2017.
 */
public interface BaseInternalElementsNode {
    public Document setDocumetStructure(Document doc);
    public void setValuesFromDocument(Document doc);
    public void setValuesFromNode(Element node);
}
