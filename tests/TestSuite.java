import edu.asu.stratego.game.MoveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientBoardTests.class,
        ClientGameManagerTests.class,
        PlaySoundTests.class,
        ServerGameManagerTests.class,
        SetupBoardTests.class,
        MoveTest.class
})
public class TestSuite {}
