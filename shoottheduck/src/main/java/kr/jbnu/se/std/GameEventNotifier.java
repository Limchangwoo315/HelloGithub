package kr.jbnu.se.std;

// Subject interface for managing observers
public interface GameEventNotifier {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyScoreChanged(int newScore);
    void notifyGameStateChanged(String newState);
}