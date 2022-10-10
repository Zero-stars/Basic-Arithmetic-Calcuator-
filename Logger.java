import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Logger {
	public static void main(String[] args) throws IOException {
		// Connect to Socket
		ServerSocket serverSocket = new ServerSocket(201);
		System.out.println("Waiting for Connection...");
		DataInputStream dataInputStreamLogger;
		DataOutputStream dataOutputStreamLogger;
		List<String> calculationStorage = new ArrayList<>();

		while (!serverSocket.isClosed()) {
			Socket socket = serverSocket.accept();
			System.out.println("Connected to Calculator Server");

			dataInputStreamLogger = new DataInputStream(socket.getInputStream());
			dataOutputStreamLogger = new DataOutputStream(socket.getOutputStream());

			while (!(socket.isClosed())) {
				String input = dataInputStreamLogger.readUTF();

				if (input.equalsIgnoreCase("List contents of the log")) {
					String finalList = "";
					int count = 0;
					for (String list : calculationStorage) {
						count++;
						if (calculationStorage.size() == count) {
							finalList += list;
						} else {
							finalList += list + ",";
						}
					}
					dataOutputStreamLogger.writeUTF(finalList);

				} else if (input.equalsIgnoreCase("Exit")) {
					calculationStorage.clear();
					dataInputStreamLogger.close();
					dataInputStreamLogger.close();
					socket.close();
				} else {
					calculationStorage.add(input);
					System.out.println(calculationStorage);
				}
			}
		}
	}
}
