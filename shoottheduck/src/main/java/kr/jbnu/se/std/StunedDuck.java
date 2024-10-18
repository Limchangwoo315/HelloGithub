//package kr.jbnu.se.std;
//
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//
//public class Duck {
//    // 기존 코드...
//
//    private boolean isStunned = false; // 기절 여부를 나타내는 변수
//    private long stunnedStartTime = 0; // 기절 시작 시간
//    private static final long STUN_DURATION = 1500000000L; // 1.5초 (나노초)
//
//    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
//        this.x = x;
//        this.y = y;
//        this.speed = speed;
//        this.score = score;
//        this.duckImg = duckImg;
//    }
//
//    /**
//     * 오리를 기절시킴.
//     */
//    public void stun() {
//        isStunned = true;
//        stunnedStartTime = System.nanoTime();
//    }
//
//    /**
//     * 기절 상태를 업데이트하고, 기절 시간이 지나면 기절을 해제합니다.
//     */
//    public void updateStunStatus() {
//        if (isStunned && System.nanoTime() - stunnedStartTime >= STUN_DURATION) {
//            isStunned = false;
//        }
//    }
//
//    /**
//     * 오리를 업데이트합니다.
//     */
//    public void Update() {
//        updateStunStatus();
//
//        if (!isStunned) {
//            x += speed;
//        }
//    }
//
//    // 나머지 기존 코드...
//}
