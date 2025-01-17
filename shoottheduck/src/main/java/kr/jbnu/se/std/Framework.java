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

public class Framework extends Canvas implements GameObserver {

    @Override
    public void onScoreChanged(int newScore) {
        System.out.println("Score updated: " + newScore);
    }

    @Override
    public void onGameStateChanged(String newState) {
        System.out.println("Game state changed to " + newState);
    }

    public static int frameWidth;
    public static int frameHeight;

    public static final long SEC_IN_NANOSEC = 1000000000L;
    public static final long MILISEC_IN_NANOSEC = 1000000L;

    //FPS - Frames per second, How many times per second the game should update?
    private final int GAME_FPS = 60;
    //Pause between updates. It is in nanoseconds.
    private final long GAME_UPDATE_PERIOD = SEC_IN_NANOSEC / GAME_FPS;

    //Possible states of the game
    public static enum GameState { STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED }

    //Current state of the game
    public static GameState gameState;

    public static void setGameState(GameState newGameState) {
        gameState = newGameState;
    }

    private long gameTime;
    private long lastTime;

    private transient Game game;
    private transient Sight sight; // 조준경
    private transient BackgroundMusic backgroundMusic; // 배경음악

    private transient List<Cloud> clouds;
    private final int NUM_CLOUDS = 5; // 생성할 구름의 수

    private boolean isRunning = true;

    private transient BufferedImage shootTheDuckMenuImg;

    public Framework() {
        super();
        sight = new Sight(); // Sight 객체 생성
        backgroundMusic = new BackgroundMusic();
        backgroundMusic.play(); // 배경 음악 시작

        changeGameState(GameState.VISUALIZING);

        // 게임 루프를 새 스레드에서 시작
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();
    }

    private void initializeCloud() {
        clouds = new ArrayList<>();
        for (int i = 0; i < NUM_CLOUDS; i++) {
            Cloud cloud = new Cloud(frameWidth, frameHeight);
            cloud.setVisible(false); // 초기에는 구름을 보이지 않게 설정
            clouds.add(cloud);
        }
    }

    private void loadContent() {
        try {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void changeGameState(GameState newState) {
        gameState = newState;
    }
    public static synchronized GameState getGameState() {
        return gameState;
    }

    private void GameLoop() {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;

        while (isRunning) {
            beginTime = System.nanoTime();
            sight.update();

            // 상태별 처리
            switch (gameState) {
                case PLAYING -> handlePlayingState();
                case GAMEOVER -> handleGameOverState();
                case MAIN_MENU -> handleMainMenuState();
                case OPTIONS -> handleOptionsState();
                case GAME_CONTENT_LOADING -> handleContentLoadingState();
                case STARTING -> handleStartingState();
                case VISUALIZING -> {
                    visualizingTime = handleVisualizingState(visualizingTime, lastVisualizingTime);
                    lastVisualizingTime = System.nanoTime();
                }
                case DESTROYED -> isRunning = false;
            }
            repaint();

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / MILISEC_IN_NANOSEC;
            if (timeLeft < 10) timeLeft = 10;

            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handlePlayingState() {
        if (game != null) { // game 객체가 null인지 확인
            gameTime += System.nanoTime() - lastTime;
            game.updateGame(gameTime, mousePosition());
            lastTime = System.nanoTime();
        }

        for (Cloud cloud : clouds) {
            cloud.update();
        }
    }

    private void handleStartingState() {
        initializeCloud();
        loadContent();
        Framework.changeGameState(GameState.MAIN_MENU);
    }

    private long handleVisualizingState(long visualizingTime, long lastVisualizingTime) {
        if (this.getWidth() > 1 && visualizingTime > SEC_IN_NANOSEC) {
            Framework.setFrameSize(this.getWidth(), this.getHeight()); // 크기 설정
            Framework.changeGameState(GameState.STARTING);
        } else {
            visualizingTime += System.nanoTime() - lastVisualizingTime;
        }
        return visualizingTime;
    }

    private void handleGameOverState() {
        // GAMEOVER 상태 처리 로직
    }

    private void handleMainMenuState() {
        // MAIN_MENU 상태 처리 로직
    }

    private void handleOptionsState() {
        // OPTIONS 상태 처리 로직
    }

    private void handleContentLoadingState() {
        // GAME_CONTENT_LOADING 상태 처리 로직
    }

    public static synchronized void setFrameSize(int width, int height) {
        frameWidth = width;
        frameHeight = height;
    }

    public static synchronized int getFrameWidth() {
        return frameWidth;
    }
    public static synchronized int getFrameHeight() {
        return frameHeight;
    }

    @Override
    public void draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING -> drawPlayingState(g2d);
            case GAMEOVER -> drawGameOverState(g2d);
            case MAIN_MENU -> drawMainMenuState(g2d);
            case OPTIONS -> drawOptionsState(g2d);
            case GAME_CONTENT_LOADING -> drawLoadingState(g2d);
            default -> drawUnknownState(g2d);
        }
        sight.draw(g2d);
    }

    private void drawPlayingState(Graphics2D g2d) {
        if (game != null) {
            game.draw(g2d, mousePosition());

            int playerScore = game.getPlayerScore();
            boolean showClouds = (playerScore >= 100 && playerScore <= 3500);

            for (Cloud cloud : clouds) {
                cloud.setVisible(showClouds);
                cloud.draw(g2d);
            }
        }
    }

    private void drawGameOverState(Graphics2D g2d) {
        if (game != null) {
            game.drawGameOver(g2d, mousePosition());
        }
    }

    private void drawMainMenuState(Graphics2D g2d) {
        g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);

        g2d.setColor(Color.white);
        g2d.drawString("Use left mouse button to shoot the duck.", frameWidth / 2 - 83, (int) (frameHeight * 0.65));
        g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int) (frameHeight * 0.67));
        g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int) (frameHeight * 0.70));
        g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);

        g2d.setFont(new Font("monospaced", Font.BOLD, 24));
        g2d.setColor(Color.white);
        String highestScoreText = (game != null) ? "HIGHEST SCORE: " + game.getHighestScore() : "HIGHEST SCORE: 0";
        g2d.drawString(highestScoreText, frameWidth / 2 - 100, frameHeight / 2 + 50);
    }

    private void drawOptionsState(Graphics2D g2d) {
        // OPTIONS 상태를 위한 렌더링 로직 (현재 비어 있음)
    }

    private void drawLoadingState(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
    }

    private void drawUnknownState(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.drawString("UNKNOWN GAME STATE", frameWidth / 2 - 50, frameHeight / 2);
    }

    private void newGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Game();
    }

    private void restartGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();

        if (game != null) {
            game.restartGame();
        }

        // We change game status so that the game can start.
        Framework.changeGameState(GameState.PLAYING);
    }

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
            default:
                System.out.println("Unexpected game state in keyReleasedFramework: " + gameState);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newGame();
                }
                break;
            case PLAYING:
                sight.startScaling(e.getX(), e.getY());
                break;
            default:
                System.out.println("Unexpected game state in mouseClicked: " + gameState);
                break;
        }
    }
}