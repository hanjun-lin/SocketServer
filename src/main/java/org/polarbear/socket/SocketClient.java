package org.polarbear.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
	
	private static final int ENDTX_ASCIICODE = 3;// define the character of end transmission
	private static final String ENDTX = new Character((char)ENDTX_ASCIICODE).toString();

	private static final String SERVERADDRESS = "localhost";
	private static final int PORTNUMBER = 5731; // 5731-5740 Unassigned

	public static void main(String[] args) {

		String hostName = (args != null && args.length > 0) ? args[0] : SERVERADDRESS;
		int portNumber = (args != null && args.length > 0) ? Integer.parseInt(args[1]) : PORTNUMBER;

		Socket clientSocket = null;

		// deal with socket inputs and outputs (I/O)
		OutputStream os = null;
		PrintWriter out = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;

        // deal with client input
		InputStreamReader isr2 = null;
		BufferedReader stdIn = null;
		try {
			clientSocket = new Socket(hostName, portNumber);
			// sent to server
			os = clientSocket.getOutputStream();
			out = new PrintWriter(os, true);
			// receive from server
			is = clientSocket.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			// deal with client input
			isr2 = new InputStreamReader(System.in);
			stdIn = new BufferedReader(isr2);
			String fromUser = null, fromServer = null;
			while (true) {
				System.out.print("Client input: ");
				fromUser = stdIn.readLine();				
				if (fromUser != null) {
					// output to server
					out.println(fromUser);
					if (fromUser.equalsIgnoreCase("BYE")
		                || fromUser.equalsIgnoreCase("EXIT")
		                || fromUser.equalsIgnoreCase("QUIT")
		                || fromUser.equalsIgnoreCase(ENDTX)
		                || fromUser.equalsIgnoreCase("[ETX]")
		                || fromUser.equalsIgnoreCase("0x03")
		                || fromUser.equalsIgnoreCase("3")
						) {
		                System.out.println("Disconnect to server...");
						break;
					} else {
						// enable this section will stop and wait for server response
						/*
						if (in != null) {
							// response from server
							fromServer = in.readLine();
							System.out.println("Server response: " + fromServer);
						}//: end if (in != null)
						*/
					}//: end if
				}//: end if (fromUser != null)
			}//: end while (true)
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host " + hostName);
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Couldn't get I/O for the connection to " + hostName);
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			System.out.println("err1: " + e.getMessage());
			e.printStackTrace();
			try {
				if (stdIn!=null) { stdIn.close(); stdIn = null; }
				if (isr2!=null) { isr2.close(); isr2 = null; }
				if (in!=null) { in.close(); in = null; }
				if (isr!=null) { isr.close(); isr = null; }
				if (is!=null) { is.close(); is = null; }
				if (out!=null) { out.close(); out = null; }
				if (os!=null) { os.close(); os = null; }
				if (clientSocket!=null) { clientSocket.close(); clientSocket = null; }
			} catch (Exception e2) {
				System.out.println("err1-1: " + e.getMessage());
				e.printStackTrace();
			}//: end try
		} finally {
			try {
				if (stdIn!=null) { stdIn.close(); stdIn = null; }
				if (isr2!=null) { isr2.close(); isr2 = null; }
				if (in!=null) { in.close(); in = null; }
				if (isr!=null) { isr.close(); isr = null; }
				if (is!=null) { is.close(); is = null; }
				if (out!=null) { out.close(); out = null; }
				if (os!=null) { os.close(); os = null; }
				if (clientSocket!=null) { clientSocket.close(); clientSocket = null; }
			} catch (Exception e) {
				System.out.println("err2: " + e.getMessage());
				e.printStackTrace();
			}//: end try
		}//: end try
	}//: end method main(String[] args)
}//: end class SocketClient