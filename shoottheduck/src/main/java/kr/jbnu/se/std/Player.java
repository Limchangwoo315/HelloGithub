package kr.jbnu.se.std;

/**
 * Player class to manage player's scores.
 */
public class Player {
    private int highestScore;
    private int currentScore;

    public Player() {
        this.highestScore = 0;
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
        }
    }

    public void resetCurrentScore() {
        this.currentScore = 0;
    }
}

