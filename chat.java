//Multi clinet chat app
//Developed by,
//Brij Shah


import java.io.*;
import java.net.*;
import java.util.*;


public class chat {

	public static void main(String[] args) throws UnknownHostException, IOException {
		InputStream inStream;
		DataInputStream inDataStream;
		OutputStream outStream;
		DataOutputStream outDataStream;
		String msg = "";

		// Print all commands to let user know about functionalities.
		System.out.println("(1) myip - See your ip address.");
		System.out.println("(2) myport - See your port number.");
		System.out.println("(3) connect <ip> <port> - Connect to peer.");
		System.out.println("(4) list Command - List all the connected peers.");
		System.out.println("(5) send <id> - Send message to peer.");
		System.out.println("(6) Terminate <id> - Terminate the connection");
		System.out.println("(7) Exit - Exit the project.");

		List<Socket> socketArray = new ArrayList<Socket>();
		final int port_no = Integer.parseInt(args[0]);
		Thread th = new Thread() {
			@Override
			public void run() {
				try {
					Server sr = new Server(port_no);
				} catch (Exception e) {

				}
			}
		};
		th.start();

		do {
			// read user input
			BufferedReader dis = new BufferedReader(new InputStreamReader(System.in));

			// get user input
			System.out.println("Enter your Command: ");
			msg = dis.readLine();

			// all command list & description
			if (msg.equals("list")) {

				// to display all connected peers
				System.out.println("Id: \t IP Address \t Port No.");
				for (int i = 0; i < socketArray.size(); i++) {
					System.out.println((i + 1) + ": \t" + socketArray.get(i).getInetAddress() + "\t"
							+ socketArray.get(i).getPort());
				}

			} else if (msg.equals("myip")) {
				System.out.println(InetAddress.getLocalHost().toString());
			} else if (msg.equals("myport")) {
				System.out.println(port_no);
			} else if (msg.equals("help")) {
				System.out.println("(1) myip - See your ip address.");
				System.out.println("(2) myport - See your port number.");
				System.out.println("(3) connect <ip> <port> - Connect to peer.");
				System.out.println("(4) list Command - List all the connected peers.");
				System.out.println("(5) send <id> - Send message to peer.");
				System.out.println("(6) Terminate <id> - Terminate the connection");
				System.out.println("(7) Exit - Exit the project.");
			} else if (msg.startsWith("connect")) {
				String server_details[] = msg.split(" ");
				String host = server_details[1];
				int secondPort = Integer.parseInt(server_details[2]);
				try {
					Socket sock = new Socket(host, secondPort);
					sock.getInetAddress();
					socketArray.add(sock);

					// print message if conection is successful.
					System.out.println("Connection successfully established to : " + host);

					outStream = sock.getOutputStream();
					outDataStream = new DataOutputStream(outStream);
					String msg_containt = "Connected: \n IP Address : " + InetAddress.getLocalHost().toString()
							+ "\n Port : " + port_no;

					// to show connect message to peer from other side
					outDataStream.writeUTF(msg_containt);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (msg.startsWith("send")) {
				String msg_context[] = msg.split(" ");
				int sockets_id = Integer.parseInt(msg_context[1]);

				msg = "";
				for (int i = 2; i < msg_context.length; i++) {
					msg += msg_context[i] + " ";
				}
				msg = msg.trim();

				if (sockets_id <= socketArray.size() && sockets_id > 0) {
					outStream = socketArray.get(sockets_id - 1).getOutputStream();
					outDataStream = new DataOutputStream(outStream);

					String msg_containt = "msg recieved from " + InetAddress.getLocalHost().toString();
					msg_containt += "\n Sender's Port : " + port_no;
					msg_containt += "\n Mesasage : " + msg;

					// to send and recive msgs
					outDataStream.writeUTF(msg_containt);
				} else {
					System.out.print("Invalid id !!");
				}
			} else if (msg.startsWith("terminate")) {
				String server_details[] = msg.split(" ");
				int sockets_id = Integer.parseInt(server_details[1]);

				if (sockets_id <= socketArray.size() && sockets_id > 0) {
					socketArray.remove(sockets_id - 1);

					// to terminate the prticular id
					System.out.print("Terminated!! \n");
				} else {
					System.out.print("Invalid Sender id");
				}
			} else if (msg.startsWith("exit")) {
				System.out.print("Thank you! Bye!");
				System.exit(0);
			} else {
				System.out.print("Invalid Command !!!");
			}
		} while (!msg.equals("exit"));
	}
};

// to create server
class Server {

	public Server(int PORT_NO) throws IOException {


		ServerSocket sock = new ServerSocket(PORT_NO, 1, InetAddress.getLocalHost());
		Socket conn = sock.accept();
		while (true) {
			try {
				InputStream ins1 = conn.getInputStream();
				DataInputStream inds1 = new DataInputStream(ins1);
				String msg1 = inds1.readUTF();
				if (msg1.startsWith("Connected:")) {
					System.out.println(msg1);
				} else {
					System.out.println(msg1);
				}

			} catch (Exception e) {
			}

		}
	}
};
