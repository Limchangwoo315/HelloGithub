package kr.jbnu.se.std;

public class DuckStunManager {
    private boolean isStunned = false;
    private long stunnedStartTime = 0;
    private static final long STUN_DURATION = 1500000000L; // 1.5초
    private int originalSpeed;

    public DuckStunManager() {
        this.isStunned = false;
    }

    public void stun(int currentSpeed) {
        if (!isStunned) {
            isStunned = true;
            stunnedStartTime = System.nanoTime();
            originalSpeed = currentSpeed;
        }
    }

    public void updateStunStatus(DuckSpeedManager speedManager) {
        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
            isStunned = false;
            speedManager.setSpeed(originalSpeed);
        }
    }

    public boolean isStunned() {
        return isStunned;
    }
}
