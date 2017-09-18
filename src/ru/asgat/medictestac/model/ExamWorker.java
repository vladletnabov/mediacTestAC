package ru.asgat.medictestac.model;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.asgat.medictestac.controller.SettingsController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Skif on 31.08.2017.
 */
public class ExamWorker {
    private List<Question> listQuestionsWA; //список вопросов с ответами
    private Question questionForm; //список вопросов из опросной формы
    private Double coincedence; // % совпадения вопросов при сравнении
    private int numShingles = 2;
    private static final Logger logger = Logger.getLogger( XMLReader.class.getName() );
    private SettingsController controller = null;
    private boolean useAlgoOld;

    //private Worker tester;

    public ExamWorker(Question question, List<Question> listQuestionWithAnswers, Double coincedence, boolean useAlgoOld){
        /*
        * question тестируемый вопрос
        * listQuestionWithAnswers - список вопросов с ответами
        * coincedence - точность при проверке предложений
        *
        * */
        this.questionForm = question;
        this.listQuestionsWA = listQuestionWithAnswers;
        this.coincedence = coincedence;
        this.useAlgoOld = useAlgoOld;
        //this.tester = tester;
    }

    public ExamWorker(Question question, List<Question> listQuestionWithAnswers, Double coincedence){
        /*
        * question тестируемый вопрос
        * listQuestionWithAnswers - список вопросов с ответами
        * coincedence - точность при проверке предложений
        *
        * */
        this.questionForm = question;
        this.listQuestionsWA = listQuestionWithAnswers;
        this.coincedence = coincedence;
        //this.tester = tester;

        this.useAlgoOld = true;
    }
    public ExamWorker(Question question, List<Question> listQuestionWithAnswers){
        this.questionForm = question;
        this.listQuestionsWA = listQuestionWithAnswers;
        this.coincedence = 70.0;
        //this.tester = tester;
        this.useAlgoOld = true;
    }

    public boolean simpleCompareStrings(String str1, String str2){
        str1 = str1.replace("\\s+","");
        str2 = str2.replace("\\s+","");
        if (str1.compareTo(str2)==0){
            return true;
        }
        return false;
    }
    public boolean shingleCompareStrings(String str1, String str2){
        Shingle shingle = new Shingle();
        shingle.setSHINGLE_LEN(getNumShingles());

        Double result = shingle.compare(shingle.genShingle(str1),shingle.genShingle(str2));
        //System.out.println("shingleCompare result: " + result);

        if(result> getCoincedence()){
            //System.out.print("shingleCompareStrings: Ok! (" + result + ")");
            return true;
        }


        return false;
    }

    public List<String> startTestAndGetIDs(){
        Question question = checkQuestionWithAnswer();
        if (question!=null){
            return listAnswerIDs(question);
        }
        return (new ArrayList<String>());
    }

    public Question checkQuestionWithAnswer(){
        if (useAlgoOld){
            return checkQuestionWithAnswerOldAlgorithm();
        }
        else{
            return checkQuestionWithAnswerNewAlgorithm();
        }
    }

    public Question checkQuestionWithAnswerNewAlgorithm(){
        logExam("checkQuestionWithAnswerNewAlgorithm");
        // Возвращает ID вопроса из формы
        Question result = null;
        logExam(".....................examining: " + getQuestionForm().getIdNode());
        for(int i=0; i<getListQuestionsWA().size(); i++){
            Question question = getListQuestionsWA().get(i);
            String addQuestion="Назовите все правильные ответы: ";
            String questionForm = getQuestionForm().getText().trim();
            questionForm = removeSimpleSymbolsFromStr(questionForm);
            questionForm = questionForm.trim();
            String questionWA = question.getText().trim();
            questionWA = removeSimpleSymbolsFromStr(questionWA);
            questionWA = questionWA.trim();
            String questionWAadd = (addQuestion + questionWA).trim();
            if(simpleCompareStrings(questionForm, questionWA)){
                logExam("QuestionText(simple compare): " + questionWA);
                logExam("QuestionText(simple compare) id: " + getQuestionForm().getIdNode());
                question.setIdNode(getQuestionForm().getIdNode());
                result = getWriteQuestionWithAnswers(question);
                break;
            }
            if(simpleCompareStrings(questionForm, questionWAadd)){
                logExam("QuestionText(simple compare) add: " + questionWAadd);
                logExam("QuestionText(simple compare) id: " + getQuestionForm().getIdNode());
                question.setIdNode(getQuestionForm().getIdNode());
                result = getWriteQuestionWithAnswers(question);
                break;
            }
        }
        if (result==null){
            for(int i=0; i<getListQuestionsWA().size(); i++){
                Question question = getListQuestionsWA().get(i);
                String addQuestion="Назовите все правильные ответы: ";
                String questionForm = getQuestionForm().getText().trim();
                questionForm = removeSimpleSymbolsFromStr(questionForm);
                questionForm = questionForm.trim();
                String questionWA = question.getText().trim();
                questionWA = removeSimpleSymbolsFromStr(questionWA);
                questionWA = questionWA.trim();
                String questionWAadd = (addQuestion + questionWA).trim();
                if (shingleCompareStrings(questionForm, questionWA)){
                    logExam("QuestionText(shingle compare): " + questionWA);
                    logExam("QuestionText(simple compare) id: " + getQuestionForm().getIdNode());
                    question.setIdNode(getQuestionForm().getIdNode());
                    result = getWriteQuestionWithAnswers(question);
                    break;
                }
                if (shingleCompareStrings(questionForm, questionWAadd )){
                    logExam("QuestionText(shingle compare) add: " + questionWAadd);
                    logExam("QuestionText(simple compare) id: " + getQuestionForm().getIdNode());
                    question.setIdNode(getQuestionForm().getIdNode());
                    result = getWriteQuestionWithAnswers(question);
                    break;
                }
            }
        }

        return result;
    }

    public Question checkQuestionWithAnswerOldAlgorithm(){
        logExam("checkQuestionWithAnswerOldAlgorithm");
        // Возвращает ID вопроса из формы
        Question result = null;
        //logExam(" getListQuestionsWA().size: " +  getListQuestionsWA().size());
        for(int i=0; i<getListQuestionsWA().size(); i++){
            Question question = getListQuestionsWA().get(i);
            //System.out.print("QUESTION: ");
            //System.out.println(question.getText());
            //logExam(String.valueOf(i) + "getListQuestionsWA.size: " + String.valueOf(getListQuestionsWA().size()));
            //logExam("QUESTIONFORM: " + getQuestionForm().getText());
            //logExam("QUESTION: " + question.getText());
            String addQuestion="Назовите все правильные ответы: ";
            String questionForm = getQuestionForm().getText().trim();
            questionForm = removeSimpleSymbolsFromStr(questionForm);
            questionForm = questionForm.trim();
            String questionWA = question.getText().trim();
            questionWA = removeSimpleSymbolsFromStr(questionWA);
            questionWA = questionWA.trim();
            String questionWAadd = (addQuestion + questionWA).trim();
            if(simpleCompareStrings(questionForm, questionWA)){
                logExam("QuestionText(simple compare): " + questionWA);
                result = getWriteQuestionWithAnswers(question);
                break;
            }
            if(simpleCompareStrings(questionForm, questionWAadd)){
                logExam("QuestionText(simple compare) add: " + questionWAadd);
                result = getWriteQuestionWithAnswers(question);
                break;
            }
            if (shingleCompareStrings(questionForm, questionWA)){
                logExam("QuestionText(shingle compare): " + questionWA);
                result = getWriteQuestionWithAnswers(question);
                break;
            }
            if (shingleCompareStrings(questionForm, questionWAadd )){
                logExam("QuestionText(shingle compare) add: " + questionWAadd);
                result = getWriteQuestionWithAnswers(question);
                break;
            }
        }
        return result;
    }

    public String removeSimpleSymbolsFromStr(String str){
        str = str.replace(":",": ");
        str = str.replace("^\\+","");
        str = str.replace("«","\\");
        str = str.replace("»","\\");
        return str;
    }

    public Question getWriteQuestionWithAnswers(Question question){
        logExam("getWriteQuestionWithAnswers: проверка ответов у вопроса ");
        List <Answer> listRightAnswers = getRightAnswerIDs(getQuestionForm().getAnswers(), question.getAnswers());
        if(listRightAnswers.size()==0){
            return null;
        }
        question.setAnswers(listRightAnswers);
        logExam("getWriteQuestionWithAnswers: Вопрос: " + question.getText());
        for(Answer answer: question.getAnswers()){
            logExam("getWriteQuestionWithAnswers: ответ:  id=" + answer.getId() + " . "+ answer.getText());
        }

        return question;
    }

    public List<String> listAnswerIDs(Question question){
        List<String> listIDs = new ArrayList<String>();
        System.out.println("listIDs.size: " + listIDs.size());
        for(Answer answerForm: questionForm.getAnswers()){
            for(Answer answer: question.getAnswers()){
                if (compareQuestionText(answerForm.getText(), answer.getText())){
                    listIDs.add(answerForm.getId());
                }
            }
        }

        return listIDs;
    }

    public List<Answer> getRightAnswerIDs(List<Answer> listAnswersForm, List<Answer> listAnswersBase){
        // Сравненеи ответов с формы с записанными в базу ответами правильными ответами на вопрос
        List<Answer> formAnswers = listAnswersForm;
        List<Answer> baseAnswers = listAnswersBase;
        List<Answer> resultAnswersWithIDs = new ArrayList<Answer>();
        List<String> okAll = new ArrayList<String>();
        okAll.add("Верно все");
        okAll.add("Верно всё");
        okAll.add("Верно все перечисленное");
        okAll.add("Верно всё перечисленное");
        okAll.add("Всё перечисленное");
        okAll.add("Все перечисленное");

        for(Answer formAnswer: formAnswers){
            //перебираем все  вопросы формы и сравниваем по очередит с вопросами из базы
            for(Answer rightAnswer: baseAnswers){
                String formText = formAnswer.getText().trim();
                formText = removeSimpleSymbolsFromStr(formText);
                String rightText = rightAnswer.getText().trim();
                rightText = removeSimpleSymbolsFromStr(rightText);

                if(simpleCompareStrings(formText, rightText)){
                    resultAnswersWithIDs.add(formAnswer);
                    logExam("answerText(simple compare): " + formAnswer.getText());
                    break;
                }
                if (shingleCompareStrings(formText, rightText)){
                    resultAnswersWithIDs.add(formAnswer);
                    logExam("answerText(shingle compare): " + formAnswer.getText());
                    break;
                }
            }

        }

        logExam("Проверка на наличие ответа ВСЁ ВЕРНО/ВЕРНО ВСЁ ПЕРЕЧИСЛЕННОЕ");
        if(formAnswers.size()==resultAnswersWithIDs.size()){
            Answer resultAnswer = null;
            for(Answer resAnswer: resultAnswersWithIDs){
                String checkText = resAnswer.getText().trim();
                checkText = removeSimpleSymbolsFromStr(checkText);
                for(String rightAll: okAll){
                    String rightText = rightAll.trim();
                    rightText = removeSimpleSymbolsFromStr(rightText);

                    if(simpleCompareStrings(checkText, rightText)){
                        resultAnswer = resAnswer;
                        break;
                    }
                    if (shingleCompareStrings(checkText, rightText)){
                        resultAnswer = resAnswer;
                        break;
                    }
                }
            }
            if(resultAnswer!=null){
                resultAnswersWithIDs = new ArrayList<Answer>();
                resultAnswersWithIDs.add(resultAnswer);
            }
            logExam("Убраны лишние ответы. Оставлен только ВСЁ ВЕРНО/ВЕРНО ВСЁ ПЕРЕЧИСЛЕННОЕ");
        }
        else{
            List<Answer> resultAnswers = new ArrayList<Answer>();
            for(Answer resAnswer: resultAnswersWithIDs){
                String checkText = resAnswer.getText().trim();
                checkText = removeSimpleSymbolsFromStr(checkText);
                for(String rightAll: okAll){
                    String rightText = rightAll.trim();
                    rightText = removeSimpleSymbolsFromStr(rightText);

                    if(!simpleCompareStrings(checkText, rightText)){
                        resultAnswers.add(resAnswer);
                        break;
                    }
                    if (!shingleCompareStrings(checkText, rightText)){
                        resultAnswers.add(resAnswer);
                        break;
                    }
                }
            }
            resultAnswersWithIDs = resultAnswers;
            logExam("Присуствуют неправильные ответы в списке с формы экзамена. " +
                    "Удалён ответ ВСЁ ВЕРНО/ВЕРНО ВСЁ ПЕРЕЧИСЛЕННОЕ");
        }
        logExam("Ответы на вопрос сформированы. Если ответа нет, значит вопрос будет пропущен. " +
                "При включённой автогенерации сформируется в случайном порядке.");

        return  resultAnswersWithIDs;
    }

    public boolean compareQuestionText(String questionForm, String questionWA){
        //Заглушка для алгоритма сравнения
        boolean result = false;
        if(questionForm.compareTo(questionWA)==0){
            result=true;
        }
        return result;
    }


    public List<Question> getListQuestionsWA() {
        return listQuestionsWA;
    }

    public void setListQuestionsWA(List<Question> listQuestionsWA) {
        this.listQuestionsWA = listQuestionsWA;
    }

    public Question getQuestionForm() {
        return questionForm;
    }

    public void setQuestionForm(Question questionForm) {
        this.questionForm = questionForm;
    }

    public Double getCoincedence() {
        return coincedence;
    }

    public void setCoincedence(Double coincedence) {
        this.coincedence = coincedence;
    }

    public int getNumShingles() {
        return numShingles;
    }

    public void setNumShingles(int numShingles) {
        this.numShingles = numShingles;
    }

    public SettingsController getController() {
        return controller;
    }

    public void setController(SettingsController controller) {
        this.controller = controller;
    }

    public List<Question> getListExamFormQuestios(Document doc, NodeList nodeList){
        List<Question> listExamQuestion = new ArrayList<Question>();

        return listExamQuestion;
    }

    public void logExam(String str){
        if(controller!=null){
            controller.addLineToConsole("logExam: " + str);
        }
        else{
            System.out.println("logExam: " + str);
        }
        //logger.info(str);

    }
}
