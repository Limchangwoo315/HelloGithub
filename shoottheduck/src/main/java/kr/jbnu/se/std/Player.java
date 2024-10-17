package kr.jbnu.se.std;

import java.io.*;

/**
 * Player class to manage player's scores.
 */
public class Player {
    private int highestScore;
    private int currentScore;
    private static final String SCORE_FILE = "highestScore.txt";

    public Player() {
        this.highestScore = loadHighestScore(); // 게임 시작 시 최고 점수를 불러옴
        this.currentScore = 0;
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

    private void updateHighestScore() {
        if (currentScore > highestScore) {
            highestScore = currentScore;
            saveHighestScore(); // 최고 점수가 갱신되면 파일에 저장
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
            // 파일이 없거나, 형식이 잘못된 경우 0으로 초기화
            return 0;
        }
    }

    public void resetCurrentScore() {
        this.currentScore = 0;
    }
}
