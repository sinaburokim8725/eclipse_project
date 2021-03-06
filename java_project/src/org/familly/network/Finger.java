package org.familly.network;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;

public class Finger {

	public  static final int DEFAULT_PORT = 79;
	
	
	protected boolean verboss;
	protected int port;
	protected String host,query;
		
	//생성자
	public Finger(String request,boolean verboss ) throws IOException {
		this.verboss = verboss;
		int at = request.lastIndexOf('@');
		if(at == -1) {//없을경우
			query = request;
			host = InetAddress.getLocalHost().getHostName();
			port = DEFAULT_PORT;
			
			
			
		} else {//@ 인덱스가 있는 경우
			query = request.substring(0,at);
			int colon = request.indexOf(':',at + 1);
			
			if(colon == -1) {//없는경우
				host = request.substring(at + 1);
				port = DEFAULT_PORT;
			} else {//port가 있을경우
				host = request.substring(at + 1,colon);
				port = Integer.parseInt(request.substring(colon+1));
				
			}
			
			if(host.equals("")) {
				host = InetAddress.getLocalHost().getHostAddress();
			}
			
		}
		
	}
	
	public Finger(String query, String host,int port,boolean verboss) throws IOException{
		this.query = query;
		host = host.equals("") ? InetAddress.getLocalHost().getHostName() : host;
		this.port = (port == -1) ? DEFAULT_PORT : port;
		this.verboss = verboss;
	}
	//생성자 끝
	
	/////// 메쏘드 /////////////
	
	
	public Reader finger() throws IOException{
		Socket socket = new Socket(host, port);
		OutputStream os = socket.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
		
		if(verboss) {
			writer.write("/W");
		}
		
		if(verboss && (query.length() > 0)) {
			writer.write(" ");
			
		}
		writer.write(query);
		writer.write("\r\n");
		writer.flush();
		return new InputStreamReader(socket.getInputStream(),"UTF-8");
	}
	
	public static void display(Reader reader ,Writer writer) throws IOException{
		PrintWriter printWriter = new PrintWriter(writer);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			printWriter.println(line);
		}
		reader.close();
		
	}
	////////////////////////////
	//
	
	public static void main(String[] args) throws IOException {
		if(((args.length == 2) && !args[0].equals("-l")) ||
			(args.length > 2)) {
			throw new IllegalArgumentException("Syntax: Finger [-l] [<username>][ { @<hostname>}[:<port>]]"); 
		}
		
		boolean verboss = (args.length > 0) && args[0].equals("-l");
		String query = (args.length > (verboss ? 1 : 0)) ? args[args.length - 1] : "";
		
		Finger finger = new Finger(query,verboss);
		Reader result = finger.finger();
		Writer console = new FileWriter(FileDescriptor.out);
		display (result,console);
		console.flush();
		

	}

}
