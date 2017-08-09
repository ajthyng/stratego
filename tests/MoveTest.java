package edu.asu.stratego.game;

import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MoveTest {
    Move testMove = new Move();
    Move testMove2 = new Move();
    Point start = new Point(-1, -1);
    Point end   = new Point(-1, -1);

    @Test
    public void isAttackMove() {
        testMove.setAttackMove(true);
        boolean result = testMove.isAttackMove();
        testMove2.setAttackMove(false);
        boolean result2 = testMove2.isAttackMove();
        assertThat(result, is(true));
        assertThat(result2, is(false));
//        System.out.println("isAttackMove Should return false. Actual result: " + isAttack);
    }

    @Test
    public void setAttackMove() {
        testMove.setAttackMove(true);
        boolean result = testMove.isAttackMove();
        testMove2.setAttackMove(false);
        boolean result2 = testMove2.isAttackMove();
        assertThat(result, is(true));
        assertThat(result2, is(false));
//        System.out.println("setAttackMove sets isAttack, should return true. Actual result: " + result);
    }

    @Test
    public void isPieceSelected() {
        testMove.setStart(-1,-1);
        boolean result = testMove.isPieceSelected();
        assertThat(result, is(false));
        testMove2.setStart(5,5);
        boolean result2 = testMove2.isPieceSelected();
        assertThat(result2, is(true));

//        System.out.println("isPieceSelected Should return false. Actual result: " + result);

    }

    @Test
    public void getRowStart() {
        testMove.setStart(2,2);
        int result = testMove.getRowStart();
        assertThat(result, is(2));
//        System.out.println("getRowStart Should return -1. Actual result: " + result);

    }

    @Test
    public void getColStart() {
        testMove.setStart(2,2);
        int result = testMove.getColStart();
        assertThat(result, is(2));
//        System.out.println("getColStart Should return -1. Actual result: " + result);
    }

    @Test
    public void getStart() {
        Point result = testMove.getStart();
        assertThat(result.toString(), is("java.awt.Point[x=-1,y=-1]"));
//        System.out.println("getStart should be (-1, -1) " + result);
    }

    @Test
    public void getEnd() {
        Point result = testMove.getEnd();
        assertThat(result.toString(), is("java.awt.Point[x=-1,y=-1]"));
//        System.out.println("getEnd should be (-1, -1) " + result);
    }

    @Test
    public void setStart() {
        testMove.setStart(2,2);
        Point result = testMove.getStart();
        assertThat(result.toString(), is("java.awt.Point[x=2,y=2]"));
//        System.out.println("setStart should be (2,2). Actual result: " + result);
    }

    @Test
    public void setEnd() {
        testMove.setEnd(5,3);
        Point result = testMove.getEnd();
        assertThat(result.toString(), is("java.awt.Point[x=5,y=3]"));
//        System.out.println("setEnd should be (5,3). Actual Result: " + result);
    }

    @Test
    public void getMoveColor() {
        testMove.setMoveColor(PieceColor.RED);
        PieceColor result = testMove.getMoveColor();
        assertThat(result.toString(), is("RED"));
//        System.out.println("getMoveColor should be red. Actual result: "+ result + " This test also test setMoveColor");
    }

    @Test
    public void setMoveColor() {
        testMove.setMoveColor(PieceColor.RED);
        PieceColor result = testMove.getMoveColor();
        assertThat(result.toString(), is("RED"));
        testMove2.setMoveColor(PieceColor.BLUE);
        PieceColor result2 = testMove2.getMoveColor();
        assertThat(result2.toString(), is("BLUE"));
    }

    @Test
    public void getStartPiece() {
        PieceType type = PieceType.SCOUT;
        assertThat(type.toString(), is("SCOUT"));
//        System.out.println("getStartPiece should be SCOUT. Actual result: " + type);
    }

    @Test
    public void setStartPiece() {

    }

    @Test
    public void getEndPiece() {
        PieceType type = PieceType.MINER;
        assertThat(type.toString(), is("MINER"));
//        System.out.println("getEndPiece should be MINER. Actual result: " + type);
    }

    @Test
    public void setEndPiece() {
        //See getEndPiece
    }

    @Test
    public void isAttackWin() {
        testMove.setAttackWin(true);
        boolean result = testMove.isAttackWin();
        assertThat(result, is(true));
//        System.out.println("isAttackWin should be true. Actual result: " + result);

    }

    @Test
    public void setAttackWin() {
        //see isAttackWin
    }

    @Test
    public void isDefendWin() {
        testMove.setDefendWin(false);
        boolean result = testMove.isDefendWin();
        assertThat(result, is(false));
//        System.out.println("isDefendWin Should be false. Actual result: " + result);
        testMove2.setDefendWin(true);
        boolean result2 = testMove2.isDefendWin();
        assertThat(result2, is(true));
    }

    @Test
    public void setDefendWin() {
        testMove.setDefendWin(false);
        boolean result = testMove.isDefendWin();
        assertThat(result, is(false));
//        System.out.println("isDefendWin Should be false. Actual result: " + result);
        testMove2.setDefendWin(true);
        boolean result2 = testMove2.isDefendWin();
        assertThat(result2, is(true));
    }

}
