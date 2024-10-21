package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Sight {
    private BufferedImage sightImage;
    private int x, y;
    private float scale;
    private boolean isScaling;
    private long startTime;

    public Sight() {
        loadImage();
        this.scale = 1.0f; // 초기 크기 비율
        this.isScaling = false;
    }

    private void loadImage() {
        try {
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImage = ImageIO.read(sightImgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        // isScaling이 true일 때만 이미지를 그립니다.
        if (isScaling && sightImage != null) {
            int width = (int) (sightImage.getWidth() * scale);
            int height = (int) (sightImage.getHeight() * scale);
            g2d.drawImage(sightImage, x - width / 2, y - height / 2, width, height, null);
        }
    }

    public void startScaling(int mouseX, int mouseY) {
        this.x = mouseX;
        this.y = mouseY;
        this.scale = 1.0f; // 크기를 초기화
        this.isScaling = true;
        this.startTime = System.nanoTime();
    }

    public void update() {
        if (isScaling) {
            // 크기를 증가시키고, 0.35초가 지나면 스케일을 원래 크기로 줄입니다.
            long elapsed = System.nanoTime() - startTime;
            if (elapsed < 350_000_000L) { // 0.35초
                scale = 1.5f; // 원하는 크기로 변경 (1.5배 예시)
            } else {
                scale = 1.0f; // 원래 크기로 되돌림
                isScaling = false; // 스케일링 종료
            }
        }
    }

    public boolean isScaling() {
        return isScaling;
    }
}