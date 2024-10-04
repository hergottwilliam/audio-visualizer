package main.java.com.hergo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		AudioFormatManager formatManager = new AudioFormatManager();
		formatManager.addAudioFormat("workMic", new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 1,3000,1000, false));
		formatManager.addAudioFormat("workMicSimple", new AudioFormat(48000, 16, 1, true, false));
		formatManager.addAudioFormat("homeMic", new AudioFormat(Encoding.PCM_SIGNED, 48000, 24,2,2000,1000,false));
		formatManager.addAudioFormat("homeMicSimple", new AudioFormat(48000, 24, 2,true, false));

		formatManager.setCurrentFormat("homeMicSimple");
		AudioFormat format = formatManager.getCurrentFormat();

		AudioRecorder recorder = new AudioRecorder();
		recorder.setFormat(format);

		recorder.start();
		Thread.sleep(20000);
		recorder.stop();


	}
}