package kr.jbnu.se.std;

public class DuckState {
    private boolean isStunned = false;
    private long stunnedStartTime = 0;
    private static final long STUN_DURATION = 1500000000L; // 1.5초
    private int originalSpeed;

    // 기절 상태 처리
    public void stun(int speed) {
        if (!isStunned) {
            isStunned = true;
            stunnedStartTime = System.nanoTime();
            originalSpeed = speed;
        }
    }

    public void updateStunStatus(int speed) {
        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
            isStunned = false;
        }
    }

    public boolean isStunned() {
        return isStunned;
    }

    public void resetSpeed(int originalSpeed) {
        this.originalSpeed = originalSpeed;
    }

    public int getOriginalSpeed() {
        return originalSpeed;
    }
}