package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * kr.jbnu.se.std.Framework that controls the game (kr.jbnu.se.std.Game.java) that created it, update it and draw it on the screen.
 *
 * @author
 */

public class Framework extends Canvas implements GameObserver {

    @Override
    public void onScoreChanged(int newScore) {
        System.out.println("Score updated: " + newScore);
    }

    @Override
    public void onGameStateChanged(String newState) {
        System.out.println("Game state changed to: " + newState);
    }

    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;

    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;

    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * Possible states of the game
     */
    public static enum GameState { STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED }

    /**
     * Current state of the game
     */
    public static GameState gameState;

    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;

    // The actual game
    private Game game;

    // private Player player; // 제거 또는 주석 처리
    private Sight sight; // 조준경

    private BackgroundMusic backgroundMusic; // 배경음악

    // 여러 구름을 관리하기 위한 리스트와 구름의 수
    private List<Cloud> clouds;
    private final int NUM_CLOUDS = 5; // 생성할 구름의 수

    /**
     * Image for menu.
     */
    private BufferedImage shootTheDuckMenuImg;

    public Framework() {
        super();

        sight = new Sight(); // Sight 객체 생성

        backgroundMusic = new BackgroundMusic();
        backgroundMusic.play(); // 배경 음악 시작

        gameState = GameState.VISUALIZING;

        // 게임 루프를 새 스레드에서 시작
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();
    }

    /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class,
     * variables and objects for the actual game can be set in kr.jbnu.se.std.Game.java.
     */
    private void Initialize() {
        // 구름 리스트 초기화
        clouds = new ArrayList<>();
        for (int i = 0; i < NUM_CLOUDS; i++) {
            Cloud cloud = new Cloud(frameWidth, frameHeight);
            cloud.setVisible(false); // 초기에는 구름을 보이지 않게 설정
            clouds.add(cloud);
        }

        // 기타 초기화 코드...
    }

    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in kr.jbnu.se.std.Game.java.
     */
    private void LoadContent() {
        try {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop() {
        // VISUALIZING 상태에서 프레임 크기를 설정
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            sight.update();

            switch (gameState) {
                case PLAYING:
                    if (game != null) { // game 객체가 null이 아닌지 확인
                        gameTime += System.nanoTime() - lastTime;

                        game.UpdateGame(gameTime, mousePosition());

                        lastTime = System.nanoTime();
                    }

                    // 모든 구름 업데이트
                    for (Cloud cloud : clouds) {
                        cloud.update();
                    }

                    break;
                case GAMEOVER:
                    // ...
                    break;
                case MAIN_MENU:
                    // ...
                    break;
                case OPTIONS:
                    // ...
                    break;
                case GAME_CONTENT_LOADING:
                    // ...
                    break;
                case STARTING:
                    // 변수 및 객체 설정
                    Initialize();
                    // 파일 로드
                    LoadContent();

                    // 모든 초기화가 완료되면 메인 메뉴로 전환
                    gameState = GameState.MAIN_MENU;
                    break;
                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // 프레임 크기가 설정되었으므로 초기화 상태로 전환
                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            // 화면 다시 그리기
            repaint();

            // FPS에 맞춰 스레드 슬립
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // 밀리초 단위

            if (timeLeft < 10)
                timeLeft = 10; // 최소 슬립 시간 설정

            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                if (game != null) { // game 객체가 null이 아닌지 확인
                    game.Draw(g2d, mousePosition());
                    int playerScore = game.getPlayerScore(); // 플레이어의 현재 점수 가져오기
                    if (playerScore >= 100 && playerScore <= 3500) {
                        // 모든 구름을 보이게 설정
                        for (Cloud cloud : clouds) {
                            cloud.setVisible(true);
                        }
                    } else {
                        // 모든 구름을 숨김
                        for (Cloud cloud : clouds) {
                            cloud.setVisible(false);
                        }
                    }
                    // 모든 구름 그리기
                    for (Cloud cloud : clouds) {
                        cloud.draw(g2d);
                    }
                }
                break;
            case GAMEOVER:
                if (game != null) { // game 객체가 null이 아닌지 확인
                    game.DrawGameOver(g2d, mousePosition());
                }
                break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawString("Use left mouse button to shoot the duck.", frameWidth / 2 - 83, (int) (frameHeight * 0.65));
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int) (frameHeight * 0.67));
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int) (frameHeight * 0.70));
                g2d.setColor(Color.white);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                // 추가된 부분: 최고 점수 표시
                g2d.setFont(new Font("monospaced", Font.BOLD, 24));
                g2d.setColor(Color.white);
                // game 객체가 null인지 확인하고 접근
                if (game != null) {
                    g2d.drawString("HIGHEST SCORE: " + game.getHighestScore(), frameWidth / 2 - 100, frameHeight / 2 + 50);
                } else {
                    g2d.drawString("HIGHEST SCORE: 0", frameWidth / 2 - 100, frameHeight / 2 + 50);
                }
                break;
            case OPTIONS:
                // ...
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
        // Sight 이미지 그리기
        sight.draw(g2d);
    }

    /**
     * Starts new game.
     */
    private void newGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Game();
    }

    /**
     * Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();

        if (game != null) {
            game.RestartGame();
        }

        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }

    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     *
     * @return Point of mouse coordinates.
     */
    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    /**
     * This method is called when keyboard key is released.
     *
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;
            case PLAYING:
            case MAIN_MENU:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                break;
        }
    }

    /**
     * This method is called when mouse button is clicked.
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newGame();
                }
                break;
            case PLAYING:
                // 클릭 시 Sight 크기 증가 시작
                sight.startScaling(e.getX(), e.getY());
                break;
        }
    }
}
