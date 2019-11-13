package com.example.scoping_project;

public class Score {
    String score;
    String teamName;

    @Override
    public String toString() {
        return "Score{" +
                "score='" + score + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    public Score(String score, String teamName) {
        this.score = score;
        this.teamName = teamName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
