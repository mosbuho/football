package test;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class testS {
	private static final int PORT = 1234;
	private static Random random = new Random();
	private static ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
	private static AtomicBoolean turnA = new AtomicBoolean(random.nextBoolean());
	private static Set<PrintWriter> writers = Collections.synchronizedSet(new HashSet<>());

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("서버 온");

			while (true) {
				Socket cs = ss.accept();
				System.out.println(cs + "접속");
				new Thread(() -> {
					try {
						csHandler(cs);
					} catch (IOException e) {
						System.out.println("쓰레드 핸들러 에러" + e.getMessage());
					} catch (InterruptedException e) {
						System.out.println("쓰레드 핸들러 에러" + e.getMessage());
					}
				}).start();
			}
		} catch (IOException e) {
			System.out.println("서버 소켓 에러 " + e.getMessage());
		}
	}

	private static void csHandler(Socket cs) throws IOException, InterruptedException {
		BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
		PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
		writers.add(out);

		out.println("사용자 이름을 입력하세요.");
		String userName = in.readLine();

		boolean isPlayerA = userName.contains("A");

		while (true) {
			if ((isPlayerA && turnA.get()) || (!isPlayerA && !turnA.get())) {
				out.println("당신의 차례입니다. 'ROLL'을 입력하세요.");
				String input = in.readLine();

				if ("ROLL".equals(input)) {
					int diceRoll = random.nextInt(6) + 1;
					scores.put(userName, diceRoll);
					broadcastMessage(userName + "의 주사위 숫자는 " + diceRoll + "입니다.");

					turnA.set(!turnA.get());

					if (scores.size() == 2) {
						String winner = scores.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
						broadcastMessage(winner + "가 이겼습니다!");
						scores.clear();
					}
				} else {
					out.println("잘못된 입력입니다. 'ROLL'을 입력하세요.");
				}
			} else {
				out.println("당신의 차례가 아닙니다. 기다려주세요.");
			}
			Thread.sleep(6000);
		}
	}

	private static void broadcastMessage(String message) {
		for (PrintWriter writer : writers) {
			writer.println(message);
		}
	}
}