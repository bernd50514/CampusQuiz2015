package trainingsspiel.bean;

public class SaveQuestionInfo {

    private Integer questionId;
    private String questionType;
    private String realAnswer;
    private String is_correct;
    private Integer score;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getRealAnswer() {
        return realAnswer;
    }

    public void setRealAnswer(String realAnswer) {
        this.realAnswer = realAnswer;
    }

    public String getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(String is_correct) {
        this.is_correct = is_correct;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String toString() {
        return "{'question_id':'" + getQuestionId() + "','question_type':'" + getQuestionType() + "','realAnswer':'" + getRealAnswer() + "','is_correct':'" + getIs_correct() + "','score':'" + getScore() + "'}";
    }

}
