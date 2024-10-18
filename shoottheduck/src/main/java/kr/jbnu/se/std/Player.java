package kr.jbnu.se.std;

import java.io.*;

/**
 * Player class to manage player's scores.
 */
public class Player {
    private int highestScore;
    private int currentScore;
    private double comboMultiplier; // 콤보 배수
    private int comboCount; // (추가된 부분) 콤보 횟수 관리
    private static final String SCORE_FILE = "highestScore.txt";

    public Player() {
        this.highestScore = loadHighestScore();
        this.currentScore = 0;
        this.comboMultiplier = 1.0;
        this.comboCount = 0; // 초기화
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int score) {
        this.currentScore = score;
        updateHighestScore();
    }

    public void addScore(int baseScore, boolean duckHit) {
        // 콤보 계산
        if (duckHit) {
            if (comboCount > 0) { // 콤보가 이미 있는 경우
                comboMultiplier *= 1.15; // 콤보를 유지하며 증가
            } else {
                comboMultiplier = 1.0; // 첫 번째 발사일 경우 콤보 배수는 1.0
            }
            comboCount++; // 콤보 횟수 증가
        } else {
            comboMultiplier = 1.0; // 오리를 맞추지 못한 경우 콤보 초기화
            comboCount = 0; // 콤보 횟수 초기화
        }

        // 콤보 점수를 반영한 최종 점수 계산
        int comboScore = (int) (baseScore * comboMultiplier); // 기본 점수에 콤보 배수 적용

//        if (duckHit) {
            currentScore += comboScore; // 현재 점수에 콤보 점수를 반영
//        } else {
//            currentScore += baseScore; // 오리를 놓친 경우 기본 점수만 추가
//        }

        // 최고 점수 업데이트
        updateHighestScore();
    }


    public void incrementCombo() {
        comboCount++;
    }

    public void resetCombo() {
        comboCount = 0;
    }

    private void updateHighestScore() {
        if (currentScore > highestScore) {
            highestScore = currentScore;
            saveHighestScore();
        }
    }

    private void saveHighestScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(highestScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadHighestScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public void resetCurrentScore() {
        this.currentScore = 0;
    }

    public int getScore() {
    return currentScore;
    }
}