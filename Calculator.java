import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Calculator {
	public static void main(String[] args) throws IOException, EOFException {

		// Connect to Socket
		ServerSocket serverSocket = new ServerSocket(101);
		Socket socket;
		DataInputStream dataInputStream;
		DataOutputStream dataOutputStream;

		while (!serverSocket.isClosed()) {
			socket = serverSocket.accept();
			System.out.println("Connected");

			// Socket between InputOutput and Calculator
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			// Start socket between Calculator and Logger
			InetAddress ip_address;
			ip_address = InetAddress.getLocalHost();
			int port = 201;
			Socket calculatorSocket = new Socket(ip_address, port);

			// Socket between Calculator and Logger
			DataInputStream dataInputStreamCalculator = new DataInputStream(calculatorSocket.getInputStream());
			DataOutputStream dataOutputStreamCalculator = new DataOutputStream(calculatorSocket.getOutputStream());

			while (!(socket.isClosed())) {

				String input = dataInputStream.readUTF();

				String inputCopy = input;

				if (input.equalsIgnoreCase("List contents of the log")) {
					dataOutputStreamCalculator.writeUTF(input);
					input = dataInputStreamCalculator.readUTF();
					dataOutputStream.writeUTF(input);

				} else if (input.equalsIgnoreCase("Exit")) {
					dataOutputStreamCalculator.writeUTF(input);
					socket.close();
				} else {
					String[] split;

					split = input.split(" ");

					System.out.println("Arithmetic Equation Received From Client: " + input);

					String errorFormatMessage;
					String answer;
					double result;

					if (split.length != 3) {
						errorFormatMessage = "Error: Please try again! Enter the equation in the following format: 'operand operator operand'";
						dataOutputStream.writeUTF(errorFormatMessage);

					} else {

						double operand1 = 0.0;
						double operand2 = 0.0;
						String operation = String.valueOf(split[1]);

						String operandError = "";

						try {
							operand1 = Double.parseDouble(String.valueOf(split[0]));
							operand2 = Double.parseDouble(String.valueOf(split[2]));
						} catch (NumberFormatException numberFormatException) {
							operandError = "Error: Please check if operands are numeric value";
							System.out.println(operandError);
							dataOutputStream.writeUTF(operandError);
						}
						// Perform Arithmetic operation
						if (!operandError.equalsIgnoreCase("Error: Please check if operands are numeric value")) {
							switch (operation) {
							case "+":
								result = operand1 + operand2;
								answer = String.valueOf(result);
								System.out.println("Result sent to Logger");
								dataOutputStreamCalculator.writeUTF(inputCopy + " = " + answer);
								break;

							case "-":
								result = operand1 - operand2;
								answer = String.valueOf(result);
								System.out.println("Result sent to Logger");
								dataOutputStreamCalculator.writeUTF(inputCopy + " = " + answer);
								break;

							case "*":
								result = operand1 * operand2;
								answer = String.valueOf(result);
								System.out.println("Result sent to Logger");
								dataOutputStreamCalculator.writeUTF(inputCopy + " = " + answer);
								break;

							case "/":
								if (operand2 == 0) {
									answer = "Error: Sorry, you are dividing by 0! Please try again.";
								} else {
									result = operand1 / operand2;
									answer = String.valueOf(result);
									System.out.println("Result sent to Logger");
									dataOutputStreamCalculator.writeUTF(inputCopy + " = " + answer);
								}
								break;

							case "^":
								result = Math.pow(operand1, operand2);
								answer = String.valueOf(result);
								System.out.println("Result sent to Logger");
								dataOutputStreamCalculator.writeUTF(inputCopy + " = " + answer);
								break;

							default:
								answer = "Error: Sorry, invalid operator found";
							}
							System.out.println("Result sent back to client");
							dataOutputStream.writeUTF(answer);
						}
					}
				}
			}
			dataInputStream.close();
			dataOutputStream.close();
			dataOutputStreamCalculator.close();
			dataInputStreamCalculator.close();
		}
	}
}
