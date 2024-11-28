package kr.jbnu.se.std;

// Observer interface to be implemented by classes receiving updates
public interface GameObserver {
    void onScoreChanged(int newScore);
    void onGameStateChanged(String newState);
}