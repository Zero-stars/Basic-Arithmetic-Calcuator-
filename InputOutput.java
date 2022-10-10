import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class InputOutput {

	public static void main(String[] args) throws IOException {
		InetAddress ip_address;
		ip_address = InetAddress.getLocalHost();
		int port = 101;

		Scanner keyboard = new Scanner(System.in);

		Socket socket = new Socket(ip_address, port);

		DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

		// while connected to a socket
		while (true) {
			System.out.println("Please enter the equation in the following format: 'operand operator operand'");
			System.out.println("The calculator operators include: {+, -, *, /, ^} ");
			System.out.println("Type 'List contents of the log' to view all calculation performed");
			System.out.println("Type 'Exit' to exit the program");

			// ask for user input
			String userInput = keyboard.nextLine();

			// send input to server: Calculator
			dataOutputStream.writeUTF(userInput);

			// Exit Program
			if (userInput.equalsIgnoreCase("Exit")) {
				break;
			}

			// View the calculation in the Logger
			if (userInput.equalsIgnoreCase("List contents of the log")) {
				String log = dataInputStream.readUTF();
				String[] logFinal = log.split(",");
				int count = 0;
				System.out.println("Contents in the Log");
				while (count < logFinal.length) {
					System.out.println(count + 1 + ". " + logFinal[count]);
					count++;
				}

			} else {
				// Get Data from the Computer
				String answer = dataInputStream.readUTF();
				System.out.println("Message From Calculator: " + answer);
				System.out.println();
			}
		}
		keyboard.close();
		socket.close();
	}

}
