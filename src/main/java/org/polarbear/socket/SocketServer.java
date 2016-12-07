package org.polarbear.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    static final int PORT = 5731; // 5731-5740 Unassigned

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server running and listening on port " + PORT);
        } catch (Exception e) {
            System.out.println("err1: " + e);
            e.printStackTrace();
            try {
            	if (serverSocket!=null) { serverSocket.close(); serverSocket = null; } 
            } catch (Exception e2) {
            	System.out.println("err1-1: " + e2);
            	e2.printStackTrace();	
            }//: end try
            System.exit(1);
        }//: end try
        
        Socket socket = null;
        int noClient = 0;
        // socket server always listening on port
        while (true) {
            try {
            	noClient++;
            	socket = serverSocket.accept();
                // new thread for a client
                System.out.println("Client connected...");
                new SocketServerThread(socket, noClient).start();
            } catch (Exception e) {
                System.out.println("err2: " + e);
                e.printStackTrace();
            	try {
            		if (socket!=null) { socket.close(); socket = null; }
                } catch (Exception e2) {
                	System.out.println("err2-1: " + e2);
                	e2.printStackTrace();	
                }//: end try
            }//: end try
        }//: end while
    }//: end method main(String args[])
}//: end class SocketServer