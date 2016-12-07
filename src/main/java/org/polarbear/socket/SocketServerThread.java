package org.polarbear.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SocketServerThread extends Thread {
	
	private static final int ENDTX_ASCIICODE = 3;// define the character of end transmission
	private static final String ENDTX = new Character((char)ENDTX_ASCIICODE).toString();
	
	protected Socket socket;
	int noClient;

    public SocketServerThread(Socket clientSocket, int noClient) {
        this.socket = clientSocket;
        this.noClient = noClient;
    }//: end constructor

    public void run() {
    	// deal with socket inputs and outputs (I/O)
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        OutputStream os = null;
        DataOutputStream out = null;
        // deal with file output
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {
            // received from client
        	is = socket.getInputStream();
            isr = new InputStreamReader(is);
            in = new BufferedReader(isr);
            // response to client
            os = socket.getOutputStream();
            out = new DataOutputStream(os);
            // initialize file output and establish the log file
            String dateHour = new SimpleDateFormat("yyyyMMddHH").format(Calendar.getInstance().getTime());
			fw = new FileWriter("C:\\SocketServer_log_" + dateHour + ".txt", true);
			bw = new BufferedWriter(fw);
        } catch (Exception e) {
            System.out.println("err3: " + e);
            e.printStackTrace();
            try {
            	if (bw!=null) { bw.close(); bw = null; }
            	if (fw!=null) { fw.close(); fw = null; }
            	if (out!=null) { out.close(); out = null; }
            	if (os!=null) { os.close(); os = null; }
            	if (in!=null) { in.close(); in = null; }
            	if (isr!=null) { isr.close(); isr = null; }
            	if (is!=null) { is.close(); is = null; }
            	if (socket!=null) { socket.close(); socket = null; }
            } catch (Exception e2) {
            	System.out.println("err3-1: " + e2);
            	e2.printStackTrace();
            }//: end try
            return;
        }//: end try
        String stringLineFromClient;
		pw = new PrintWriter(bw);

		// get current date and time and log client connected
		String timeStamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		System.out.println("Client #" + noClient + " connected...");
		pw.println(timeStamp + ": " + "Client #" + noClient + " connected...");
		pw.flush();

        while (true) {
            try {
            	if (in!=null) {
	            	stringLineFromClient = in.readLine();
            		if (stringLineFromClient!=null) {
		                System.out.println("Received from client #" + noClient + ": " + stringLineFromClient);
		                if (stringLineFromClient.equalsIgnoreCase("BYE")
		                	|| stringLineFromClient.equalsIgnoreCase("EXIT")
		                	|| stringLineFromClient.equalsIgnoreCase("QUIT")
		                	|| stringLineFromClient.equalsIgnoreCase(ENDTX)
		                	|| stringLineFromClient.equalsIgnoreCase("[ETX]")
		                	|| stringLineFromClient.equalsIgnoreCase("0x03")
		                	|| stringLineFromClient.equalsIgnoreCase("3")
		                	) {
		            		// get current date and time and log client disconnected
		            		timeStamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		            		System.out.println("Client #" + noClient + " disconnected...");
		            		pw.println(timeStamp + ": " + "Client #" + noClient + " disconnected...");
		            		pw.flush();
		        			pw.close();
		        			if (bw!=null) { bw.close(); bw = null; }
		        			if (fw!=null) { fw.close(); fw = null; }
		        			if (out!=null) { out.close(); out = null; }		
		        			if (os!=null) { os.close(); os = null; }
		        			if (in!=null) { in.close(); in = null; }
		                    if (isr!=null) { isr.close(); isr = null; }
		                    if (is!=null) { is.close(); is = null; }
		                    if (socket!=null) { socket.close(); socket = null; }
		                    return;
		                } else {
		                	// response to client
		                	/*
		                    out.writeBytes(stringLineFromClient + "\r\n");
		                    out.flush();
		                    */
		    				// get current date and time
		    				timeStamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		    				// write client inputs to file
		    				pw.println(timeStamp + ": " + stringLineFromClient);
		    				pw.flush();
		                }//: end if
            		}//: end if (in.readLine()!=null)
            	}//: end if (in!=null)                
            } catch (Exception e) {
            	String errMessage = e.getMessage();
                if (errMessage.indexOf("Connection reset")>-1) {
                	System.out.println("Connection timeout or closed of Client #" + noClient);
    				// get current date and time
    				timeStamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    				// write client inputs to file
    				pw.println(timeStamp + ": " + "Connection timeout or connection closed of Client #" + noClient);
    				pw.flush();                	
                } else {
                	System.out.println("err4: " + e);
                	e.printStackTrace();
                }//: end if
            	try {
                	if (pw!=null) { pw.close(); pw = null; }
                	if (bw!=null) { bw.close(); bw = null; }
                	if (fw!=null) { fw.close(); fw = null; }
                	if (out!=null) { out.close(); out = null; }
                	if (os!=null) { os.close(); os = null; }
                	if (in!=null) { in.close(); in = null; }
                	if (isr!=null) { isr.close(); isr = null; }
                	if (is!=null) { is.close(); is = null; }
                	if (socket!=null) { socket.close(); socket = null; }
                } catch (Exception e2) {
                	System.out.println("err4-1: " + e2);
                	e2.printStackTrace();
                }//: end try
                return;
            }//: end try
        }//: end while
    }//: end method run()
}//: end class SocketServerThread