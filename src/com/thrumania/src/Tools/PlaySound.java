package com.thrumania.src.Tools;



import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ali on 6/20/2016.
 */
public class PlaySound implements Runnable {
    private String path;
    private boolean is_stop;
    private boolean repeat;
    private Clip clip;

    public PlaySound(String path) {
        this.path = path;
        is_stop = false;
    }

    public void play(boolean repeat) {
        this.repeat = repeat;
        (new Thread(this)).start();
    }

    public void stop(){
        clip.stop();
        is_stop = true;
    }

    @Override
    public void run() {
        do {
            clip = null;
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e1) {
            }

            AudioInputStream inputStream;
            try {
                File sound = new File(path);
                inputStream = AudioSystem.getAudioInputStream(sound);
                try {
                    clip.open(inputStream);
                } catch (LineUnavailableException e2) {
                    // TODO Auto-generated catch block
                }
                clip.start();
                try {
                    Thread.sleep(clip.getMicrosecondLength()/1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedAudioFileException | IOException e2) {
            }
        } while (repeat && !is_stop);
    }
}
