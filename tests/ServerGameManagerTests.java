import edu.asu.stratego.game.*;
import edu.asu.stratego.game.board.ServerBoard;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerGameManagerTests {
    ServerGameManager server;
    ServerBoard board;
    Socket sockOne, sockTwo;
    int session = 0;
    Method checkWinCondtions, computeValidMoves;
    Piece bomb = new Piece(PieceType.BOMB, PieceColor.BLUE, true);

    @Before
    public void setUp() throws NoSuchMethodException {
        server = new ServerGameManager(sockOne, sockTwo, ++session);
        board = new ServerBoard();
        checkWinCondtions = server.getClass().getDeclaredMethod("checkWinCondition");
        checkWinCondtions.setAccessible(true);

        computeValidMoves = server.getClass().getDeclaredMethod("computeValidMoves", int.class, int.class, PieceColor.class);
        computeValidMoves.setAccessible(true);
    }

    @Test
    public void givenRedHasNoMovesThenBlueWins() throws InvocationTargetException, IllegalAccessException {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board.getSquare(row, col).setPiece(new Piece(PieceType.CAPTAIN, PieceColor.BLUE, true));
            }
        }
        assertEquals(GameStatus.RED_NO_MOVES, checkWinCondtions.invoke(server));
    }

    @Test
    public void givenRedAndBlueHaveNoMovesThenGameEnds() throws InvocationTargetException, IllegalAccessException {
        board.getSquare(0, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.BLUE, true));
        board.getSquare(9, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.RED, false));
        GameStatus status = GameStatus.class.cast(checkWinCondtions.invoke(server));
        assertTrue(status != GameStatus.IN_PROGRESS);
    }

    @Test
    public void givenRedAndBlueHaveAFlagButNoMovesThenGameShouldTie() throws InvocationTargetException, IllegalAccessException {
        board.getSquare(0, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.BLUE, true));
        board.getSquare(9, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.RED, false));
        GameStatus status = GameStatus.class.cast(checkWinCondtions.invoke(server));
        assertTrue("Red should not be loser", status != GameStatus.RED_NO_MOVES);
        assertTrue("Blue should not be loser", status != GameStatus.BLUE_NO_MOVES);
        assertTrue("Red should not be loser", status != GameStatus.RED_CAPTURED);
        assertTrue("Blue should not be loser",status != GameStatus.BLUE_CAPTURED);
        assertTrue("Game should have ended", status != GameStatus.IN_PROGRESS);
    }

    @Test
    public void givenABombThenNoValidMovesAvailable() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        board.getSquare(0,0).setPiece(bomb);
        Field board = server.getClass().getDeclaredField("board");
        board.setAccessible(true);
        board.set(server, board);
        PieceColor color = bomb.getPieceColor();
        ArrayList<Point> availableMoves = (ArrayList<Point>) (computeValidMoves.invoke(server,0, 0, color));
        assertTrue("Bomb had available moves", availableMoves.size() == 0);
    }
}
