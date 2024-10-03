package audioreader.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Main {

	public static void main(String[] args) {
		System.out.println("Recording your mic........");

		// Audio input format configurations
		Map<String, AudioFormat> formatMap = new HashMap<>();
		formatMap.put("workMic", new AudioFormat(Encoding.PCM_SIGNED, 48000, 16, 1,3000,1000, false));
		formatMap.put("homeMic", new AudioFormat(Encoding.PCM_SIGNED, 48000, 24,2,2000,1000,false));
		AudioFormat format = formatMap.get("homeMic");
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		try {
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format,line.getBufferSize());

			int framSizeInBytes = format.getFrameSize();
			int bufferLengthInFrames = line.getBufferSize() / 8;
			final int bufferLengthInBytes = bufferLengthInFrames * framSizeInBytes;  // matches array length below

			// build the byte output stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] data = new byte[bufferLengthInBytes];
			int numBytesRead;
			while(Thread.activeCount() != 0) {
				if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
					break;
				}
				out.write(data, 0, numBytesRead);
			}

			// convert byte Outstream to AudioInputStream
			byte audioBytes[] = out.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
			AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioBytes.length / framSizeInBytes);
			long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());

			// save to wav
			File outputWav = new File("resources/recording.wav");
			try {
				AudioSystem.write(audioInputStream, Type.WAVE, outputWav);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

    	// try {
        //     TargetDataLine ttsMic = AudioSystem.getTargetDataLine(ttsMicFormat);
		// 	ttsMic.open(ttsMicFormat);
		// 	ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 	int numBytesRead;
		// 	byte[] data = new byte[ttsMic.getBufferSize() / 5];
		// 	ttsMic.start(); // begin capture

		// 	int i = 0;
		// 	while(i < 100) {
		// 		numBytesRead = ttsMic.read(data, 0, data.length);
		// 		out.write(data, 0, numBytesRead);
		// 		System.out.println("Data");
		// 		i++;
		// 	}
		// 	// -- write to wav --
		// 	File outputWav = new File("resources/recording.wav");
		// 	InputStream byteInputStream = new ByteArrayInputStream(data);
		// 	AudioInputStream audioInputStream = new AudioInputStream(byteInputStream, ttsMicFormat, data.length);
        //     try {
        //     AudioSystem.write(audioInputStream, Type.WAVE, outputWav);
        //     } catch (IOException ex) {
		// 		ex.printStackTrace();
        //     }

        // } catch (LineUnavailableException ex) {
		// 	ex.printStackTrace();
        // }
	}
}
