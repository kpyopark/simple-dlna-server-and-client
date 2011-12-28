package com.elevenquest.sol.upnp.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class HTTPReader {
	
	Socket clientSocket = null;
	InputStream is = null;
	public HTTPReader(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void getInputStream() throws Exception {
		this.is = this.clientSocket.getInputStream();
	}
	
	public String readLine() throws Exception {
		int curByte = -1;
		String rtn = null;
		if ( this.is == null ) 
			getInputStream();
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			while( ( curByte = this.is.read() ) != -1 ) {
				System.out.println( curByte );
				if ( curByte != '\n' )
					break;
				baos.write(curByte);
			}
			if ( baos.size() == 0 && curByte == -1 )
				rtn = null;
			else
				rtn = baos.toString();
		} finally {
			if ( baos != null ) try { baos.close(); } catch ( Exception e1 ) { e1.printStackTrace(); } 
		}
		return rtn;
	}
	
	public byte[] getBody() throws Exception {
		ByteArrayOutputStream baos = null;
		byte[] rtn = null;
		if ( this.is == null ) 
			getInputStream();
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ( ( len = is.read(buffer, 0, 1024)) != -1 ) {
				baos.write(buffer,0,len);
			}
			baos.flush();
			rtn = baos.toByteArray();
		} finally {
			if ( baos != null ) try { baos.close(); } catch ( Exception e1 )  { e1.printStackTrace(); }
		}
		return rtn;
	}
	
	public void close() {
		if ( is != null ) try { is.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
		if ( clientSocket != null ) try { clientSocket.close(); } catch ( Exception e1 ) { e1.printStackTrace(); }
	}
}
