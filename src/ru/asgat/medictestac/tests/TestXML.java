package ru.asgat.medictestac.tests;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;
import ru.asgat.medictestac.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Skif on 23.08.2017.
 */
public class TestXML extends TestCase {
    private String xmlConfigString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><settings><protocols><proto name=\"http\">true</proto><proto name=\"https\">false</proto></protocols><hostname>mail.nic.ru</hostname><autoanswer>993</autoanswer><mintimout>5</mintimout><maxtimout>15</maxtimout><folder>INBOX</folder></settings>";
    private XMLReader configFile;

    private static Question questionForm;
    private static List<Question> listQuestionWisthAnswer;
    private static String pageAsString;

    private static Worker tester;

    List<String> lstBasePaths;
    private static List<Question> listQuestionFromRawFile;


    public void testXmlConfigString(){
        boolean isEmpty = xmlConfigString.isEmpty();
        assertFalse(isEmpty);


    }

    public void testDirs(){
        this.lstBasePaths = new ArrayList<String>();
        this.lstBasePaths.add("user.home");
        this.lstBasePaths.add("user.dir");

        for (String base: lstBasePaths) {
            String basePath = System.getProperty(base);
            System.out.println(basePath);
            assertNotNull(basePath);
            assertFalse(basePath.isEmpty());
        }
    }

    public void testFindFilePath(){
        //assertNotNull(configFile.findFilePath());
    }

    /*public void testList(){
        List<String> list = new ArrayList<String>();
        System.out.println("size: " + list.size());
        assertTrue(list.size()==0);
    }*/

    public static void genDataForAllVariables(){
        //
        listQuestionWisthAnswer = new ArrayList<Question>();
        Question question = new Question();
        question.setText("Назовите условия хранения чистого и грязного белья.");
        question.setAnswers(new ArrayList<Answer>());
        question.getAnswers().add(new Answer("","В раздельных помещениях"));
        question.getAnswers().add(new Answer("","Чистое белье хранится в закрытых шкафах, стеллажах"));
        question.getAnswers().add(new Answer("","Чистое белье хранится на открытых полках в индивидуальной упаковке"));
        question.getAnswers().add(new Answer("","Грязное белье хранится в закрытой таре на стеллажах"));
        listQuestionWisthAnswer.add(question);

        question = new Question();
        question.setText("Каким образом осуществляется хранение чистого белья на открытых стеллажах?");
        question.setAnswers(new ArrayList<Answer>());
        question.getAnswers().add(new Answer("","в индивидуальной упаковке"));
        listQuestionWisthAnswer.add(question);

        question = new Question();
        question.setText("Назовите требования к хранению грязного белья.");
        question.setAnswers(new ArrayList<Answer>());
        question.getAnswers().add(new Answer("","В закрытых мешках на напольных стеллажах"));
        listQuestionWisthAnswer.add(question);

        question = new Question();
        question.setText("Назовите требования к оборудованию бельевых.");
        question.setAnswers(new ArrayList<Answer>());
        question.getAnswers().add(new Answer("","Напольные стеллажи или деревянные лари для хранения белья"));
        question.getAnswers().add(new Answer("","Раковина с подводкой горячей воды"));
        question.getAnswers().add(new Answer("","Верно все перечисленное"));
        listQuestionWisthAnswer.add(question);

        questionForm = new Question();
        questionForm.setText("Назовите требования к оборудованию бельевых для грязного белья.");
        questionForm.setAnswers(new ArrayList<Answer>());
        questionForm.getAnswers().add(new Answer("a1a","Напольные стеллажи или деревянные лари для хранения белья"));
        questionForm.getAnswers().add(new Answer("a1b","Хрень"));
        questionForm.getAnswers().add(new Answer("a1c","Раковина с подводкой горячей воды"));
        questionForm.getAnswers().add(new Answer("a1d","Верно все перечисленное"));
        questionForm.getAnswers().add(new Answer("a1e","Где-то, что-то такое"));

        tester = new Worker();
        tester.setFamily("Иванов");
        tester.setName("Иван");
        tester.setYear("1993");

    }

    @Test
    public static void testQuestionCompare(){

        genDataForAllVariables();

        System.out.println("listQuestionWisthAnswer: " + listQuestionWisthAnswer.size());
        System.out.println(questionForm.getText());
        ExamWorker exam = new ExamWorker(questionForm, listQuestionWisthAnswer, 70.0);

        List<String> ids = exam.startTestAndGetIDs();
        System.out.println("exam ids: " + ids.size());

        for (String id: ids) {
            System.out.println("id: " + id );
        }
        boolean result = ids.size()>0;
        System.out.println(result);
        assertTrue(result);
    }


    public static void readHTMLpageAsString(){
        String fileName = "D:\\Projects\\TesterServer\\stringFromSelemiumGetContentExamPage.txt";
        File file = new File(fileName);
        pageAsString ="";

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                //process the line
                pageAsString +=line;
            }
        }
        catch (Exception exp){
            System.out.println(exp);
        }

    }

    @Test
    public void testHtmlPageAsString(){
        readHTMLpageAsString();
        //assertNotNull(pageAsString);
        assertTrue(pageAsString.compareTo("")!=0);

        System.out.println(pageAsString);
    }

    public static void readRawTXT(){
        String fileName = "D:\\Projects\\TesterServer\\raw_questions.txt";
        File file = new File(fileName);
        List<Question> listQuestions = new ArrayList<Question>();

        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            Pattern WORD_PATTERN = Pattern.compile("\\w+");
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
                if(mNotNull.find()){
                    indMfind++;
                    System.out.println("mNotNull");
                    if(mAnswer.find()){
                        System.out.println("mAnswer.find");
                        Answer answer = new Answer();
                        line = line.replace("^\\+\\s","");
                        line = line.replace("^\\+","");
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
                    /*if(question!=null){
                        listQuestions.add(question);
                        question = null;
                    }*/
                }

            }
            System.out.println(indLine);
            System.out.println(indMfind);


        }
        catch (Exception exp){
            System.out.println(exp);
        }

        listQuestionFromRawFile = listQuestions;

    }

    @Test
    public void testListRawFileQuestions(){
        readRawTXT();
        System.out.println("listQuestionFromRawFile" + listQuestionFromRawFile.size());
        assertTrue(listQuestionFromRawFile!=null);
    }


}
