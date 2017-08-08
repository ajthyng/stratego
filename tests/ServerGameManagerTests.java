import edu.asu.stratego.game.*;
import edu.asu.stratego.game.board.ServerBoard;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ServerGameManagerTests {
    ServerGameManager server;
    ServerBoard board;
    Socket sockOne, sockTwo;
    int session = 0;
    Method checkWinConditions, computeValidMoves;
    Piece bomb = new Piece(PieceType.BOMB, PieceColor.BLUE, true);

    @Before
    public void setUp() throws NoSuchMethodException {
        server = new ServerGameManager(sockOne, sockTwo, ++session);
        checkWinConditions = server.getClass().getDeclaredMethod("checkWinCondition", ServerBoard.class);
        checkWinConditions.setAccessible(true);

        computeValidMoves = server.getClass().getDeclaredMethod("computeValidMoves", int.class, int.class, Piece.class, ServerBoard.class);
        computeValidMoves.setAccessible(true);
    }

    @Test
    public void givenRedHasNoMovesThenBlueWins() throws InvocationTargetException, IllegalAccessException {
    	board = new ServerBoard();
        board.getSquare(1, 1).setPiece(new Piece(PieceType.CAPTAIN, PieceColor.BLUE, true));
        GameStatus status = (GameStatus) checkWinConditions.invoke(server, board);
        assertThat(status.toString(), is("RED_NO_MOVES"));
    }

    @Test
    public void givenRedAndBlueHaveNoMovesThenGameEnds() throws InvocationTargetException, IllegalAccessException {
    	board = new ServerBoard();
        board.getSquare(0, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.BLUE, true));
        board.getSquare(9, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.RED, false));
        GameStatus status = (GameStatus) checkWinConditions.invoke(server, board);
        assertThat(status.toString(), is(not("IN_PROGRESS")));
    }

    @Test
    public void givenRedAndBlueHaveAFlagButNoMovesThenGameShouldTie() throws InvocationTargetException, IllegalAccessException {
    	board = new ServerBoard();
        board.getSquare(0, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.BLUE, true));
        board.getSquare(9, 9).setPiece(new Piece(PieceType.FLAG, PieceColor.RED, false));
        GameStatus status = GameStatus.class.cast(checkWinConditions.invoke(server, board));
        assertTrue("Red should not be loser", status != GameStatus.RED_NO_MOVES);
        assertTrue("Blue should not be loser", status != GameStatus.BLUE_NO_MOVES);
        assertTrue("Red should not be loser", status != GameStatus.RED_CAPTURED);
        assertTrue("Blue should not be loser",status != GameStatus.BLUE_CAPTURED);
        assertTrue("Game should have ended", status != GameStatus.IN_PROGRESS);
    }

    @Test
    public void givenABombThenNoValidMovesAvailable() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    	board = new ServerBoard();
        board.getSquare(0,0).setPiece(bomb);
        boolean availableMoves = (boolean) (computeValidMoves.invoke(server,0, 0, bomb, board));
        assertFalse("Bomb had available moves", availableMoves);
    }
}
