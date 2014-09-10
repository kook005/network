import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWrapper {
	private Socket socket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
	private String hostName = "cs5700f14.ccs.neu.edu";
	private int port = 27993;
	private boolean ssl = false;

	public SocketWrapper(String portNum, String hostName, boolean ssl) {
		if (portNum != null)
			this.setPort(Integer.parseInt(portNum));
		if (hostName != null)
			this.setHostName(hostName);
		if (ssl)
			this.ssl = ssl;
		createSocket();
	}

	public boolean createSocket() {
		try {
			socket = new Socket(getHostName(), getPort());
			socketWriter = new PrintWriter(socket.getOutputStream(), true);
			socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public void closeSocket() throws IOException {
		if (this.socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

	public void sendMessage(String message) {
		getSocketWriter().write(message);
	}

	public String readMessage() throws IOException {
		return getSocketReader().readLine();
	}

	public String[] getResponse() throws IOException {
		String message = readMessage();
		return message.split("\\s+");
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public PrintWriter getSocketWriter() {
		return socketWriter;
	}

	public void setSocketWriter(PrintWriter socketWriter) {
		this.socketWriter = socketWriter;
	}

	public BufferedReader getSocketReader() {
		return socketReader;
	}

	public void setSocketReader(BufferedReader socketReader) {
		this.socketReader = socketReader;
	}
}