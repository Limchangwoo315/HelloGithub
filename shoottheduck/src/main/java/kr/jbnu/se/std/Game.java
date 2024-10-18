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

/**
 * Actual game.
 *
 * @author www.gametutorial.net
 */

public class Game {

    private Random random;
    private Font font;
    private ArrayList<Duck> ducks;
    private int runawayDucks;
    private int killedDucks;
    private int score;
    private int highestScore; // 최고 점수를 저장할 변수
    private int shoots;
    private long lastTimeShoot;
    private long timeBetweenShots;
    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage sightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;
// Player 객체 초기화
    private Player player = new Player(); // (추가된 부분)
    private boolean firstShot = true;

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    private void Initialize()
    {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        ducks = new ArrayList<Duck>();

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        highestScore = 0; // 최고 점수 초기화
        shoots = 0;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;

        player.resetCurrentScore(); // (추가된 부분)
    }

    private void LoadContent()
    {
        try
        {
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
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame()
    {
        ducks.clear();
        Duck.lastDuckTime = 0;

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;

        lastTimeShoot = 0;

        player.resetCurrentScore(); // (추가된 부분)
    }

    public void UpdateGame(long gameTime, Point mousePosition)
    {


        if(System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks)
        {
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2], Duck.duckLines[Duck.nextDuckLines][3], duckImg));

            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }

        for(int i = 0; i < ducks.size(); i++)
        {
            ducks.get(i).Update();

            if(ducks.get(i).x < 0 - duckImg.getWidth())
            {
                ducks.remove(i);
                runawayDucks++;
            }
        }

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;
                boolean duckHit = false;

                for(int i = 0; i < ducks.size(); i++)
                {
                    if(new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition) ||
                            new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition))
                    {
                        killedDucks++;
                        score += ducks.get(i).score;
                        player.addScore(ducks.get(i).getScore(), true);
                        ducks.remove(i);
                        duckHit = true; // 오리를 맞춘 경우
                        break;
                    }
                }

                // 첫 번째 발사 이후로 false로 설정
                firstShot = false;
                if (!duckHit) {
                    player.addScore(0, false); // 오리를 맞추지 못했을 때

                    player.resetCombo(); // (추가된 부분) 오리 명중 실패 시 콤보 초기화
                }
//                player.setCurrentScore(score); // 현재 점수를 플레이어 클래스에 업데이트
                lastTimeShoot = System.nanoTime();
            }
        }

        for (int i = 0; i < ducks.size(); i++) {   //오리가 겹쳐 있는지 채크
            Duck duck = ducks.get(i);
            duck.Update();
        }
        checkCollision();

        if(runawayDucks >= 200)
            Framework.gameState = Framework.GameState.GAMEOVER;

    }

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        for(int i = 0; i < ducks.size(); i++)
        {
            ducks.get(i).Draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + player.getCurrentScore(), 440, 21);

        g2d.drawString("HIGHEST SCORE: " + player.getHighestScore(), 580, 21); // (추가된 부분)
    }

    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);

        g2d.setColor(Color.black);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }

    // 최고 점수를 얻는 메서드를 추가합니다.
    private int getHighestScore() {
        // (추가된 부분)
        // 최고 점수를 반환하는 로직을 구현합니다. 예시로 score를 그대로 사용했습니다.
        // 나중에 다른 저장 방식을 사용할 수 있습니다.
        return score;
    }
    private void checkCollision() {
        // 각 오리의 충돌을 감지하기 위해 오리들을 겹치는 위치별로 그룹화합니다.
        ArrayList<ArrayList<Duck>> overlappingGroups = new ArrayList<>();

        for (int i = 0; i < ducks.size(); i++) {
            Duck duck1 = ducks.get(i);
            ArrayList<Duck> group = new ArrayList<>();
            group.add(duck1);

            for (int j = 0; j < ducks.size(); j++) {
                if (i == j) continue;
                Duck duck2 = ducks.get(j);

                // 두 오리가 겹치는지 판단합니다.
                if (areOverlapping(duck1, duck2)) {
                    group.add(duck2);
                }
            }

            if (group.size() > 1) {
                overlappingGroups.add(group);
            }
        }

        // 겹치는 그룹을 처리합니다.
        for (ArrayList<Duck> group : overlappingGroups) {
            if (group.size() >= 2) {
                // 두 마리 이상 겹쳐있는 경우, 뒤에 있는 오리들을 기절시킵니다.
                for (int i = 1; i <= Math.min(2, group.size() - 1); i++) {
                    group.get(i).stun();
                }
            }
        }
    }

    /**
     * 두 오리가 겹치는지 판단하는 메서드.
     */
    private boolean areOverlapping(Duck duck1, Duck duck2) {
        // 오리들의 이미지 크기나 위치를 사용해 겹치는지 판단합니다.
        // 예를 들어, 단순한 AABB 충돌 검사:
        int duck1Width = duck1.getImage().getWidth();
        int duck1Height = duck1.getImage().getHeight();
        int duck2Width = duck2.getImage().getWidth();
        int duck2Height = duck2.getImage().getHeight();

        return duck1.x < duck2.x + duck2Width && duck1.x + duck1Width > duck2.x
                && duck1.y < duck2.y + duck2Height && duck1.y + duck1Height > duck2.y;
    }
}