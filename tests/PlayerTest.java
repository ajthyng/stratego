import edu.asu.stratego.game.PieceColor;
import edu.asu.stratego.game.Player;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
class PlayerTest {

    Player testPlayerTest = new Player();
    String nickname = "TestNickName";
    String result;
    PieceColor color = PieceColor.BLUE;
    PieceColor testColor;


    @Test
    void setNickname() {
        testPlayerTest.setNickname(nickname);
        result = testPlayerTest.getNickname();
        assertThat(result.toString(), is("TestNickName"));
    }

    @Test
    void getNickname() {
        testPlayerTest.setNickname(nickname);
        result = testPlayerTest.getNickname();
        assertThat(result.toString(), is("TestNickName"));
    }

    @Test
    void setColor() {
        testPlayerTest.setColor(color);
        testColor = testPlayerTest.getColor();
        assertThat(testColor.toString(), is("BLUE"));

    }

    @Test
    void getColor() {
        testPlayerTest.setColor(color);
        testColor = testPlayerTest.getColor();
        assertThat(testColor.toString(), is("BLUE"));

    }

}