import java.io.IOException;

public class Client {
	public static final String COURSE = "cs5700fall2014";
	public static String NUID = "001989426";

	public enum Message {
		HELLO, STATUS, SOLUTION, BYE
	}

	public static void main(String[] args) throws IOException {
		String hostName = null;
		String portNum = null;
		boolean ssl = false;
		if (args.length < 2) {
			System.out.println("Invalid input format. Check your input format!");
			System.out.println("client <-p port> <-s> [hostname] [NEU ID]");
			System.exit(-1);
		} else if (args.length == 2) {
			hostName = args[0];
			NUID = args[1];
		} else {
			for(int i = 0; i < args.length; i++) {
				if (args[i].equals("-p")) {
					portNum = args[i+1];
					i++;
				} else if (args[i].equals("-s")) {
					ssl = true;
				} else if(hostName == null){
					hostName = args[i];
				} else {
					NUID = args[i];
				}
			}
		}
		
		SocketWrapper socketWrapper = null;
		try {
			socketWrapper = new SocketWrapper(portNum, hostName, ssl);
			socketWrapper.sendMessage(createHelloMessage());
			String[] response = socketWrapper.getResponse();
			String responseStatus = response[1];

			while (responseStatus.equals(Message.STATUS.toString())) {
				int solution = calculateExpression(response[2], response[4], response[3]);
				socketWrapper.sendMessage(createSolutionMessage(solution));
				response = socketWrapper.getResponse();
				responseStatus = response[1];
			}

			if (response[2].equals(Message.BYE.toString())) {
				System.out.println("response: " + response);
				System.out.println("success!");
				System.out.println(response[1]);
			} else {
				System.out.println("response: " + response);
				System.out.println("failed!");
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (socketWrapper != null)
				socketWrapper.closeSocket();
		}
	}

	public static String createHelloMessage() {
		return String.format("%s %s %s\n", COURSE, Message.HELLO.toString(),
				NUID);
	}

	public static String createSolutionMessage(int solution) {
		return String.format("%s %d\n", COURSE, solution);
	}

	public static int calculateExpression(String number1, String number2,
			String op) {
		int num1 = Integer.parseInt(number1);
		int num2 = Integer.parseInt(number2);
		int result;
		switch (op) {
		case "+":
			result = num1 + num2;
			break;
		case "-":
			result = num1 - num2;
			break;
		case "*":
			result = num1 * num2;
			break;
		case "/":
			result = num1 / num2;
			break;
		default:
			throw new RuntimeException("Operator Error!");
		}
		return result;
	}

}
