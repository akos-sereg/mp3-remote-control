package hu.akoss.git.mp3remotecontrol.server.mp3player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

class LineRedirecter extends Thread {
	/** The input stream to read from. */
	private InputStream in;
	/** The output stream to write to. */
	private OutputStream out;

	/**
	 * @param in
	 *            the input stream to read from.
	 * @param out
	 *            the output stream to write to.
	 * @param prefix
	 *            the prefix used to prefix the lines when outputting to the
	 *            logger.
	 */
	LineRedirecter(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	public void run() {
		try {
			// creates the decorating reader and writer
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			PrintStream printStream = new PrintStream(out);
			String line;

			// read line by line
			while ((line = reader.readLine()) != null) {
				printStream.println(line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}