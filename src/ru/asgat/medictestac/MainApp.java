package ru.asgat.medictestac;

/**
 * Created by Skif on 19.08.2017.
 */

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.asgat.medictestac.controller.SettingsController;
import ru.asgat.medictestac.model.*;
import ru.asgat.medictestac.model.Worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class MainApp extends Application {

    private Stage mainStage;
    private static final int SPLASH_WIDTH = 600;
    private static final int SPLASH_HEIGHT = 200;
    private Stage primaryStage;
    private AnchorPane rootLayout;

    private XMLReader configFile;
    private XMLReader questionsFile;
    private XMLReader newQuestionsFile;
    private XMLReader logFile;
    //private List<BaseInternalElementsNode> listNodesConfig;
   // private List<BaseInternalElementsNode> listQuestionsFile;
   // private List<BaseInternalElementsNode>  listNewQuestionsFile;
    private static final Logger logger = Logger.getLogger( XMLReader.class.getName() );
    private ObservableList<Settings> dataConfig = FXCollections.observableArrayList();
    private ObservableList<BaseInternalElementsNode> dataQuestions = FXCollections.observableArrayList();
    private ObservableList<BaseInternalElementsNode> dataNewQuestion = FXCollections.observableArrayList();
    private ObservableList<BaseInternalElementsNode> dataLog = FXCollections.observableArrayList();

    private List<Worker> lstTesters = new ArrayList<Worker>();
    //private Observable configData =

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Date date = new Date();
        String millis = String.valueOf(date.getTime());
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        this.configFile = new XMLReader("config.xml", "conf", "settings");
        this.questionsFile = new XMLReader("questions.xml","conf", "questions");
        this.logFile = new XMLReader(getUniqFileName("","log-",millis, ".xml"),"logs","records");

        getConfigFile().removeSpacesFromXml();
        getQuestionsFile().removeSpacesFromXml();

        XMLstructure configStructure = getConfigFile().getXmlStructure();
        //XMLstructure questionStructure = getQuestionsFile().getXmlStructure();

        List<BaseInternalElementsNode> lstSettings = configStructure.getLstNodes();
        if (lstSettings==null){
            logger.info("mainApp:start() lstSettings is null");
            logger.info(getConfigFile().getDoc().getFirstChild().getNodeName());
            NodeList lstNodes = getConfigFile().getDoc().getFirstChild().getChildNodes();
            if (lstNodes.getLength()==0){
                lstSettings = new ArrayList<BaseInternalElementsNode>();
                lstSettings.add(new Settings());
            }
            else{
                for(int indNod =0; indNod< lstNodes.getLength(); indNod++){
                    logger.info("childNode: " + lstNodes.item(indNod).getNodeName());
                    if (lstNodes.item(indNod).getNodeName().compareTo("#text")!=0){
                        Element elem = (Element) lstNodes.item(indNod);
                        lstSettings = new ArrayList<BaseInternalElementsNode>();
                        BaseInternalElementsNode setting = new Settings();
                        setting.setValuesFromNode(elem);
                        lstSettings.add(setting);
                        break;
                    }
                }

            }
        }
        logger.warning("------------------------|||-------------------------");
        logger.info(getQuestionsFile().getFile().getAbsolutePath());
        NodeList lstQuestionNodes = getQuestionsFile().getDoc().getFirstChild().getChildNodes();
        for(int qInd=0; qInd< lstQuestionNodes.getLength(); qInd++){
            Question question = new Question();
            question.setValuesFromNode((Element)lstQuestionNodes.item(qInd));
            if (question.getText().compareTo("")!=0){
                dataQuestions.add(question);
            }
        }


        dataConfig.add((Settings)lstSettings.get(0));
        this.configFile.writeXML();

        logger.info(dataConfig.get(0).getHostname());




        //logger.info("mainApp:start() getXmlStructure(): " + this.configFile.getXmlStructure().getRootElement().getNodeName().toString());




        //this.configFile.genDocOnCreate();

        //this.configFile.writeXML();

        initRootLayout();
    }

    public String getUniqFileName(String prefix, String root, String uniq, String suffix){
        return prefix + root + uniq + suffix;
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initRootLayout() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/MainForm.fxml"));
        rootLayout = (AnchorPane) loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);

        primaryStage.setScene(scene);
        primaryStage.show();
        SettingsController controller = loader.getController();
        controller.setMainApp(this);

    }

    public XMLReader getConfigFile() {
        return configFile;
    }

    public void setConfigFile(XMLReader configFile) {
        this.configFile = configFile;
    }

    public XMLReader getQuestionsFile() {
        return questionsFile;
    }

    public void setQuestionsFile(XMLReader questionsFile) {
        this.questionsFile = questionsFile;
    }

    public XMLReader getLogFile() {
        return logFile;
    }

    public void setLogFile(XMLReader logFile) {
        this.logFile = logFile;
    }

    public ObservableList<Settings> getDataConfig() {
        return dataConfig;
    }

    public void setDataConfig(ObservableList<Settings> dataConfig) {
        this.dataConfig = dataConfig;
    }

    public ObservableList<BaseInternalElementsNode> getDataQuestions() {
        return dataQuestions;
    }

    public void setDataQuestions(ObservableList<BaseInternalElementsNode> dataQuestions) {
        this.dataQuestions = dataQuestions;
    }

    public ObservableList<BaseInternalElementsNode> getDataNewQuestion() {
        return dataNewQuestion;
    }

    public void setDataNewQuestion(ObservableList<BaseInternalElementsNode> dataNewQuestion) {
        this.dataNewQuestion = dataNewQuestion;
    }

    public ObservableList<BaseInternalElementsNode> getDataLog() {
        return dataLog;
    }

    public void setDataLog(ObservableList<BaseInternalElementsNode> dataLog) {
        this.dataLog = dataLog;
    }

    public List<Worker> getLstTesters() {
        return lstTesters;
    }

    public void setLstTesters(List<Worker> lstTesters) {
        this.lstTesters = lstTesters;
    }

    public XMLReader getNewQuestionsFile() {
        return newQuestionsFile;
    }

    public void setNewQuestionsFile(XMLReader newQuestionsFile) {
        this.newQuestionsFile = newQuestionsFile;
    }
}
