package sounds;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundOfFiles {

	private final static int CHANNELS = 1;
	private final static boolean IS_BIG_ENDIAN = true;
	private final static boolean IS_SIGNED = true;
	private static final Logger logger = Logger.getLogger(SoundOfFiles.class
			.getName());
	private final static float SAMPLE_RATE = 44100.0f;
	private final static int SAMPLE_SIZE = 8;

	/**
	 * The main method.
	 */
	public static void main(final String[] args) {
		final String path;
		final File folder;

		if (args.length > 0) {
			path = args[0];
		} else {
			path = getPath();
		}

		folder = new File(path);

		if (folder.exists()) {
			try {
				playFile(folder);
			} catch (final LineUnavailableException e) {
				logger.severe("Encounter error: " + e.getClass());
				logger.severe("Error message: " + e.getMessage());
				logger.severe("Error cause: " + e.getCause());
			}
		} else {
			logger.info("Invalid path.");
		}

	}

	/**
	 * Get path.
	 */
	public static String getPath() {
		final String input;
		final Scanner scanner = new Scanner(System.in);

		logger.info("Enter the path to which you want to make sound (example C:/Files/Project/):");
		input = scanner.next();
		scanner.close();

		return input;
	}

	/**
	 * Loop through the files.
	 */
	public static void playFile(final File folder)
			throws LineUnavailableException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				playFile(fileEntry);
			} else {
				final long fileSizeInBytes = fileEntry.length();
				logger.info("Playing File: "
						+ String.valueOf(fileEntry.getName()) + " " + fileSizeInBytes);
				playSound(fileSizeInBytes);
			}
		}

	}

	/**
	 * Play a sound according to the file size.
	 */
	private static void playSound(final long filesize)
			throws LineUnavailableException {
		final byte[] buffer = new byte[1];

		final AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE,
				SAMPLE_SIZE, CHANNELS, IS_SIGNED, IS_BIG_ENDIAN);
		SourceDataLine sourceDataLine = AudioSystem
				.getSourceDataLine(audioFormat);
		sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
		for (int i = 0; i < 1000 * SAMPLE_RATE / 1000; i++) {
			final double angle = i / (SAMPLE_RATE / filesize) * 2.0 * Math.PI;
			buffer[0] = (byte) (Math.sin(angle) * 127.0);
			sourceDataLine.write(buffer, 0, 1);

		}

		sourceDataLine.drain();
		sourceDataLine.stop();
		sourceDataLine.close();
	}

}
