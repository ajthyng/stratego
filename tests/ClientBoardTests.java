import edu.asu.stratego.game.Piece;
import edu.asu.stratego.game.PieceColor;
import edu.asu.stratego.game.PieceType;
import edu.asu.stratego.game.board.ClientBoard;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClientBoardTests {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    ClientBoard board;

    @Before
    public void setUp() {
        board = new ClientBoard();
    }

    @Test
    public void givenPieceIsSetThenProperPieceIsReturned() {
        Piece flag = new Piece(PieceType.FLAG, PieceColor.BLUE, true);
        board.getSquare(0, 5).setPiece(flag);

        Piece retrievedFlag = board.getSquare(0, 5).getPiece();
        assertTrue("Flag piece was not retrieved", retrievedFlag.getPieceType() == flag.getPieceType());
    }

    @Test(expected = NullPointerException.class)
    public void givenAnOffBoardLocationThenNullShouldReturn() {
        Piece retrievedFlag = board.getSquare(0, -1).getPiece();
    }
}
