package ru.asgat.medictestac.model;

import org.apache.log4j.lf5.Log4JLogRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.*;
import java.util.regex.*;

import org.apache.log4j.*;

/**
 * Created by Skif on 21.08.2017.
 */
public class XMLReader {
    private File file;
    private String fileName;
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private XMLstructure xmlStructure;
    private String subDir;
    private String rootFolder; // название корневой папки
    private String rootPath; // путь к расположению файлов данных
    List<String> lstBasePaths;
    final private String dirUsers = "\\Users\\";
    final private String appData = "AppData\\Roaming";
    private static final Logger logger = Logger.getLogger( XMLReader.class.getName() );
    private boolean optionalSetDoc = true; // уставливать переменную doc

    public XMLReader(String fileName, String subDir, String rootElementName){
        //logger.info("start constructor 0");
        this.fileName = fileName;
        this.lstBasePaths = new ArrayList<String>();
        this.lstBasePaths.add("user.home");
        this.lstBasePaths.add("user.dir");
        this.rootFolder = "medicTest";
        this.subDir = subDir;
        this.xmlStructure = new XMLstructure(rootElementName);
        initDataFolders();
        existFile((getRootPath()+"\\"+ subDir + "\\" + fileName), true);
        genDocOnCreate();
    }

    public XMLReader(String fileName, String rootElementName){
        //logger.info("start constructor 1");
        this.fileName = fileName;
        this.lstBasePaths = new ArrayList<String>();
        this.lstBasePaths.add("user.home");
        this.lstBasePaths.add("user.dir");
        this.rootFolder = "medicTest";
        this.subDir = "";
        this.xmlStructure = new XMLstructure(rootElementName);
        initDataFolders();
        existFile((getRootPath() + "\\" + fileName), true);
        genDocOnCreate();
    }

    public XMLReader(String docAsString){
        Date date = new Date();
        String millis = String.valueOf(date.getTime());
        this.fileName = "questionsFromHTMLrequest-" + millis + ".xml";
        this.lstBasePaths = new ArrayList<String>();
        this.lstBasePaths.add("user.home");
        this.lstBasePaths.add("user.dir");
        this.rootFolder = "medicTest";
        this.subDir = "logs";
        initDataFolders();
        existFile((getRootPath()+"\\"+ subDir + "\\" + fileName), true);

        this.docFactory = DocumentBuilderFactory.newInstance();
        this.docFactory.setIgnoringElementContentWhitespace(true);
        createDocumentBuilder();
        Document doc = genDoc(docAsString);
        setDoc(doc);
        writeXML();
    }
    public XMLReader(String docAsString, Boolean result ){
        Date date = new Date();
        String millis = String.valueOf(date.getTime());
        this.fileName = "resultFromHTMLrequest-" + millis + ".xml";
        this.lstBasePaths = new ArrayList<String>();
        this.lstBasePaths.add("user.home");
        this.lstBasePaths.add("user.dir");
        this.rootFolder = "medicTest";
        this.subDir = "logs";
        initDataFolders();
        existFile((getRootPath()+"\\"+ subDir + "\\" + fileName), true);

        this.docFactory = DocumentBuilderFactory.newInstance();
        this.docFactory.setIgnoringElementContentWhitespace(true);
        createDocumentBuilder();
        Document doc = genDoc(docAsString);
        setDoc(doc);
        writeXML();
    }

    public void genDocOnCreate(){
        //logger.info("start genDocOnCreate()");
        this.docFactory = DocumentBuilderFactory.newInstance();
        this.docFactory.setIgnoringElementContentWhitespace(true);
        //logger.info("genDocOnCreate()" + this.docFactory.toString());
        createDocumentBuilder();
        genDoc();
    }

    public void initDataFolders(){
        //logger.info("initDataFolders()");
        checkRootPath();
        createDir(getRootPath());
        if (getSubDir().contentEquals("")){
            createDir(getRootPath());
        }
        else{
            createDir(getRootPath()+"\\"+getSubDir());
        }
    }

    public String checkRootPath(){
        //logger.info("checkRootPath()");
        String base = "user.home";
        String rootPath = System.getProperty(base) + "\\" + appData + "\\" + rootFolder;
        if (existDir(rootPath)){
            setRootPath(rootPath);
            return rootPath;
        }
        base = "user.dir";
        if (existDir(System.getProperty(base))){
            setRootPath(rootPath);
            return rootPath;
        }
        createDir(rootPath);
        setRootPath(rootPath);
        //logger.info(rootPath);
        return rootPath;
    }

    public void createDir(String dirPath){
        //logger.info("createDir(" + dirPath + ")");
        File dir = new File(dirPath);
        if(!dir.exists()){
            //logger.info("create folder...");
            dir.mkdir();
            if (dir.exists()){
                //logger.info("created");
            }
            else {
                //logger.warning("NOT created");
            }
        }
    }

    public boolean existDir(String rootFolder){
        File dir = new File(rootFolder);
        if(dir.exists()){
            if(dir.isDirectory()){
                return true;
            }
        }
        return false;
    }

    public boolean createDocumentBuilder(){
        //logger.info("start createDocumentBuilder()");
        boolean result = false;
        try {
            this.docBuilder = this.docFactory.newDocumentBuilder();
            result = true;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //logger.info("result createDocumentBuilder(): " + String.valueOf(result) + " :"+this.docBuilder.toString());
        return result;
    }

    public Document genDoc(DocumentBuilder docBuilder, File file){
        Document result = null;
        try {
            result = docBuilder.parse(file);
        } catch (SAXException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        return result;
    }

    public Document genDoc() {
        //logger.info("start genDoc()");
        //logger.info(String.valueOf(this.file.length()));
        if (this.file.length() > 0){
            try {
                logger.info("try parce doc for file(size not null):" + file.getAbsolutePath());
                /*String fileAsString = readFileAsString(getFile().getAbsolutePath());
                fileAsString = fileAsString.replaceAll(">\\s+<", "><");
                logger.info(fileAsString);*/
                this.doc = this.docBuilder.parse(file);
                logger.info("doc generated!");
            } catch (SAXException exp) {
                //logger.warning("1");
                exp.printStackTrace();
                this.doc = null;
            } catch (IOException exp) {
                //logger.warning("2");
                exp.printStackTrace();
                this.doc = null;
            }
        }
        else{
            //logger.info("genDoc() lenght ==0");
            //logger.info("genDoc() docBuilder.newDocument()");
            this.doc = this.docBuilder.newDocument();
            //logger.info("genDoc() this.doc is setted: " + this.doc.toString());
            createDefaultXMLstructure();
        }
        return this.doc;
    }

    public static String readFileAsString(String fileName){
        //String fileName = "D:\\Projects\\TesterServer\\stringFromSelemiumGetContentExamPage.txt";
        File file = new File(fileName);
        String  result ="";

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //process the line
                result +=line;
            }
        }
        catch (Exception exp){
            System.out.println(exp);
        }

        return result;

    }

    public Document genDoc(String docAsString){
        /*docAsString = docAsString.replace("<!--","");
        docAsString = docAsString.replace("-->","");*/
        docAsString = this.prepareHTMLstring(docAsString);
        setDoc(convertFromStringToDocument(docAsString));
        return getDoc();
    }

    public boolean existFile(String fileName, boolean create){
        boolean result = false;
        this.file = new File(fileName);
        if (!this.file.exists()){
            logger.info("Not exist file: " + fileName);
            if (create){
                try {
                    this.file.createNewFile();
                    result = true;
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                }
            }
        }
        else{
            result=true;
        }
        logger.info(fileName + " exist: " + result);

        return result;
    }

    public boolean existFile(String fileName){
        boolean result = false;
        this.file = new File(fileName);
        if (this.file.exists()){
            result=true;
        }
        return result;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocumentBuilderFactory getDocFactory() {
        return docFactory;
    }

    public void setDocFactory(DocumentBuilderFactory docFactory) {
        this.docFactory = docFactory;
    }

    public DocumentBuilder getDocBuilder() {
        return docBuilder;
    }

    public void setDocBuilder(DocumentBuilder docBuilder) {
        this.docBuilder = docBuilder;
    }

    public Document getDoc() {
        //logger.info("TRY TO GET DOC");
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public XMLstructure getXmlStructure() {
        return xmlStructure;
    }

    public void setXmlStructure(XMLstructure xmlStructure) {
        this.xmlStructure = xmlStructure;
    }

    public String getSubDir() {
        return subDir;
    }

    public void setSubDir(String subDir) {
        this.subDir = subDir;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public List<String> getLstBasePaths() {
        return lstBasePaths;
    }

    public void setLstBasePaths(List<String> lstBasePaths) {
        this.lstBasePaths = lstBasePaths;
    }

    public String getDirUsers() {
        return dirUsers;
    }

    public String getAppData() {
        return appData;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void writeXML() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try{
            transformerFactory.setAttribute("indent-number", "4");
        }
        catch(Exception exp){
            exp.printStackTrace();
        }

        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // форматируем XML
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(getDoc()), new StreamResult(getFile()));

        } catch (TransformerConfigurationException exp) {
            exp.printStackTrace();
        } catch (TransformerException exp) {
            exp.printStackTrace();
        }

    }

    public void createDefaultXMLstructure(Document doc, Element root){
        doc.appendChild(root);
    }
    public void createDefaultXMLstructure(){
        //logger.info("start createDefaultXMLstructure()");
        //logger.info(doc.toString());
        Node root = this.doc.createElement(getXmlStructure().getRootName());
        this.doc.appendChild(root);
        logger.info("createDefaultXMLstructure appended child: " + getXmlStructure().getRootName());

        writeXML();
    }

    public Document removeSpacesFromXml(){
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        String xml = "";
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "nont");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // форматируем XML
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(getDoc()), new StreamResult(stringWriter));

            xml = stringWriter.toString();
            logger.info(xml);
            xml = xml.replaceAll(">\\s+<", "><");
            xml = xml + "\n";
            logger.info(xml);


        } catch (TransformerConfigurationException exp) {
            exp.printStackTrace();
        } catch (TransformerException exp) {
            exp.printStackTrace();
        }

        return convertFromStringToDocument(xml);
    }

    public Document convertFromStringToDocument(String str){
        logger.info("convertSromStringToDocument: " + str);
        //str = prepareHTMLstring(str);
        StringReader stringReader = new StringReader( str );
        InputSource inputSource = new InputSource( stringReader );
        try
        {
            setDoc(getDocBuilder().parse(inputSource));
            //return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getDoc();
    }

    public String prepareHTMLstring(String str){
        logger.info(str);
        int startBody = str.indexOf("<body");
        int endBody   = str.indexOf("</body>");
        //addLineToConsole(pageAsString);
        str = str.substring(startBody,endBody);
        str = str.replace("</body>", "") + "</body>";

        String arr[] = str.split("<input");


        for (int i=0; i< arr.length; i++){
            if (i>0){
                str += "<input";
                int index = arr[i].indexOf(">");
                StringBuffer sb = new StringBuffer(arr[i]);
                sb.insert(index," /");
                str += sb.toString();
            }
            else {
                str = arr[0];
            }

        }


        str = str.replace("&nbsp;", "");
        str = str.replace("value=\"<\"", "value=\"more\"");
        str = str.replace("<br>", "<br />");
        str = str.replace("<hr>", "<hr />");

        str = str.replaceAll("<!--([\\s\\S]*?)-->","");

        return str;
    }

    public NodeList getNodeByAttribute(String attrName, String attrValue){
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        String expression;
        NodeList nodeList;
        expression = String.format("//*[@%s=\"%s\"]", attrName, attrValue);;
        try {
            return (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null;


    }
}
