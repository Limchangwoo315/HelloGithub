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
    private GoldenDuck goldenDuck; // 황금 오리 변수 추가
    private int runawayDucks;
    private int killedDucks;
    private int shoots;
    private long lastTimeShoot;
    private long timeBetweenShots;
    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage goldenDuckImg; // 황금 오리 이미지
    private BufferedImage sightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;
    private Player player = new Player();
    private int level;
    private boolean goldenDuckSpawned = false; // 황금 오리 스폰 상태 추가
    private boolean bossSpawned = false;
    private boolean bossDefeated = false;
    private int nextBossScore = 500; // 첫 보스 등장 점수
    private final int BOSS_SCORE_INTERVAL = 5000; // 이후 보스 등장 간격
    private int currentStage = 1;

    // 생성자: 레벨 선택과 초기화
    public Game() {
        selectLevel();
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread(() -> {
            Initialize();
            LoadContent();
            Framework.gameState = Framework.GameState.PLAYING;
        });
        threadForInitGame.start();
    }

    private void selectLevel() {
        String[] options = { "1", "2", "3", "4", "5" };
        String selectedLevel = (String) JOptionPane.showInputDialog(
                null, "Select Level:", "Level Selection",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        level = (selectedLevel != null) ? Integer.parseInt(selectedLevel) : 1;
        System.out.println("Selected Level: " + level);
    }

    private void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        ducks = new ArrayList<>();
        runawayDucks = 0;
        killedDucks = 0;
        shoots = 0;
        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / Math.max(1, (5 - level));
        player.resetCurrentScore();
    }

    private void LoadContent() {
        try {
            backgroundImg = ImageIO.read(getClass().getResource("/images/background.jpg"));
            grassImg = ImageIO.read(getClass().getResource("/images/grass.png"));
            duckImg = ImageIO.read(getClass().getResource("/images/duck.png"));
            sightImg = ImageIO.read(getClass().getResource("/images/sight.png"));
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
            goldenDuckImg = loadGoldenDuckImage(); // 황금오리 이미지 로드
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        ducks.clear();
        Duck.lastDuckTime = 0;
        runawayDucks = 0;
        killedDucks = 0;
        shoots = 0;
        lastTimeShoot = 0;
        bossSpawned = false;
        bossDefeated = false;
        nextBossScore = 500; // 첫 보스 점수로 초기화
        currentStage = 1;
        player.resetCurrentScore();
    }

    public void UpdateGame(long gameTime, Point mousePosition) {

        if (player.getCurrentScore() >= nextBossScore && !bossSpawned) {
            spawnBossDuck();
            bossSpawned = true;
            bossDefeated = false;
            System.out.println("Boss spawned! Stage: " + currentStage);
        }
        // 황금오리 업데이트 및 포획 처리
        if (goldenDuck != null) {
            goldenDuck.Update();
            if (goldenDuck.getHitBox().x < 0 - goldenDuck.getImage().getWidth()) {
                goldenDuck = null; // 화면을 넘어가면 황금오리 제거
            }
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                if (goldenDuck.getHitBox().contains(mousePosition)) {
                    handleGoldenDuckCapture();
                }
            }
        }

        if (!bossSpawned && System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            spawnSmallDuck();
            Duck.lastDuckTime = System.nanoTime();
        }

        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);
            duck.Update();
            if (duck.getHitBox().x < 0 - duck.getImage().getWidth()) {
                ducks.remove(i);
                runawayDucks++;
            }
        }

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                handleClick(mousePosition);
                lastTimeShoot = System.nanoTime();
                shoots++;
            }
        }

        if (runawayDucks >= 200) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }

    private void spawnSmallDuck() {
        int speed = -(level + currentStage);
        ducks.add(new Duck(
                Framework.frameWidth,
                random.nextInt(Framework.frameHeight - 100),
                speed, 10, duckImg));
    }

    private void handleClick(Point mousePosition) {
        boolean duckHit = false;

        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);

            if (duck.getHitBox().contains(mousePosition)) {
                if (duck instanceof BossDuck) {
                    BossDuck boss = (BossDuck) duck;
                    boss.takeDamage();

                    if (boss.getHealth() <= 0) {
                        ducks.remove(i);
                        player.addScore(500, true, true);
                        System.out.println("Boss defeated!");
                        resetAfterBossDefeat();
                        spawnGoldenDuck();
                    }
                } else {
                    killedDucks++;
                    player.addScore(duck.getScore(), true, false);
                    ducks.remove(i);
                }
                duckHit = true;
                break;
            }
        }

        if (!duckHit) {
            player.addScore(0, false, false);
            player.resetCombo();
        }
    }

    private void resetAfterBossDefeat() {
        bossSpawned = false;
        currentStage++;
        nextBossScore += BOSS_SCORE_INTERVAL;
        player.resetCombo();
    }

    private void spawnBossDuck() {
        ducks.clear();
        int startY = random.nextInt(Framework.frameHeight - 300);
        ducks.add(new BossDuck(Framework.frameWidth, startY, -2, duckImg));
    }

    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        for (Duck duck : ducks) {
            duck.Draw(g2d);
        }
        if (goldenDuck != null) {
            goldenDuck.Draw(g2d);
        }
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(),
                Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth,
                mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + player.getCurrentScore(), 440, 21);
        g2d.drawString("HIGHEST SCORE: " + player.getHighestScore(), 580, 21);
        g2d.drawString("STAGE: " + currentStage, Framework.frameWidth / 2, 41);
    }

    public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
        Draw(g2d, mousePosition);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, Framework.frameHeight / 2);
    }
    private void spawnGoldenDuck() {
        if (!goldenDuckSpawned) { // 황금오리가 이미 스폰되지 않았을 때만 생성
            int speed = -5; // 적절한 속도로 설정
            goldenDuck = new GoldenDuck(Framework.frameWidth, random.nextInt(Framework.frameHeight - 100), speed, goldenDuckImg);
            goldenDuckSpawned = true; // 황금오리 스폰 플래그 설정
        }
    }

    private BufferedImage loadGoldenDuckImage() {
        try {
            return ImageIO.read(getClass().getResource("/images/goldenDuck.png"));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null; // 이미지 로드 실패 시 null 반환
        }
    }
    private void handleGoldenDuckCapture() {
        if (goldenDuck != null) {
            player.addScore(goldenDuck.getScore(), true, false); // 황금오리 점수 추가
            goldenDuck.capture(); // 황금오리 포획
            goldenDuck = null; // 황금오리 제거
            timeBetweenShots = Math.max(100000000, timeBetweenShots - (Framework.secInNanosec / 8)); // 총알 발사 속도 감소
            goldenDuckSpawned = false; // 황금오리 스폰 상태 초기화
        }
    }
}
