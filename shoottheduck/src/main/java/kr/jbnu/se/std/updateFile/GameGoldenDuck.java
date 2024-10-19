//public class Game {
//    // 기존 변수들...
//
//    private BufferedImage goldenDuckImg; // 황금 오리 이미지
//    private boolean goldenDuckSpawned = false; // 황금 오리 스폰 상태 추가
//
//    // 생성자, 초기화, 로드 콘텐츠 등...
//
//    private void LoadContent() {
//        try {
//            // 기존 이미지 로드...
//            goldenDuckImg = loadGoldenDuckImage(); // 황금오리 이미지 로드
//        } catch (IOException ex) {
//            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    // UpdateGame 메서드...
//    public void UpdateGame(long gameTime, Point mousePosition) {
//        // 기존 코드...
//
//        // 황금오리 업데이트 및 포획 처리
//        if (goldenDuck != null) {
//            goldenDuck.Update();
//            if (goldenDuck.getHitBox().x < 0 - goldenDuck.getImage().getWidth()) {
//                goldenDuck = null; // 화면을 넘어가면 황금오리 제거
//            }
//            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
//                if (goldenDuck.getHitBox().contains(mousePosition)) {
//                    handleGoldenDuckCapture();
//                }
//            }
//        }
//
//        // 기존 코드...
//    }
//
//    private void handleClick(Point mousePosition) {
//        boolean duckHit = false;
//
//        for (int i = 0; i < ducks.size(); i++) {
//            Duck duck = ducks.get(i);
//
//            if (duck.getHitBox().contains(mousePosition)) {
//                if (duck instanceof BossDuck) {
//                    BossDuck boss = (BossDuck) duck;
//                    boss.takeDamage();
//
//                    if (boss.getHealth() <= 0) {
//                        ducks.remove(i);
//                        player.addScore(500, true, true);
//                        System.out.println("Boss defeated!");
//                        resetAfterBossDefeat();
//                        spawnGoldenDuck(); // 보스를 처치한 후 황금오리 스폰
//                    }
//                } else {
//                    killedDucks++;
//                    player.addScore(duck.getScore(), true, false);
//                    ducks.remove(i);
//                }
//                duckHit = true;
//                break;
//            }
//        }
//
//        if (!duckHit) {
//            player.addScore(0, false, false);
//            player.resetCombo();
//        }
//    }
//
//    private void spawnGoldenDuck() {
//        if (!goldenDuckSpawned) { // 황금오리가 이미 스폰되지 않았을 때만 생성
//            int speed = -5; // 적절한 속도로 설정
//            goldenDuck = new GoldenDuck(Framework.frameWidth, random.nextInt(Framework.frameHeight - 100), speed, goldenDuckImg);
//            goldenDuckSpawned = true; // 황금오리 스폰 플래그 설정
//        }
//    }
//
//    private void handleGoldenDuckCapture() {
//        if (goldenDuck != null) {
//            player.addScore(goldenDuck.getScore(), true, false); // 황금오리 점수 추가
//            goldenDuck.capture(); // 황금오리 포획
//            goldenDuck = null; // 황금오리 제거
//            timeBetweenShots = Math.max(100000000, timeBetweenShots - (Framework.secInNanosec / 4)); // 총알 발사 속도 감소
//            goldenDuckSpawned = false; // 황금오리 스폰 상태 초기화
//        }
//    }
//}
