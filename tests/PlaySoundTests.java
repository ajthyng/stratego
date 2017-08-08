import edu.asu.stratego.media.PlaySound;
import org.junit.Rule;
import org.junit.Test;

public class PlaySoundTests {
    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    /**
     * We just want to test that no exceptions are thrown in the process
     * of playing a sound file. All three methods in the PlaySound class are
     * essentially the same, so we'll be cleaning up and only using one.
     */
    @Test
    public void playSoundGivenFileAndVolume() {
        PlaySound.playMusic("cornfield", 60);
    }

}
