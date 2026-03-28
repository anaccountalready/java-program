package five.edu.cn;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BackGroundMusic{
private static URL url = null;
private static File file=new File("music\\bkmusic.wav");
public static void main(String []a){
	new Thread(){
		public void run(){
			
			try {AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(file);
			Clip clip=AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
				
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}.start();
}
}
