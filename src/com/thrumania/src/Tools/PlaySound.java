package com.thrumania.src.Tools;



import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by ali on 6/20/2016.
 */
public class PlaySound implements Runnable {
    private String path;
    private boolean is_stop;
    private boolean repeat;
    private Clip clip;
    private static LinkedList<PlaySound> soundList = new LinkedList<>();
    private static int volume = 0;
    public PlaySound(String path) {
        soundList.add(this);
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
        soundList.remove(this);
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

        soundList.remove(this);
    }

    public static void changeVolume(float volume){
        PlaySound.volume = (int)volume;
        for(PlaySound sound : soundList){
            FloatControl gainControl = (FloatControl) sound.clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }

    }

    public static int getVolume(){
        System.out.println(volume);
        return volume;
    }
}
