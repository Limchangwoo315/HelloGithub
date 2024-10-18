package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Game {

    private Random random;
    private Font font;
    private ArrayList<Duck> ducks;
    private int runawayDucks;
    private int killedDucks;
    private int score;
    private int highestScore; // 최고 점수 저장
    private int shoots;
    private long lastTimeShoot;
    private long timeBetweenShots;
    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage sightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;
    private Player player = new Player(); // 플레이어 객체
    private boolean firstShot = true;
    private int level; // 사용자가 선택한 레벨 저장

    // 생성자: 레벨 선택과 초기화
    public Game() {
        selectLevel(); // 레벨 선택
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    // 레벨 선택 창을 띄우는 메서드
    private void selectLevel() {
        String[] options = { "1", "2", "3", "4", "5" };
        String selectedLevel = (String) JOptionPane.showInputDialog(
                null, "Select Level:", "Level Selection",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (selectedLevel != null) {
            level = Integer.parseInt(selectedLevel);
        } else {
            level = 1; // 기본값
        }
        System.out.println("Selected Level: " + level);
    }

    // 게임 초기화
    private void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        ducks = new ArrayList<>();
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        highestScore = 0;
        shoots = 0;
        lastTimeShoot = 0;

        // 레벨에 따른 오리 생성 간격 조정 (최소 나누기 1 보장)
        timeBetweenShots = Framework.secInNanosec / Math.max(1, (5 - level));

        player.resetCurrentScore();
    }

    // 게임 리소스 로드
    private void LoadContent() {
        try {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 게임 재시작
    public void RestartGame() {
        ducks.clear();
        Duck.lastDuckTime = 0;
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;
        lastTimeShoot = 0;

        player.resetCurrentScore();
    }

    // 게임 업데이트
    public void UpdateGame(long gameTime, Point mousePosition) {
        // 오리 생성 간격이 지났는지 확인하고 새 오리 추가
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            int duckSpeed = level; // 레벨에 따라 오리 속도 설정

            ducks.add(new Duck(
                    Framework.frameWidth, // 시작 X 좌표 (화면 오른쪽 끝)
                    random.nextInt(Framework.frameHeight - 100), // 랜덤한 Y 좌표
                    -duckSpeed, // 왼쪽으로 이동하는 음수 속도
                    10, // 오리 점수 (예시)
                    duckImg // 오리 이미지
            ));

            Duck.lastDuckTime = System.nanoTime();
        }

        // 각 오리 업데이트 및 경계 확인
        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);
            duck.Update();

            // 화면 왼쪽을 벗어나면 제거
            if (duck.x < 0 - duckImg.getWidth()) {
                ducks.remove(i);
                runawayDucks++;
            }
        }

        // 마우스 클릭 상태 확인
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            boolean duckHit = false;

            // 오리 명중 확인
            for (int i = 0; i < ducks.size(); i++) {
                Duck duck = ducks.get(i);

                // 오리의 히트박스 생성
                Rectangle hitBox = new Rectangle(duck.x, duck.y, duckImg.getWidth(), duckImg.getHeight());

                // 마우스 포인터가 히트박스 내에 있는지 확인
                if (hitBox.contains(mousePosition)) {
                    killedDucks++; // 오리 명중 시 증가
                    score += duck.getScore(); // 점수 증가
                    player.addScore(duck.getScore(), true); // 플레이어 점수 업데이트
                    ducks.remove(i); // 오리 제거
                    duckHit = true;
                    break;
                }
            }

            // 오리를 명중하지 못한 경우 콤보 초기화
            if (!duckHit) {
                player.addScore(0, false);
                player.resetCombo();
            }

            // 다음 클릭을 위해 클릭 상태 초기화
            lastTimeShoot = System.nanoTime();
        }

        // 게임 오버 조건 확인
        if (runawayDucks >= 200) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }


    // 게임 그리기
    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        for (Duck duck : ducks) {
            duck.Draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + player.getCurrentScore(), 440, 21);
        g2d.drawString("HIGHEST SCORE: " + player.getHighestScore(), 580, 21);
    }

    public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
        Draw(g2d, mousePosition);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, Framework.frameHeight / 2);
    }
}
