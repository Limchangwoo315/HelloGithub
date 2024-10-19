//package kr.jbnu.se.std;
//import java.awt.Graphics2D;
//import java.awt.Point;
//import java.util.ArrayList;
//
//public class Game {
//    private ArrayList<Duck> ducks; // 오리 리스트
//
//    public Game() {
//        ducks = new ArrayList<>();
//        // 오리 초기화...
//    }
//
//    public void UpdateGame(long gameTime, Point mousePosition) {
//        for (int i = 0; i < ducks.size(); i++) {
//            Duck duck = ducks.get(i);
//            duck.Update();
//        }
//        checkCollision();
//    }
//
//    /**
//     * 오리들 간의 충돌을 체크하고, 앞에 있는 오리를 맞추면 뒤에 있는 오리를 기절시킵니다.
//     */
//    private void checkCollision() {
//        // 각 오리의 충돌을 감지하기 위해 오리들을 겹치는 위치별로 그룹화합니다.
//        ArrayList<ArrayList<Duck>> overlappingGroups = new ArrayList<>();
//
//        for (int i = 0; i < ducks.size(); i++) {
//            Duck duck1 = ducks.get(i);
//            ArrayList<Duck> group = new ArrayList<>();
//            group.add(duck1);
//
//            for (int j = 0; j < ducks.size(); j++) {
//                if (i == j) continue;
//                Duck duck2 = ducks.get(j);
//
//                // 두 오리가 겹치는지 판단합니다.
//                if (areOverlapping(duck1, duck2)) {
//                    group.add(duck2);
//                }
//            }
//
//            if (group.size() > 1) {
//                overlappingGroups.add(group);
//            }
//        }
//
//        // 겹치는 그룹을 처리합니다.
//        for (ArrayList<Duck> group : overlappingGroups) {
//            if (group.size() >= 2) {
//                // 두 마리 이상 겹쳐있는 경우, 뒤에 있는 오리들을 기절시킵니다.
//                for (int i = 1; i <= Math.min(2, group.size() - 1); i++) {
//                    group.get(i).stun();
//                }
//            }
//        }
//    }
//
//    /**
//     * 두 오리가 겹치는지 판단하는 메서드.
//     */
//    private boolean areOverlapping(Duck duck1, Duck duck2) {
//        // 오리들의 이미지 크기나 위치를 사용해 겹치는지 판단합니다.
//        // 예를 들어, 단순한 AABB 충돌 검사:
//        int duck1Width = duck1.getImage().getWidth();
//        int duck1Height = duck1.getImage().getHeight();
//        int duck2Width = duck2.getImage().getWidth();
//        int duck2Height = duck2.getImage().getHeight();
//
//        return duck1.x < duck2.x + duck2Width && duck1.x + duck1Width > duck2.x
//                && duck1.y < duck2.y + duck2Height && duck1.y + duck1Height > duck2.y;
//    }
//
//
//}
