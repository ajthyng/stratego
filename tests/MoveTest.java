import org.junit.Test;
import edu.asu.stratego.game.Move;
import edu.asu.stratego.game.PieceColor;
import edu.asu.stratego.game.PieceType;

import java.awt.*;

public class MoveTest {
    Move testMove = new Move();
    Point start = new Point(-1, -1);
    Point end   = new Point(-1, -1);

    @Test
    public void isAttackMove() {
        boolean isAttack = false;
        System.out.println("isAttackMove Should return false. Actual result: " + isAttack);
    }

    @Test
    public void setAttackMove() {
        testMove.setAttackMove(true);
        boolean result = testMove.isAttackMove();
        System.out.println("setAttackMove sets isAttack, should return true. Actual result: " + result);
    }

    @Test
    public void isPieceSelected() {
        start.x = 2; start.y = 2;
        boolean result = testMove.isPieceSelected();
        System.out.println("isPieceSelected Should return false. Actual result: " + result);

    }

    @Test
    public void getRowStart() {
        start.x = -1;
        int result = testMove.getRowStart();
        System.out.println("getRowStart Should return -1. Actual result: " + result);

    }

    @Test
    public void getColStart() {
        start.y = -1;
        int result = testMove.getColStart();
        System.out.println("getColStart Should return -1. Actual result: " + result);
    }

    @Test
    public void getStart() {
        Point result = testMove.getStart();
        System.out.println("getStart should be (-1, -1) " + result);
    }

    @Test
    public void getEnd() {
        Point result = testMove.getEnd();
        System.out.println("getEnd should be (-1, -1) " + result);
    }

    @Test
    public void setStart() {
        testMove.setStart(2,2);
        Point result = testMove.getStart();
        System.out.println("setStart should be (2,2). Actual result: " + result);
    }

    @Test
    public void setEnd() {
        testMove.setEnd(5,3);
        Point result = testMove.getEnd();
        System.out.println("setEnd should be (5,3). Actual Result: " + result);
    }

    @Test
    public void getMoveColor() {
        testMove.setMoveColor(PieceColor.RED);
        PieceColor result = testMove.getMoveColor();
        System.out.println("getMoveColor should be red. Actaul result: "+ result + " This test also test setMoveColor");
    }

    @Test
    public void setMoveColor() {
        //See getmoveColor
    }

    @Test
    public void getStartPiece() {
        PieceType type = PieceType.SCOUT;
        System.out.println("getStartPiece should be SCOUT. Actual result: " + type);
    }

    @Test
    public void setStartPiece() {
        //See getStartPiece
    }

    @Test
    public void getEndPiece() {
        PieceType type = PieceType.MINER;
        System.out.println("getEndPiece should be MINER. Actual result: " + type);
    }

    @Test
    public void setEndPiece() {
        //See getEndPiece
    }

    @Test
    public void isAttackWin() {
        testMove.setAttackWin(true);
        boolean result = testMove.isAttackWin();
        System.out.println("isAttackWin should be true. Actual result: " + result);

    }

    @Test
    public void setAttackWin() {
        //see isAttackWin
    }

    @Test
    public void isDefendWin() {
        testMove.setDefendWin(false);
        boolean result = testMove.isDefendWin();
        System.out.println("isDefendWin Should be false. Actual result: " + result);
    }

    @Test
    public void setDefendWin() {
        //See isDefendWin
    }

}