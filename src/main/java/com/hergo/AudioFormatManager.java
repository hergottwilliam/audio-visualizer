package main.java.com.hergo;

import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;

public class AudioFormatManager {
    	private Map<String, AudioFormat> formatMap;
        private AudioFormat currentFormat;

        public AudioFormatManager() {
            this.formatMap = new HashMap<>();
            this.currentFormat = null;
        }

        public void addAudioFormat(String name, AudioFormat format) {
            this.formatMap.put(name, format);
        }

        public void setCurrentFormat(String key){
            if(formatMap.containsKey(key)) {
                this.currentFormat = formatMap.get(key);
            }
        }

        public AudioFormat getCurrentFormat() {
            return currentFormat;
        }

        public AudioFormat getFormatByName(String name) {
            return this.formatMap.get(name);
        }

        public void clearFormats() {
            this.formatMap.clear();
        }
}