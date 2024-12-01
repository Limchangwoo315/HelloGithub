package kr.jbnu.se.std;

import java.io.*;

/**
 * Player class to manage player's scores.
 */
public class PlayerScore {
    private int highestScore;
    private int currentScore;
    private double comboMultiplier;
    private int comboCount;
    private static final String SCORE_FILE = "highestScore.txt";  // 최고 점수를 저장할 파일

    public PlayerScore() {
        this.highestScore = loadHighestScore();  // 최고 점수 로드
        this.currentScore = 0;
        this.comboMultiplier = 1.0;
        this.comboCount = 0;
    }

    // 현재 최고 점수 반환
    public int getHighestScore() {
        return highestScore;
    }

    // 현재 점수 반환
    public int getCurrentScore() {
        return currentScore;
    }

    // 점수 추가: 보스와 일반 오리를 구분
    public void addScore(int baseScore, boolean duckHit, boolean isBoss) {
        if (duckHit && !isBoss) {  // 보스가 아닌 오리를 맞췄을 때 콤보 증가
            comboMultiplier *= 1.15;
            comboCount++;
        } else {  // 보스거나 오리를 맞추지 못한 경우 콤보 초기화
            comboMultiplier = 1.0;
            comboCount = 0;
        }

        // 최종 점수 계산
        int comboScore = (int) (baseScore * comboMultiplier);
        currentScore += comboScore;

        updateHighestScore();  // 최고 점수 업데이트
    }

    // 콤보 초기화
    public void resetCombo() {
        comboCount = 0;
        comboMultiplier = 1.0;
    }

    // 최고 점수 업데이트
    private void updateHighestScore() {
        if (currentScore > highestScore) {
            highestScore = currentScore;
            saveHighestScore();  // 파일에 저장
        }
    }

    // 최고 점수 저장
    private void saveHighestScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(highestScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 최고 점수 로드
    private int loadHighestScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;  // 파일이 없거나 에러가 발생하면 0으로 초기화
        }
    }

    // 현재 점수 초기화
    public void resetCurrentScore() {
        this.currentScore = 0;
    }

    // 현재 점수 반환 (추가 메서드)
    public int getScore() {
        return currentScore;
    }
}