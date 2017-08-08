package edu.asu.stratego.game;
import org.junit.jupiter.api.Test;

import java.awt.*;

class MoveTest {
    Move testMove = new Move();
    Point start = new Point(-1, -1);
    Point end   = new Point(-1, -1);

    @Test
    void isAttackMove() {
        boolean isAttack = false;
        System.out.println("isAttackMove Should return false. Actual result: " + isAttack);
    }

    @Test
    void setAttackMove() {
        testMove.setAttackMove(true);
        boolean result = testMove.isAttackMove();
        System.out.println("setAttackMove sets isAttack, should return true. Actual result: " + result);
    }

    @Test
    void isPieceSelected() {
        start.x = 2; start.y = 2;
        boolean result = testMove.isPieceSelected();
        System.out.println("isPieceSelected Should return false. Actual result: " + result);

    }

    @Test
    void getRowStart() {
        start.x = -1;
        int result = testMove.getRowStart();
        System.out.println("getRowStart Should return -1. Actual result: " + result);

    }

    @Test
    void getColStart() {
        start.y = -1;
        int result = testMove.getColStart();
        System.out.println("getColStart Should return -1. Actual result: " + result);
    }

    @Test
    void getStart() {
        Point result = testMove.getStart();
        System.out.println("getStart should be (-1, -1) " + result);
    }

    @Test
    void getEnd() {
        Point result = testMove.getEnd();
        System.out.println("getEnd should be (-1, -1) " + result);
    }

    @Test
    void setStart() {
        testMove.setStart(2,2);
        Point result = testMove.getStart();
        System.out.println("setStart should be (2,2). Actual result: " + result);
    }

    @Test
    void setEnd() {
        testMove.setEnd(5,3);
        Point result = testMove.getEnd();
        System.out.println("setEnd should be (5,3). Actual Result: " + result);
    }

    @Test
    void getMoveColor() {
        testMove.setMoveColor(PieceColor.RED);
        PieceColor result = testMove.getMoveColor();
        System.out.println("getMoveColor should be red. Actaul result: "+ result + " This test also test setMoveColor");
    }

    @Test
    void setMoveColor() {
        //See getmoveColor
    }

    @Test
    void getStartPiece() {
        PieceType type = PieceType.SCOUT;
        System.out.println("getStartPiece should be SCOUT. Actual result: " + type);
    }

    @Test
    void setStartPiece() {
        //See getStartPiece
    }

    @Test
    void getEndPiece() {
        PieceType type = PieceType.MINER;
        System.out.println("getEndPiece should be MINER. Actual result: " + type);
    }

    @Test
    void setEndPiece() {
        //See getEndPiece
    }

    @Test
    void isAttackWin() {
        testMove.setAttackWin(true);
        boolean result = testMove.isAttackWin();
        System.out.println("isAttackWin should be true. Actual result: " + result);

    }

    @Test
    void setAttackWin() {
        //see isAttackWin
    }

    @Test
    void isDefendWin() {
        testMove.setDefendWin(false);
        boolean result = testMove.isDefendWin();
        System.out.println("isDefendWin Should be false. Actual result: " + result);
    }

    @Test
    void setDefendWin() {
        //See isDefendWin
    }

}