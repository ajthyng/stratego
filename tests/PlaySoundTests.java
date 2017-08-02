import edu.asu.stratego.gui.media.PlaySound;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlaySoundTests {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    @Before
    public void setUp() {
        sound = new PlaySound();
    }

    /**
     * We just want to test that no exceptions are thrown in the process
     * of playing a sound file. All three methods in the PlaySound class are
     * essentially the same, so we'll be cleaning up and only using one.
     */
    @Test
    public void playSoundGivenFileAndVolume() {
        sound.playMusic('edu/asu/stratego/media/sound/cornfield.mp3', 60);
    }

}
