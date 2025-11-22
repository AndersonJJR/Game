package audio;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;

public class SoundEffect {

    public static void play(String fileName) {
        try {
            InputStream audioSrc = SoundEffect.class.getResourceAsStream("/res/audio/" + fileName);

            if (audioSrc == null) {
                System.out.println("Som não encontrado: " + fileName);
                return;
            }

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(audioSrc));

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            //aumentar volume
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(6.0f);

            clip.start();

            // AGUARDAR O SOM TERMINAR
            while (clip.isRunning()) {
                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


