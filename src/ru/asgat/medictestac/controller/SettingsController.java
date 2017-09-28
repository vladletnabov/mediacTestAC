package ru.asgat.medictestac.controller;

/**
 * Created by Skif on 21.08.2017.
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.asgat.medictestac.MainApp;
import ru.asgat.medictestac.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import  org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;


public class SettingsController {

    private static final Logger logger = Logger.getLogger( SettingsController.class.getName() );
    private String pageAsString;
    private String resultPageAsString;
    private ObservableList<TableResultTesterExam> listResultTesterExams = FXCollections.observableArrayList();
    private ObservableList<TableResultNewQuestion> listResultNewQuestions = FXCollections.observableArrayList();

    private Log logLine;
    private double currentProggress = 0.1;
    private int newQuestionID = 0;

    private Worker currentTester;

    @FXML // fx:id="btnStartTest"
    private Button btnStartTest; // Value injected by FXMLLoader

    @FXML // fx:id="btnSaveResult"
    private Button btnSaveResult; // Value injected by FXMLLoader

    @FXML // fx:id="btnGenGuestions"
    private Button btnGenGuestions; // Value injected by FXMLLoader

    @FXML // fx:id="tabPaneMainWindow"
    private TabPane tabPaneMainWindow; // Value injected by FXMLLoader

    @FXML // fx:id="tabSettings"
    private Tab tabSettings; // Value injected by FXMLLoader

    @FXML // fx:id="anchorPaneSettings"
    private AnchorPane anchorPaneSettings; // Value injected by FXMLLoader

    @FXML // fx:id="paneSettings"
    private Pane paneSettings; // Value injected by FXMLLoader

    @FXML // fx:id="selectSingleWorker"
    private RadioButton selectSingleWorker; // Value injected by FXMLLoader

    @FXML // fx:id="selectCountCoWorker"
    private ToggleGroup selectCountCoWorker; // Value injected by FXMLLoader

    @FXML // fx:id="selectMultiWorkers"
    private RadioButton selectMultiWorkers; // Value injected by FXMLLoader

    @FXML // fx:id="fioFamily"
    private TextField fioFamily; // Value injected by FXMLLoader

    @FXML // fx:id="fioName"
    private TextField fioName; // Value injected by FXMLLoader

    @FXML // fx:id="fioYear"
    private TextField fioYear; // Value injected by FXMLLoader

    @FXML // fx:id="txtListCoWorker"
    private TextArea txtListCoWorker; // Value injected by FXMLLoader

    @FXML // fx:id="hostname"
    private TextField hostname; // Value injected by FXMLLoader

    @FXML // fx:id="btnSave"
    private Button btnSave; // Value injected by FXMLLoader

    @FXML // fx:id="btnByDflt"
    private Button btnByDflt; // Value injected by FXMLLoader

    @FXML // fx:id="btnClear"
    private Button btnClear; // Value injected by FXMLLoader

    @FXML // fx:id="radioUnknownQuastTrue"
    private RadioButton radioUnknownQuastTrue; // Value injected by FXMLLoader

    @FXML // fx:id="unknowQuestion"
    private ToggleGroup unknowQuestion; // Value injected by FXMLLoader

    @FXML // fx:id="radioUnknownQuastFalse"
    private RadioButton radioUnknownQuastFalse; // Value injected by FXMLLoader

    @FXML // fx:id="minTimout"
    private Slider minTimout; // Value injected by FXMLLoader

    @FXML // fx:id="maxTimout"
    private Slider maxTimout; // Value injected by FXMLLoader

    @FXML // fx:id="tabConsole"
    private Tab tabConsole; // Value injected by FXMLLoader

    @FXML // fx:id="anchorPaneConsole"
    private AnchorPane anchorPaneConsole; // Value injected by FXMLLoader

    @FXML // fx:id="txtConsoleLog"
    private TextArea txtConsoleLog; // Value injected by FXMLLoader

    @FXML // fx:id="tabResult"
    private Tab tabResult; // Value injected by FXMLLoader

    @FXML // fx:id="anchorPaneResult"
    private AnchorPane anchorPaneResult; // Value injected by FXMLLoader

    @FXML // fx:id="lblMinTimout"
    private Label lblMinTimout; // Value injected by FXMLLoader

    @FXML // fx:id="lblMaxTimout"
    private Label lblMaxTimout; // Value injected by FXMLLoader

    @FXML // fx:id="progressTesting"
    private ProgressBar progressTesting; // Value injected by FXMLLoader


    @FXML // fx:id="sliderCoincedence"
    private Slider sliderCoincedence; // Value injected by FXMLLoader

    @FXML // fx:id="lblCoincedence"
    private Label lblCoincedence; // Value injected by FXMLLoader

    @FXML
    private Slider dopDelayAfterTest;

    @FXML
    private Label lblDopDelayAfterTest;

    @FXML
    private RadioButton selectAlgoOld;

    @FXML
    private ToggleGroup selectAlgoGroup;

    @FXML
    private RadioButton selectAlgoNew;

    @FXML // fx:id="tblResultTesterExam"
    private TableView<TableResultTesterExam> tblResultTesterExam = new TableView<TableResultTesterExam>(); // Value injected by FXMLLoader

    @FXML // fx:id="colRTEid"
    private TableColumn<TableResultTesterExam, Long> colRTEid; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEdate"
    private TableColumn<TableResultTesterExam, String>  colRTEdate; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEfamily"
    private TableColumn<TableResultTesterExam, String>  colRTEfamily; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEname"
    private TableColumn<TableResultTesterExam, String>  colRTEname; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEyear"
    private TableColumn<TableResultTesterExam, String>  colRTEyear; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEcountQuestion"
    private TableColumn<TableResultTesterExam, Integer>  colRTEcountQuestion; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEcountRightQurestion"
    private TableColumn<TableResultTesterExam, Integer>  colRTEcountRightQurestion; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEcountMaxError"
    private TableColumn<TableResultTesterExam, Integer> colRTEcountMaxError; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEcountErrorQuestion"
    private TableColumn<TableResultTesterExam, Integer>  colRTEcountErrorQuestion; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEautoGen"
    private TableColumn<TableResultTesterExam, Boolean>  colRTEautoGen; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEautoQuesion"
    private TableColumn<TableResultTesterExam, Integer>  colRTEautoQuesion; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEresult"
    private TableColumn<TableResultTesterExam, String> colRTEresult; // Value injected by FXMLLoader

    @FXML // fx:id="colRTEwrongAnswers"
    private TableColumn<TableResultTesterExam, String> colRTEwrongAnswers; // Value injected by FXMLLoader

    @FXML // fx:id="tblNewQuestion"
    private TableView<TableResultNewQuestion> tblNewQuestion = new TableView<TableResultNewQuestion>(); // Value injected by FXMLLoader

    @FXML // fx:id="colNQid"
    private TableColumn<TableResultNewQuestion, Long> colNQid; // Value injected by FXMLLoader

    @FXML // fx:id="colNQname"
    private TableColumn<TableResultNewQuestion, String>  colNQname; // Value injected by FXMLLoader

    @FXML // fx:id="colNQtext"
    private TableColumn<TableResultNewQuestion, String>  colNQtext; // Value injected by FXMLLoader

    @FXML // fx:id="colNQanswers"
    private TableColumn<TableResultNewQuestion, String>  colNQanswers; // Value injected by FXMLLoader

    @FXML
    void SaveSettings(MouseEvent event) {
        logger.info("SettingController: Save button clicked! ");
        setAllValues();
        this.mainApp.getConfigFile().setDoc(
                this.mainApp.getDataConfig().get(0).setDocumetStructure(
                        this.mainApp.getConfigFile().getDoc()
                )
        );
        logger.info("-----------------------------> " + this.mainApp.getDataConfig().get(0).getHostname());
        try{
            this.mainApp.getConfigFile().writeXML();
        }
        catch (Exception e){
            logger.warning(e.toString());
        }
        //mainApp.getConfigFile().setDoc(new Settings().setDocumentStructure(mainApp.getConfigFile().getDoc(), settingNodes));

    }

    @FXML
    void startTest(MouseEvent event) {
        //logger.info(((Button)selectCountCoWorker.getSelectedToggle()).getId());
        txtConsoleLog.setText("");
        RadioButton radio = (RadioButton)selectCountCoWorker.getSelectedToggle();
        listResultTesterExams.clear();
        listResultNewQuestions.clear();
        //logger.info("---------------------------------------------\n\n" + listResultTesterExams.size() + "\n\n------------------------------------------------\n");
        tblResultTesterExam.refresh();
        tblNewQuestion.refresh();

        List<Question> listQuestionBase = getQuestionFromBase();

        List<Worker> lstTesters = null;
        logger.warning(radio.getId());
        if(radio.getId().compareTo("multiPersone")==0){
            logger.info("selectMultiWorkers");
            lstTesters = parseTextListTesters(mainApp.getLstTesters());
        }
        else{
            logger.info("selectSingleWorkers");
            lstTesters = compareTextFieldForListTesters(mainApp.getLstTesters());
        }

        if (lstTesters!=null){
            mainApp.setLstTesters(lstTesters);
            for(Worker tester: mainApp.getLstTesters()){
                logger.info("person:\n" + tester.getFamily() + ", " + tester.getName() + ", " + tester.getYear());
            }
            String auto = "no";
            if(((RadioButton)unknowQuestion.getSelectedToggle()).getId().compareTo("radioUnknownQuastTrue")==0){
                auto="yes";
            }
            tabPaneMainWindow.getSelectionModel().select(tabConsole);
            addLineToConsole("start testing");
            addLineToConsole("server: " + hostname.getText());
            addLineToConsole("autoanswer: " + auto);

            currentProggress = 0.1;
            progressTesting.setProgress(currentProggress);

            double examPartOfProgress = (1.0- currentProggress)/mainApp.getLstTesters().size();

            setNewQuestionID(0);

            testerCicle:
            for (int indTester=0; indTester<mainApp.getLstTesters().size(); indTester++) {
                setCurrentTester(mainApp.getLstTesters().get(indTester));
                Date examDate = new Date();
                setLogLine(new Log());
                getLogLine().setDate(examDate);

                TableResultTesterExam examResultRecord = new TableResultTesterExam();
                examResultRecord.setId(indTester+1);
                examResultRecord.setFioFamily(getCurrentTester().getFamily());
                examResultRecord.setFioName(getCurrentTester().getName());
                examResultRecord.setFioYear(getCurrentTester().getYear());
                examResultRecord.setDate(examDate);
                examResultRecord.setFormattedDate(examDate);


                DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                capabilities.setCapability("marionette", true);
                WebDriver myWebDriver = new FirefoxDriver();

                addLineToConsole("driver - ok");
                myWebDriver.get(mainApp.getDataConfig().get(0).getHostname());
                addLineToConsole("get - ok");
                addLineToConsole(myWebDriver.getTitle());


                // ввод регистрационных данных
                setSeleniumAuthData(myWebDriver);
                //проверка авторизации
                if(!checkSeleniumAuth(myWebDriver)){
                    examResultRecord.setResult("ошибка авторизации");
                    listResultTesterExams.add(examResultRecord);
                    myWebDriver.close();

                    continue testerCicle;
                }

                delayInput(10); //задержка перехода на следующую страницу
                if(!pressStartExamButton(myWebDriver)){
                    examResultRecord.setResult("ошибка отправки согласия на тест");
                    listResultTesterExams.add(examResultRecord);
                    myWebDriver.close();
                    continue testerCicle;
                }

                String examPageAsString = myWebDriver.getPageSource();


                HashMap <String, HashMap<String, Question>> hashQuestionCompareResult= new HashMap<String, HashMap<String, Question>>();
                /*
                * hashQuestionCompareResult содержит списки вопросов с правильными ответами и незнакомых вопросов
                * со вусеми ответами
                *
                * hashQuestionCompareResult[questionWithAnswers] - вопросы с ответами
                * hashQuestionCompareResult[newQuestions] - новые вопросы со всеми ответами без разделения
                *                                           на правильные и неправильные
                * hashQuestionCompareResult[questionForm] - вопросы с ответами
                *
                * */

                /*hashQuestionCompareResult = prepareDataForClickBot(examDate, indTester, listQuestionBase,
                        examPartOfProgress , hashQuestionCompareResult, "READFILEFROMDISK");*/

                hashQuestionCompareResult = prepareDataForClickBot(examDate, indTester, listQuestionBase,
                                examPartOfProgress , examPageAsString);

                examResultRecord.setCountQuestions(hashQuestionCompareResult.get("questionForm").size());
                examResultRecord.setCountRightQuestions(hashQuestionCompareResult.get("questionWithAnswers").size());
                examResultRecord.setCountWrongQuestions( hashQuestionCompareResult.get("newQuestions").size());
                if(mainApp.getDataConfig().get(0).getAutoAnswer()){
                    examResultRecord.setAutoGenAnswers(true);
                    examResultRecord.setCountQuestionsWautoGenAnswers(hashQuestionCompareResult.get("newQuestions").size());
                }
                else{
                    examResultRecord.setAutoGenAnswers(false);
                    examResultRecord.setCountQuestionsWautoGenAnswers(0);
                }
                listResultTesterExams.add(examResultRecord);

                List<Question> listAutoGenQuestions = new ArrayList<Question>();

                if (mainApp.getDataConfig().get(0).getAutoAnswer()){
                    listAutoGenQuestions = genAutoGenAnswers(convertHashToList(hashQuestionCompareResult.get("newQuestions")));
                }

                if (hashQuestionCompareResult.get("questionForm").size()>
                        (hashQuestionCompareResult.get("questionWithAnswers").size() + listAutoGenQuestions.size())){
                    examResultRecord.setResult("Список вопросов с ответами меньше, чем вопросов экзамена.");
                    myWebDriver.close();
                    continue;
                }

                boolean setAnsers = true;
                for(Map.Entry<String, Question> entry : hashQuestionCompareResult.get("questionWithAnswers").entrySet()){
                    addLineToConsole("Pressed for question id: " + entry.getKey());
                    if(!clickAnswerSelenium(myWebDriver, entry.getValue())){
                        addLineToConsole("Прервано на обработке вопросов из базы");
                        examResultRecord.setResult("Прервано на обработке вопросов из базы");
                        setAnsers = false;
                        continue testerCicle;
                    }
                }
                for(Question question: listAutoGenQuestions){
                    if(!clickAnswerSelenium(myWebDriver, question)){
                        addLineToConsole("Прервано на обработке вопросов с автоматически сгененрированными ответами");
                        examResultRecord.setResult("Прервано на обработке вопросов с автоматически сгененрированными ответами");
                        setAnsers = false;
                        continue testerCicle;
                    }
                }
                if(setAnsers){examResultRecord.setResult("Ответы заданы");}

                addLineToConsole("TEST CHECK: ");

                List<String> listKey = new ArrayList(hashQuestionCompareResult.get("questionWithAnswers").keySet());
                Collections.sort(listKey);
                for (String key: listKey){
                    addLineToConsole(key);
                    addLineToConsole(hashQuestionCompareResult.get("questionWithAnswers").get(key).getIdNode());
                    addLineToConsole(hashQuestionCompareResult.get("questionWithAnswers").get(key).getText());
                    addLineToConsole(hashQuestionCompareResult.get("questionWithAnswers").get(key).getAnswers().get(0).getId());
                    addLineToConsole(hashQuestionCompareResult.get("questionWithAnswers").get(key).getAnswers().get(0).getText());
                }

                addLineToConsole("TEST CHECK2: ");

                listKey = new ArrayList(hashQuestionCompareResult.get("questionForm").keySet());
                Collections.sort(listKey);
                for (String key: listKey){
                    addLineToConsole(key);
                    addLineToConsole(hashQuestionCompareResult.get("questionForm").get(key).getIdNode());
                    addLineToConsole(hashQuestionCompareResult.get("questionForm").get(key).getText());
                    addLineToConsole(hashQuestionCompareResult.get("questionForm").get(key).getAnswers().get(0).getId());
                    addLineToConsole(hashQuestionCompareResult.get("questionForm").get(key).getAnswers().get(0).getText());
                }


                delayInput(mainApp.getDataConfig().get(0).getDopDelay());

                if(pressFinishExam(myWebDriver)){
                    examResultRecord.setResult("Переход на страницу с результатами");
                }
                else{
                    getLogLine().setTester(getCurrentTester());
                    getLogLine().setText(txtConsoleLog.getText());
                    mainApp.getLogFile().setDoc(getLogLine().setDocumetStructure(mainApp.getLogFile().getDoc()));
                    mainApp.getLogFile().writeXML();
                    myWebDriver.close();
                    continue testerCicle;
                }
                HashMap<String, HashMap<String, Integer>> mapResultPage = getResultPageAsString(myWebDriver);

                examResultRecord.setCountWrongQuestions(mapResultPage.get("results").get("errorquestions"));
                examResultRecord.setCountMaxErrors(mapResultPage.get("results").get("maxerrors"));
                addLineToConsole("Количество заданных вопросов: " + String.valueOf(mapResultPage.get("results").get("allquestions")));
                addLineToConsole("Количество неправильных ответов: " + String.valueOf(mapResultPage.get("results").get("errorquestions")));
                addLineToConsole("Максимально количество ошибок: " + String.valueOf(mapResultPage.get("results").get("maxerrors")));
                examResultRecord.setCountRightQuestions(examResultRecord.getCountQuestions()-examResultRecord.getCountWrongQuestions());

                int indWrongQuestion = 1;
                String wrongList = "";
                for (String key : mapResultPage.get("errorquestion").keySet()) {
                    logger.info("key: " + key);
                    wrongList= wrongList+ String.valueOf(indWrongQuestion) + ". " + key + "\n";
                    indWrongQuestion++;
                }
                logger.info(wrongList);
                examResultRecord.setWrongQuestions(wrongList);
                addLineToConsole("Неправильные ответы даны на следующие вопросы: \n" + wrongList + "\n");

                if(examResultRecord.getCountMaxErrors()<examResultRecord.getCountWrongQuestions()){
                    examResultRecord.setResult("Тест не сдан");
                    addLineToConsole("Тест не сдан");
                }
                else{
                    examResultRecord.setResult("Сдан");
                    addLineToConsole("Сдан");
                }
                pressExitExamButton(myWebDriver);

                myWebDriver.close();

                getLogLine().setTester(getCurrentTester());
                getLogLine().setText(txtConsoleLog.getText());
                mainApp.getLogFile().setDoc(getLogLine().setDocumetStructure(mainApp.getLogFile().getDoc()));
                mainApp.getLogFile().writeXML();
            }
        }
        else{
            logger.warning("NOT SETTED TESTER!!!!");
            callAllertNotAllSettings();
        }


    }

    public HashMap<String, HashMap<String, Integer>> getResultPageAsString(WebDriver myWebDriver){

        delayInput(10);
        this.resultPageAsString = myWebDriver.getPageSource();

        //readResultHTMLpageAsString();
        XMLReader resultHTMLfile = new XMLReader(resultPageAsString, true);
        resultHTMLfile.removeSpacesFromXml();


        NodeList listRawNodesAnswersResult =  resultHTMLfile.getNodeByAttribute("class", "v-counter"); //получаем список нод с вопросами
        HashMap<String, Integer> mapAnswerResults = new HashMap<String, Integer>();
        HashMap<String, Integer> mapWrongAnswers = new HashMap<String, Integer>();
        for(int indNode=0; indNode<listRawNodesAnswersResult.getLength(); indNode++){
            Node currentCounterNode = listRawNodesAnswersResult.item(indNode);
            String name="";
            Integer count=0;
            for(int indCouterChild=0; indCouterChild<currentCounterNode.getChildNodes().getLength(); indCouterChild++){
                Element element = (Element) currentCounterNode.getChildNodes().item(indCouterChild);
                switch (element.getAttribute("class")){
                    case "count-number":
                        count = Integer.valueOf(element.getTextContent());
                        break;
                    case "v-counter-text":
                        //name = element.getTextContent()
                        String text = element.getTextContent();
                        switch (text){
                            case "Задано вопросов":
                                name="allquestions";
                                break;
                            case "Количество допустимых ошибок":
                                name="maxerrors";
                                break;
                            case "Допущено ошибок":
                                name="errorquestions";
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
            mapAnswerResults.put(name,count);
        }
        logger.info("----------------");
        NodeList listRawNodesWrongAnswers =  resultHTMLfile.getDoc().getElementsByTagName("ol");
        for(int indOl=0; indOl<listRawNodesWrongAnswers.getLength(); indOl++){
            Node currentNode = listRawNodesWrongAnswers.item(indOl);
            for(int indList=0; indList<currentNode.getChildNodes().getLength(); indList++){
                String question = currentNode.getChildNodes().item(indList).getTextContent();
                question = question.replace(":",": ");
                logger.info("Wrong question: " + String.valueOf(indList + 1) + ". " + question);
                mapWrongAnswers.put(question, (indList+1));
            }
        }
        logger.info("----------------");
        HashMap<String, HashMap<String, Integer>> result = new HashMap<String, HashMap<String, Integer>>();
        result.put("results", mapAnswerResults);
        result.put("errorquestion", mapWrongAnswers);

        return result;

    }

    public boolean pressFinishExam(WebDriver myWebDriver){
        try {
            WebElement element = myWebDriver.findElement(By.cssSelector("button#ps"));
            element.click();
            addLineToConsole("Нажатие селектора: button#ps -> завершение экзамена ");

        }
        catch (Exception exp){
            addLineToConsole("Кнопка завершения тестирования не найдена" );
            addLineToConsole(exp.toString());

            exp.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Integer> getAnswerResultsSelenium(WebDriver myWebDriver){
        List<Integer>  resultList = null;
        String examPageAsString = "READFILEFROMDISK";
        try{
            examPageAsString = myWebDriver.getPageSource();
        }
        catch(Exception exp){
            addLineToConsole("ошибка получения страницы после прохождения теста");
            addLineToConsole(exp.toString());
            exp.printStackTrace();
        }

        if(examPageAsString.compareTo("READFILEFROMDISK")==0){
            readHTMLpageAsString();
            addLineToConsole("Чтение заглушки с диска");
        }
        else{
            this.pageAsString = examPageAsString;
        }

        XMLReader questionHTMLfile = new XMLReader(pageAsString);
        questionHTMLfile.removeSpacesFromXml();

        NodeList listRawNodesQuestions =  questionHTMLfile.getNodeByAttribute("class", "v-team-member-box col-sm-12");


        return resultList;
    }

    public boolean clickAnswerSelenium(WebDriver myWebDriver, Question question){

        for(Answer answer: question.getAnswers()){
            //delayInput(mainApp.getDataConfig().get(0).getCurrentTimout());
            String elementId = "input#" + answer.getId();
            try {
                WebElement element = myWebDriver.findElement(By.cssSelector(elementId));
                addLineToConsole("Нажатие селектора: " + elementId);
                element.click();
            }
            catch (Exception exp){
                addLineToConsole("ошибка поиска элемента на странице для клика по нему: " + elementId);
                addLineToConsole(exp.toString());

                exp.printStackTrace();
                return false;
            }

        }

        return true;
    }

    public List<Question> genAutoGenAnswers(List<Question> listQuestion){
        List<Question> listAutoGenQuestion = new ArrayList<Question>();
        for(Question question: listQuestion){
            int maxRightAnswers = 1;
            logger.info("question.getAnswers().size(): " + question.getAnswers().size());
            if(question.getAnswers().size()>2){
                maxRightAnswers = question.getAnswers().size()-1;
            }
            int rightAnswersCount = (int) question.getCurrentRandom(1, maxRightAnswers);
            int answerCount=0;
            List<Integer> answersIndex = new ArrayList<Integer>();
            while (answerCount<rightAnswersCount){
                int currentIndex = question.getCurrentRandom(0, question.getAnswers().size()-1);
                boolean exist = false;
                for(Integer index: answersIndex){
                    if(index==currentIndex){
                        exist=true;
                    }
                }
                if(!exist){
                    logger.info("current index: " + currentIndex);
                    answersIndex.add(currentIndex);
                    answerCount++;
                }
            }
            List<Answer> listRightAnswers = new ArrayList<Answer>();

            for(Integer index: answersIndex){
                listRightAnswers.add(question.getAnswers().get(index));
            }
            question.setAnswers(listRightAnswers);
            listAutoGenQuestion.add(question);
        }
        return listAutoGenQuestion;
    }

    public HashMap <String, HashMap<String, Question>> prepareDataForClickBot(Date examDate, int indTester,
                                                                   List<Question> listQuestionBase,
                                                                   double examPartOfProgress,
                                                                   String htmlString){

        HashMap <String, HashMap<String, Question>> hashQuestionCompareResultMethod = new HashMap<String,  HashMap<String, Question>>();
        logger.info("test for tester: " + getCurrentTester().getFamily() + " " + getCurrentTester().getName());
        addLineToConsole("Tester: " + getCurrentTester().getFamily() + " " + getCurrentTester().getName() + ", "+ getCurrentTester().getYear());
        addLineToConsole("time to start: " + currentDateTime());
        addLineToConsole("timout: " + String.valueOf(mainApp.getDataConfig().get(0).getCurrentTimout()) + " (" + String.valueOf(mainApp.getDataConfig().get(0).getMinTimout()) + "  - "+ String.valueOf(mainApp.getDataConfig().get(0).getMaxTimout()) + ")");

        if(htmlString.compareTo("READFILEFROMDISK")==0){
            readHTMLpageAsString();
        }
        else{
            this.pageAsString = htmlString;
        }
        //addLineToConsole(pageAsString);

        XMLReader questionHTMLfile = new XMLReader(pageAsString);
        questionHTMLfile.removeSpacesFromXml();

        NodeList listRawNodesQuestions =  questionHTMLfile.getNodeByAttribute("class", "v-team-member-box col-sm-12"); //получаем список нод с вопросами
        List<Question> listQuestionExamForm = new ArrayList<Question>();



        if (listRawNodesQuestions!=null){
            //
            for(int indNode=0; indNode<listRawNodesQuestions.getLength(); indNode++){
                        /*addLineToConsole(listRawNodesQuestions.item(indNode).getChildNodes().item(0).
                                getChildNodes().item(0).
                                getChildNodes().item(0).
                                getChildNodes().item(0).
                                getChildNodes().item(0).getTextContent());*/
                Element questionNode = (Element) listRawNodesQuestions.item(indNode).getChildNodes().item(0).
                        getChildNodes().item(0);
                //addLineToConsole(questionNode.getAttribute("class"));
                Question currentQuestion = new Question();
                currentQuestion.setValueFromHtmlNode(questionNode);
                String answers = "";
                String idQuestion = currentQuestion.getAnswers().get(0).getId().split("_")[0];
                addLineToConsole(currentQuestion.getText());
                for(int indAns=0; indAns<currentQuestion.getAnswers().size(); indAns++){
                    answers += (indAns + 1) + ". " + currentQuestion.getAnswers().get(indAns).getText() + " (" + currentQuestion.getAnswers().get(indAns).getId() + ")\n";
                }
                currentQuestion.setIdNode(idQuestion);
                addLineToConsole(answers);
                logger.warning("Add to list question: " + currentQuestion.getText());
                addLineToConsole("Add to list question: " + currentQuestion.getText());
                listQuestionExamForm.add(currentQuestion);
            }
        }

        checkListQuestionsResultWA(listQuestionExamForm);

        hashQuestionCompareResultMethod = compareQuestionFromFormAndBase(listQuestionExamForm, listQuestionBase);

        addLineToConsole("Количество вопросов: " + listQuestionExamForm.size());
        addLineToConsole("Количество вопросов с ответами: " + hashQuestionCompareResultMethod.get("questionWithAnswers").size());
        addLineToConsole("Количество вопросов без ответов(новых): " + hashQuestionCompareResultMethod.get("newQuestions").size());

        if (hashQuestionCompareResultMethod.get("newQuestions").size()>0){
            List<Question> listNewQuestions = convertHashToList(hashQuestionCompareResultMethod.get("newQuestions"));
            createNewQuestionFile(getCurrentTester(), listNewQuestions);
        }

        currentProggress += examPartOfProgress;
        progressTesting.setProgress(currentProggress);

        if (hashQuestionCompareResultMethod.get("newQuestions").size()>0){
            addLineToConsole("New questions more then 0");
            //for (int indNewQuestion=0; indNewQuestion< hashQuestionCompareResultMethod.get("newQuestions").size(); indNewQuestion++){
            for(Map.Entry<String, Question> entry : hashQuestionCompareResultMethod.get("newQuestions").entrySet()) {
                Question questionForNew = entry.getValue();
                TableResultNewQuestion newQuestion = new TableResultNewQuestion();
                setNewQuestionID(getNewQuestionID()+1);
                newQuestion.setId((long)getNewQuestionID());
                newQuestion.setName(getCurrentTester().getFamily(),getCurrentTester().getName(),getCurrentTester().getYear());
                newQuestion.setQuestion(questionForNew.getText());
                newQuestion.setAnswers(questionForNew.getAnswers());
                listResultNewQuestions.add(newQuestion);

            }
        }

        getCurrentTester().setAskedQuestions(listQuestionExamForm);
        getCurrentTester().setNewQuestions(convertHashToList(hashQuestionCompareResultMethod.get("newQuestions")));

        hashQuestionCompareResultMethod.put("questionForm", convertListToHash(listQuestionExamForm));

        //checkHashQuestionsResultsWA(hashQuestionCompareResultMethod);
        addLineToConsole("prepareDataForClickBot completed");
        return hashQuestionCompareResultMethod;

    }

    public HashMap<String, Question> convertListToHash(List<Question> list){
        HashMap<String, Question> hashMap = new HashMap<String, Question>();
        int ind = 1;
        for(Question question: list){
            if(question.getIdNode().compareTo("")==0){
                String id = "c" + ind;
                hashMap.put(id, question);
            }
            else{
                hashMap.put(question.getIdNode(), question);
            }
        }
        return hashMap;
    }

    public List<Question> convertHashToList(HashMap<String, Question> hashQuestions){
        List<Question> list = new ArrayList<Question>();
        for(Map.Entry<String, Question> entry : hashQuestions.entrySet()) {
            String key = entry.getKey();
            list.add(entry.getValue());
        }
        return list;
    }

    public void checkHashQuestionsResultsWA(HashMap <String, List<Question>> hashQuestionCompareResultMethod ){
        checkListQuestionsResultWA(hashQuestionCompareResultMethod.get("questionWithAnswers"));
    }

    public void checkListQuestionsResultWA(List<Question> list){
        addLineToConsole("size list: " + list.size());
        addLineToConsole("---------------------CHECKING---------------------");
        for(Question question: list){
            addLineToConsole("Question: " + question.getIdNode() + ". " +  question.getText());
            for(Answer answer: question.getAnswers()){
                addLineToConsole(" - answer: "+ answer.getId() + ". " + answer.getText());
            }
        }

        addLineToConsole("--------------------- FINISH ---------------------");
    }



    public void createNewQuestionFile(Worker tester, List<Question> listNewQuestion){
        Date date = new Date();
        String millis = String.valueOf(date.getTime());
        String fileName = "new-question-"+ tester.getFamily()+"-"+tester.getName()+"-"+tester.getYear()+"-"+millis+".xml";
        XMLReader newQuestionFile = new XMLReader(fileName, "logs", "new-questions");
        newQuestionFile.removeSpacesFromXml();
        //newQuestionFile.setDoc(newQuestionFile.);
        for(Question question: listNewQuestion){
            newQuestionFile.setDoc(question.setDocumetStructure(newQuestionFile.getDoc()));
        }
        newQuestionFile.writeXML();
    }

    public List<Question> getQuestionFromBase(){
        List<Question> listResult = new ArrayList<Question>();
        for (int i=0; i< mainApp.getDataQuestions().size(); i++){
            Question question = (Question)mainApp.getDataQuestions().get(i);
            listResult.add(question);
        }
        return  listResult;
    }


    public HashMap<String, HashMap<String, Question>> compareQuestionFromFormAndBase(List<Question> listQuestionForm,
                                                        List<Question> listQuestionBase){
        /*
        * listQuestionForm - список полученный со страницы сервера (question не имеет ID, ответы не имеют ID)
        *
        * listQuestionBase - список вопросов из базы (question ID содержит индекс вопроса из списка listQuestionForm,
        * answers содержит только правильные ответы и каждый ответ имеет ID из списка вопросов севера)
        *
        * получение списка вопросов с идентифкаторами правильных ответов исходя из имеющейся базы ответов на вопросы
        *
        * */
        addLineToConsole("Compare question from HTML page and Base");
        //List <Question> listQuestionAnswers = new ArrayList<Question>();
        HashMap <String, Question> hashQuestionAnswers = new HashMap<String, Question>();
        //List <Question> listNewQuestions = new ArrayList<Question>();
        HashMap <String, Question> hashNewQuestions  = new HashMap<String, Question>();

        for(Question questionForm: listQuestionForm){
            addLineToConsole("CREATE EXAM for querstion: " +  questionForm.getIdNode() +". " +  questionForm.getText());
            ExamWorker exam = new ExamWorker(questionForm, listQuestionBase, mainApp.getDataConfig().get(0).getCoincedence(), mainApp.getDataConfig().get(0).getAlgoOldNew());
            exam.setController(this);
            //
            boolean unknownQuestion = true;

            addLineToConsole("querstion ID: " + questionForm.getIdNode());
            Question resultQuestion = exam.checkQuestionWithAnswer();
            if (resultQuestion!=null){
                /*addLineToConsole("Add to hashQuestionAnswers question id: " + resultQuestion.getIdNode() + ". " + resultQuestion.getText());
                addLineToConsole("First answer id: " + resultQuestion.getAnswers().get(0).getId() + ". " + resultQuestion.getAnswers().get(0).getText());
                addLineToConsole("Add to hashQuestionAnswers question id: " + resultQuestion.getIdNode());*/
                //listQuestionAnswers.add(resultQuestion);
                Question question = new Question();
                question.setIdNode(resultQuestion.getIdNode());
                question.setText(resultQuestion.getText());
                question.setAnswers(resultQuestion.getAnswers());
                resultQuestion.hashCode();
                //hashQuestionAnswers.put(resultQuestion.getIdNode(), resultQuestion);
                hashQuestionAnswers.put(question.getIdNode(), question);
                addLineToConsole("............................\nCheck hashQuestionAnswers{}...");
                checkHashQuestionsResultWA(hashQuestionAnswers);
                addLineToConsole("............................\n");
                resultQuestion=null;

                unknownQuestion=false;
            }

            if (unknownQuestion){
                //listNewQuestions.add(questionForm);
                hashNewQuestions.put(questionForm.getIdNode(), questionForm);
            }

            //logger.warning("EXAM for querstion COMPLETED");

        }

        //checkListQuestionsResultWA(checkQuestionWithAnswer);
        addLineToConsole("----------------------------------------------\nchecking hashQuestionAnswers");
        checkHashQuestionsResultWA(hashQuestionAnswers);
        //HashMap<String, List<Question>> result = new HashMap<String, List<Question>>();
        HashMap<String, HashMap<String, Question>> result = new HashMap<String, HashMap<String, Question>>();

        checkHashQuestionsResultWA(hashQuestionAnswers);
        /*result.put("questionWithAnswers", listQuestionAnswers);
        result.put("newQuestions", listNewQuestions);*/
        result.put("questionWithAnswers", hashQuestionAnswers);
        result.put("newQuestions", hashNewQuestions);

        return result;
    }

    public void checkHashQuestionsResultWA(HashMap<String, Question> hashMapQuestions){
        addLineToConsole("size hashMapQuestions: " + hashMapQuestions.size());
        addLineToConsole("---------------------CHECK HM---------------------");
        List<String> listKey = new ArrayList(hashMapQuestions.keySet());
        Collections.sort(listKey);
        for(String key: listKey){
        //for(Map.Entry<String, Question> entry : hashMapQuestions.entrySet()){
            Question question = hashMapQuestions.get(key);
            addLineToConsole("Question: " + key + " ("+ question.getIdNode() + "). " +  question.getText());
            for(Answer answer: question.getAnswers()){
                addLineToConsole(" - answer: "+ answer.getId() + ". " + answer.getText());
            }
        }

        addLineToConsole("--------------------- FINISH ---------------------");
    }

    public List<Question> compareQuestinFromFormAndAnswers(List<Question> listQuestionForm,
                                                        List<Question> listQuestionAnswers){
        /*
        *
        * Полученние списка вопросов на которых нет ответа(новые вопросы в опроснике).
        * */
        List <Question> listNewQuestionWOAnswers = new ArrayList<Question>();


        return listNewQuestionWOAnswers;
    }

    private MainApp mainApp;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public SettingsController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        /*firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());*/

        minTimout.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                logger.info(String.valueOf(new_val.doubleValue()));
                lblMinTimout.setText(String.format("%.2f", new_val));
                mainApp.getDataConfig().get(0).setMinTimout(Double.valueOf(new_val.toString()));
                maxTimout.setValue(mainApp.getDataConfig().get(0).getMaxTimout());
            }
        });

        dopDelayAfterTest.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                logger.info(String.valueOf(new_val.doubleValue()));
                String delay= String.format("%.2f", new_val);
                logger.info(delay);
                delay = delay.replace(",",".");
                logger.info(delay);
                setLblDopDelay(Double.valueOf(delay));
                mainApp.getDataConfig().get(0).setDopDelay(dopDelayAfterTest.getValue());
            }
        });

        maxTimout.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                logger.info(String.valueOf(new_val.doubleValue()));
                lblMaxTimout.setText(String.format("%.2f", new_val));
                mainApp.getDataConfig().get(0).setMaxTimout(Double.valueOf(new_val.toString()));
                minTimout.setValue(mainApp.getDataConfig().get(0).getMinTimout());
            }
        });

        sliderCoincedence.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                logger.info(String.valueOf(new_val.doubleValue()));
                lblCoincedence.setText(String.format("%.2f", new_val) + " %");
            }
        });


        selectCountCoWorker.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (selectCountCoWorker.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) selectCountCoWorker.getSelectedToggle();
                    //logger.info("RADIO: " + button.getId());
                    if((button.getId()).compareTo("multiPersone")==0){
                        txtListCoWorker.setDisable(false);
                        fioFamily.setDisable(true);
                        fioName.setDisable(true);
                        fioYear.setDisable(true);
                    }
                    else{
                        txtListCoWorker.setDisable(true);
                        fioFamily.setDisable(false);
                        fioName.setDisable(false);
                        fioYear.setDisable(false);
                    }
                }
            }
        });

        unknowQuestion.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (unknowQuestion.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) unknowQuestion.getSelectedToggle();
                    //logger.info("RADIO: " + button.getId());
                    if((button.getId()).compareTo("radioUnknownQuastTrue")==0){
                        mainApp.getDataConfig().get(0).setAutoAnswer(true);
                    }
                    else{
                        mainApp.getDataConfig().get(0).setAutoAnswer(false);
                    }
                }
            }
        });

        selectAlgoGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (selectAlgoGroup.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) selectAlgoGroup.getSelectedToggle();
                    logger.info("RADIO: " + button.getId());
                    if((button.getId()).compareTo("selectAlgoOld")==0){
                        mainApp.getDataConfig().get(0).setAlgoOldNew(true);
                    }
                    else{
                        mainApp.getDataConfig().get(0).setAlgoOldNew(false);
                    }
                }
            }
        });

        //tblResultTesterExam.add;
        //;

        //TableResultTesterExam Data
        colRTEid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRTEdate.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        colRTEfamily.setCellValueFactory(new PropertyValueFactory<>("fioFamily"));
        colRTEname.setCellValueFactory(new PropertyValueFactory<>("fioName"));
        colRTEyear.setCellValueFactory(new PropertyValueFactory<>("fioYear"));
        colRTEcountQuestion.setCellValueFactory(new PropertyValueFactory<>("countQuestions"));
        colRTEcountRightQurestion.setCellValueFactory(new PropertyValueFactory<>("countRightQuestions"));
        colRTEcountMaxError.setCellValueFactory(new PropertyValueFactory<>("countMaxErrors"));
        colRTEcountErrorQuestion.setCellValueFactory(new PropertyValueFactory<>("countWrongQuestions"));
        colRTEautoGen.setCellValueFactory(new PropertyValueFactory<>("autoGenAnswers"));
        colRTEautoQuesion.setCellValueFactory(new PropertyValueFactory<>("countQuestionsWautoGenAnswers"));
        colRTEresult.setCellValueFactory(new PropertyValueFactory<>("result"));
        colRTEwrongAnswers.setCellValueFactory(new PropertyValueFactory<>("wrongQuestions"));
        tblResultTesterExam.setItems(listResultTesterExams);

        //TableResultNewQuestion Data

        colNQid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNQname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNQanswers.setCellValueFactory(new PropertyValueFactory<>("answers"));
        colNQtext.setCellValueFactory(new PropertyValueFactory<>("question"));
        tblNewQuestion.setItems(listResultNewQuestions);


    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        //personTable.setItems(mainApp.getPersonData());
        logger.info("++++++++++++++++++++++++++++++++++++++");
        logger.info((this.mainApp.getDataConfig().get(0)).getHostname());
        this.hostname.setText(this.mainApp.getDataConfig().get(0).getHostname());
        this.minTimout.setValue(this.mainApp.getDataConfig().get(0).getMinTimout());
        this.maxTimout.setValue(this.mainApp.getDataConfig().get(0).getMaxTimout());
        this.dopDelayAfterTest.setValue(this.mainApp.getDataConfig().get(0).getDopDelay());
        setLblDopDelay(this.mainApp.getDataConfig().get(0).getDopDelay());

        if (this.mainApp.getDataConfig().get(0).getAutoAnswer()==true){
            /*radioUnknownQuastTrue.setSelected(true);
            radioUnknownQuastFalse.setSelected(false);*/
            unknowQuestion.selectToggle(radioUnknownQuastTrue);
        }
        else{
            /*radioUnknownQuastTrue.setSelected(true);
            radioUnknownQuastFalse.setSelected(false);*/
            unknowQuestion.selectToggle(radioUnknownQuastFalse);
        }

        if(this.mainApp.getDataConfig().get(0).getAlgoOldNew()){
            selectAlgoGroup.selectToggle(selectAlgoOld);
        }
        else{
            selectAlgoGroup.selectToggle(selectAlgoNew);
        }



    }

    public Worker getCurrentTester() {
        return currentTester;
    }

    public void setCurrentTester(Worker currentTester) {
        this.currentTester = currentTester;
    }

    public Log getLogLine() {
        return logLine;
    }

    public void setLogLine(Log logLine) {
        this.logLine = logLine;
    }

    public int getNewQuestionID() {
        return newQuestionID;
    }

    public void setNewQuestionID(int newQuestionID) {
        this.newQuestionID = newQuestionID;
    }

    public double getCurrentProggress() {
        return currentProggress;
    }

    public void setCurrentProggress(double currentProggress) {
        this.currentProggress = currentProggress;
    }

    public void setAllValues(){
        this.mainApp.getDataConfig().get(0).setHostname(this.hostname.getText());
        this.mainApp.getDataConfig().get(0).setMinTimout(this.minTimout.getValue());
        this.mainApp.getDataConfig().get(0).setMaxTimout(this.maxTimout.getValue());
        /*if (this.radioUnknownQuastTrue.isSelected()){
            this.mainApp.getDataConfig().get(0).setAutoAnswer(true);
        }
        else{
            this.mainApp.getDataConfig().get(0).setAutoAnswer(false);
        }*/
        if(((RadioButton)unknowQuestion.getSelectedToggle()).getId().compareTo("radioUnknownQuastTrue")==0){
            this.mainApp.getDataConfig().get(0).setAutoAnswer(true);
        }
        else{
            this.mainApp.getDataConfig().get(0).setAutoAnswer(false);
        }
    }

    public List<Worker> parseTextListTesters(List<Worker> list){
        list = new ArrayList<Worker>();
        String text = txtListCoWorker.getText();
        List<String> txtList = Arrays.asList(text.split("\n"));
        int addToList = 0;
        for (String str: txtList) {
            int counter = 0;
            for( int i=0; i<str.length(); i++ ) {
                if( str.charAt(i) == ';' ) {
                    counter++;
                }
            }
            if (counter>=2){
                Worker tester = convertStringToTester(str);
                if (tester!=null){
                    list.add(tester);
                    addToList++;
                }
            }
        }
        if (addToList==0){
            list=null;
        }
        return list;
    }

    public Worker convertStringToTester(String str){
        Worker result = new Worker();
        String arr[] = str.split(";");
        if(arr.length<3){return null;}
        if (arr[0].compareTo("")==0){return null;}
        if (arr[1].compareTo("")==0){return null;}
        if (arr[2].compareTo("")==0){return null;}
        result.setFamily(arr[0]);
        result.setName(arr[1]);
        result.setYear(arr[2]);
        return result;
    }

    public List<Worker> compareTextFieldForListTesters(List<Worker> list){
        list = new ArrayList<Worker>();
        String text = fioFamily.getText()+";"+fioName.getText()+";"+fioYear.getText();
        List<String> txtList = Arrays.asList(text.split("\n"));
        Worker tester = convertStringToTester(text);
        if (tester!=null){
            list.add(tester);
        }
        else{
            return null;
        }
        return list;
    }

    public void addLineToConsole(String str){
        String currentText = txtConsoleLog.getText();
        if(("").compareTo(currentText)==0){
            txtConsoleLog.setText(str);
        }
        else{
            txtConsoleLog.setText(currentText + "\n" + str);
        }
    }

    public String currentDateTime(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        // Print what date is today!
        return  reportDate;
    }

    public void callAllertNotAllSettings(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Information Alert");
        String s ="Проверьте, что установлены все параметры и указаны все сотрудники, которые должны проходить тестирование";
        alert.setContentText(s);
        alert.show();
    }

    public void callAllertNotAllQuestionHasAnswer(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Information Alert");
        String s ="Внимание, не все вопросы имеют ответы";
        alert.setContentText(s);
        alert.show();
    }

    public void readHTMLpageAsString(){
        String fileName = "D:\\Projects\\TesterServer\\stringFromSelemiumGetContentExamPage.txt";
        File file = new File(fileName);
        this.pageAsString ="";

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //process the line
                this.pageAsString +=line;
            }
        }
        catch (Exception exp){
            System.out.println(exp);
        }

    }

    public void readResultHTMLpageAsString(){
        String fileName = "D:\\Projects\\TesterServer\\resultHTMLasTest.txt";
        File file = new File(fileName);
        this.resultPageAsString ="";

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //process the line
                this.resultPageAsString +=line;
            }
        }
        catch (Exception exp){
            System.out.println(exp);
        }

    }

    public void setSeleniumAuthData(WebDriver myWebDriver){

        delayInput(5);
        WebElement element = myWebDriver.findElement(By.cssSelector("input#p1"));
        element.clear();
        element.sendKeys(getCurrentTester().getFamily());

        delayInput(5);
        element = myWebDriver.findElement(By.cssSelector("input#p2"));
        element.clear();
        element.sendKeys(getCurrentTester().getName());

        delayInput(5);
        element = myWebDriver.findElement(By.cssSelector("input#p3"));
        element.clear();
        element.sendKeys(getCurrentTester().getYear());

        delayInput(2);
        element = myWebDriver.findElement(By.cssSelector("button#pbstart"));
        //element.clear();
        element.click();
    }

    public void delayInput(double seconds){
        if (seconds<1){
            seconds=1;
        }
        seconds = seconds*1000;
        addLineToConsole("delay: " + seconds);
        try {
            Thread.sleep(Math.round(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkSeleniumAuth(WebDriver myWebDriver){

        delayInput(5);
        try{
            /*WebElement element  = myWebDriver.findElement(By.cssSelector("div#my_alert div.modal-dialog div.modal-content div.modal-header div.alert-icon strong"));
            if (element.getText().compareTo("Ошибка регистрации!")==0){
                element  = myWebDriver.findElement(By.cssSelector("div#my_alert"));
                if(element.getAttribute("style").contains("display")){
                    addLineToConsole("Ошибка регистрации!");
                    return false;
                }
            }*/
            WebElement element  = myWebDriver.findElement(By.cssSelector("#startexam"));
            /*addLineToConsole("поиск элемнта с ошибкой");
            addLineToConsole(element.getAttribute("style"));*/

            /*if(element.getAttribute("style").contains("display")){
                addLineToConsole("Ошибка регистрации!");
                return false;
            }*/
        }
        catch (Exception exp){
            addLineToConsole("Ошибка регистрации!");
            addLineToConsole(exp.toString());
            return false;
        }


        return true;
    }

    public boolean pressStartExamButton(WebDriver myWebDriver){
        boolean result = false;
        try{
            WebElement element = myWebDriver.findElement(By.cssSelector("button#startexam"));
            element.click();
            result = true;
            addLineToConsole("pressStratExamButton");
        }
        catch (Exception exp){
            addLineToConsole("Not found button element");
            addLineToConsole(exp.toString());
        }


        return result;
    }

    public boolean pressExitExamButton(WebDriver myWebDriver){
        boolean result = false;
        try{
            WebElement element = myWebDriver.findElement(By.cssSelector("button#pbstart"));
            element.click();
            result = true;
            addLineToConsole("pressExitExamButton");
        }
        catch (Exception exp){
            addLineToConsole("Not found button element");
            addLineToConsole(exp.toString());
        }


        return result;
    }

    public void genQuestionFromRawFile(){
        String fileName = "C:\\raw_questions.txt";
        File file = new File(fileName);
        List<Question> listQuestions = new ArrayList<Question>();

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            Pattern WORD_PATTERN = Pattern.compile("\\w");
            //String str = "\+";
            Pattern ANSWER_PATTERN = Pattern.compile("^\\+");
            Question question = null;
            int indLine = 0;
            int indMfind=0;
            while((line = br.readLine()) != null){
                System.out.println(line);
                indLine++;

                Matcher mNotNull = WORD_PATTERN.matcher(line);
                Matcher mAnswer = ANSWER_PATTERN.matcher(line);
                if(line.length()>3){
                    indMfind++;
                    System.out.println("mNotNull");

                    if(mAnswer.find()){
                        System.out.println("mAnswer.find");
                        Answer answer = new Answer();
                        line = line.replaceAll("^\\+\\s","");
                        line = line.replaceAll("^\\+","");
                        answer.setText(line);
                        question.getAnswers().add(answer);
                    }
                    else{
                        System.out.println("question");
                        question = new Question();
                        line = line.replace("^\\s","");
                        question.setText(line);
                    }

                }
                else{
                    if(question!=null){
                        listQuestions.add(question);
                        question = null;
                    }
                }
            }
        }
        catch (Exception exp){
            exp.printStackTrace();
        }

        Date date = new Date();
        String millis = String.valueOf(date.getTime());

        XMLReader genQuestionsFile = new XMLReader("gen-questions.xml","conf", "questions");
        genQuestionsFile.removeSpacesFromXml();

        for(Question question: listQuestions){
            genQuestionsFile.setDoc(question.setDocumetStructure(genQuestionsFile.getDoc()));
        }
        genQuestionsFile.writeXML();

    }

    @FXML
    void startGenQuestions(MouseEvent event) {
        genQuestionFromRawFile();
    }

    public void setLblDopDelay(double delay){
        String textDelay= String.format("%.2f", delay);
        this.lblDopDelayAfterTest.setText(String.valueOf(textDelay) + " сек.");
    }

}
