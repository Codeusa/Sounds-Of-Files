package sounds;

import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundOfFiles {

	public static void getFileBytes(final File folder)
			throws LineUnavailableException {
		for (final File fileEntry : folder.listFiles()) {
			final long fileSizeInBytes = fileEntry.length();
			if (fileEntry.isDirectory()) {
				getFileBytes(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				playSound((int) fileSizeInBytes / 100); // saving your ears from
														// death by / 100
			}
		}

	}

	public static void main(final String[] args)
			throws LineUnavailableException {
		final String path;
		if (args.length > 0) {
			path = args[0];
		} else {
			final Scanner scanner = new Scanner(System.in);
			System.out
					.print("Enter the path to which you want to make sound (example C:/Files/):");
			path = scanner.next(); // Get what the user types.
			scanner.close();
		}

		final File folder = new File(path);
		getFileBytes(folder);
	}

	private static void playSound(final int filesize)
			throws LineUnavailableException {
		final byte[] buffer = new byte[1];

		final AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true,
				false);
		SourceDataLine sourceDataLine = AudioSystem
				.getSourceDataLine(audioFormat);
		sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
		for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
			final double angle = i / ((float) 44100 / filesize) * 2.0 * Math.PI;
			buffer[0] = (byte) (Math.sin(angle) * 128); //mathmathmath
			sourceDataLine.write(buffer, 0, 1);

		}

		sourceDataLine.drain();
		sourceDataLine.stop();
		sourceDataLine.close();
	}

}
