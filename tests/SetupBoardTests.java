import edu.asu.stratego.game.Piece;
import edu.asu.stratego.game.PieceColor;
import edu.asu.stratego.game.PieceType;
import edu.asu.stratego.game.Game;
import edu.asu.stratego.game.board.ClientBoard;
import edu.asu.stratego.game.board.SetupBoard;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SetupBoardTests {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    @Before
    public void setUp() {
        new Game();
        setup_board = new SetupBoard();
    }

    @Test
    public void clientBoardToSetupBoard() {
        Piece flag = new Piece(PieceType.FLAG, PieceColor.BLUE, true);
        Piece spy = new Piece(PieceType.SPY, PieceColor.BLUE, true);
        Game.getBoard.getSquare(6, 5).setPiece(flag);

        setup_board.getPiecePositions();
        setup_board.setPiece(spy, 1, 3);
        assertTrue("SetupBoard created correctly", Game.getBoard.getSquare(6, 5).getPiece() == setup_board.getPiece(0, 5) && setup_board.getPiece(1,3) == spy);
    }
}
