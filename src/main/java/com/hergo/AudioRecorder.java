package main.java.com.hergo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioRecorder implements Runnable {

    public Thread thread;
    private AudioFormat format;

    public AudioRecorder() {
        super();
    }

    public void start() {
        thread = new Thread(this);
        thread.setName("Audio input thread");
        thread.start();
    }

    public void stop() {
        this.thread = null;
    }

    @Override
    public void run() {
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
			while(thread != null) {
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
			WavDataUtil.saveToFile("test",AudioFileFormat.Type.WAVE , audioInputStream);

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
    }    

    public AudioFormat getFormat() {
        return format;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }


}
