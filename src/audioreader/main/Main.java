package audioreader.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Main {

	public static void main(String[] args) {
		System.out.println("Recording your mic........");

		// For work mic: 1 channel, 16 bit, 4800hz
		AudioFormat ttsMicFormat = new AudioFormat(48000, 16, 1, true, false);
    	try {
            TargetDataLine ttsMic = AudioSystem.getTargetDataLine(ttsMicFormat);
			ttsMic.open(ttsMicFormat);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int numBytesRead;
			byte[] data = new byte[ttsMic.getBufferSize() / 5];
			ttsMic.start(); // begin capture

			int i = 0;
			while(i < 100) {
				numBytesRead = ttsMic.read(data, 0, data.length);
				out.write(data, 0, numBytesRead);
				System.out.println("Data");
				i++;
			}
			// -- write to wav --
			File outputWav = new File("resources/recording.wav");
			InputStream byteInputStream = new ByteArrayInputStream(data);
			AudioInputStream audioInputStream = new AudioInputStream(byteInputStream, ttsMicFormat, data.length);
            try {
            AudioSystem.write(audioInputStream, Type.WAVE, outputWav);
            } catch (IOException ex) {
				ex.printStackTrace();
            }

        } catch (LineUnavailableException ex) {
			ex.printStackTrace();
        }
	}
}
