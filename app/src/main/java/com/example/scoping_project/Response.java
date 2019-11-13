package com.example.scoping_project;

public class Response {
    public int questionId;
    public int responseId;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public Response(int questionId, int responseId) {
        this.questionId = questionId;
        this.responseId = responseId;
    }
}
