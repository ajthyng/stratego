import edu.asu.stratego.game.ClientGameManager;
import edu.asu.stratego.game.PieceColor;
import edu.asu.stratego.game.Player;
import edu.asu.stratego.gui.ClientStage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;


public class ClientGameManagerTests {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
    ClientGameManager client;
    Method getPlayerColor, calculateShift;


    @Before
    public void setUp() throws NoSuchMethodException {
        client = new ClientGameManager(new ClientStage());
        getPlayerColor = client.getClass().getDeclaredMethod("getPlayerColor", Player.class);
        getPlayerColor.setAccessible(true);

        calculateShift = client.getClass().getDeclaredMethod("calculateShift", int.class);
        calculateShift.setAccessible(true);
    }

    @Test
    public void givenBlueOpponentThenPlayerIsRed() throws InvocationTargetException, IllegalAccessException {
        Player pOne = new Player();
        pOne.setColor(PieceColor.BLUE);
        assertEquals(PieceColor.RED, getPlayerColor.invoke(client, pOne));
    }

    @Test
    public void givenRedOpponentThenPlayerIsBlue() throws InvocationTargetException, IllegalAccessException {
        Player pOne = new Player();
        pOne.setColor(PieceColor.RED);
        assertEquals(PieceColor.BLUE, getPlayerColor.invoke(client, pOne));
    }

    @Test
    public void givenNegativeMoveThenShiftIsNegative() throws InvocationTargetException, IllegalAccessException {
        int neg = -5;
        assertEquals(-1, calculateShift.invoke(client, neg));
    }
    @Test
    public void givenPositiveMoveThenShiftIsPositive() throws InvocationTargetException, IllegalAccessException {
        int pos = 15;
        assertEquals(1, calculateShift.invoke(client, pos));
    }
    @Test
    public void givenZeroMoveThenShiftIsZero() throws InvocationTargetException, IllegalAccessException {
        int zero = 0;
        assertEquals(0, calculateShift.invoke(client, zero));
    }
}