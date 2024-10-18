//private Cloud cloud;
//
//public Framework() {
//    super();
//    player = new Player();
//    cloud = new Cloud(); // Cloud 객체 초기화
//    gameState = GameState.VISUALIZING;
//
//    //We start game in new thread.
//    Thread gameThread = new Thread() {
//        @Override
//        public void run() {
//            GameLoop();
//        }
//    };
//    gameThread.start();
//}
//
//@Override
//public void Draw(Graphics2D g2d) {
//    switch (gameState) {
//        case PLAYING:
//            game.Draw(g2d, mousePosition());
//            cloud.draw(g2d); // 구름 그리기
//            break;
//        case GAMEOVER:
//            game.DrawGameOver(g2d, mousePosition());
//            cloud.draw(g2d); // 구름 그리기
//            break;
//        case MAIN_MENU:
//            g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
//            // ... 기존 코드 생략 ...
//            cloud.draw(g2d); // 구름 그리기
//            break;
//        // ... 나머지 코드 ...
//    }
//}
