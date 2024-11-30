package kr.jbnu.se.std;

public class DuckSpeedManager {
    // 속도 관련 상수
    private static final int SPEED_FAST = -5;
    private static final int SPEED_MEDIUM = -4;
    private static final int SPEED_SLOW = -3;
    private static final int SPEED_VERY_SLOW = -2;

    // y 위치에 따른 범위 설정
    private static final double[] Y_THRESHOLDS = {0.15, 0.30, 0.58, 0.65, 0.70};

    private int speed;

    public DuckSpeedManager(int initialSpeed) {
        this.speed = initialSpeed;
    }

    public void adjustSpeedBasedOnY(int y, double frameHeight) {
        speed = determineSpeed(y, frameHeight);
    }

    private int determineSpeed(int y, double frameHeight) {
        if (y < frameHeight * Y_THRESHOLDS[0]) {
            return SPEED_FAST;
        } else if (y < frameHeight * Y_THRESHOLDS[1]) {
            return SPEED_MEDIUM;
        } else if (y < frameHeight * Y_THRESHOLDS[2]) {
            return SPEED_MEDIUM;
        } else if (y < frameHeight * Y_THRESHOLDS[3]) {
            return SPEED_SLOW;
        } else if (y < frameHeight * Y_THRESHOLDS[4]) {
            return SPEED_SLOW;
        } else {
            return SPEED_VERY_SLOW;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
