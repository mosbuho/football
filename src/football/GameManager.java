package football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GameManager {
	private User attUser;
	private User defUser;
	private PrintWriter attWriter;
	private PrintWriter defWriter;
	private BufferedReader attReader;
	private BufferedReader defReader;

	public GameManager(User attUser, User defUser, PrintWriter attWriter, PrintWriter defWriter,
			BufferedReader attReader, BufferedReader defReader) {
		this.attUser = attUser;
		this.defUser = defUser;
		this.attWriter = attWriter;
		this.defWriter = defWriter;
		this.attReader = attReader;
		this.defReader = defReader;
	}

	public void start() throws IOException {
		attWriter.println("att");
		attWriter.flush();
		defWriter.println("def");
		defWriter.flush();

		while (true) {
			String attAction = attReader.readLine();
			if (attAction.equals("pass")) {
				/*
				 * Player a = attUserPlayers.get((int) (Math.random() * 8)); Player b =
				 * attUserPlayers.get((int) (Math.random() * 8)); Player defPlayer =
				 * defUserPlayers.get((int) (Math.random() * 10 - 5 + 1) + 5);
				 */
			} else if (attAction.equals("cross")) {
				// 크로스
			} else {
				// 슛
			}
			String defAction = defReader.readLine();
		}
	}
}
