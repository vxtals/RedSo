package server;

import java.rmi.server.RMISocketFactory;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

/*
* User: Tim Goffings
* Date: Oct 3, 2002 – 3:51:34 PM
*/

public class FixedPortRMISocketFactory extends RMISocketFactory {

/**
* Creates a client socket connected to the specified host and port and writes out debugging info
* @param host the host name
* @param port the port number
* @return a socket connected to the specified host and port.
* @exception IOException if an I/O error occurs during socket creation
*/
public Socket createSocket(String host, int port)
throws IOException {
return new Socket(host, port);
}

/**
* Create a server socket on the specified port (port 0 indicates
* an anonymous port) and writes out some debugging info
* @param port the port number
* @return the server socket on the specified port
* @exception IOException if an I/O error occurs during server socket
* creation
*/
public ServerSocket createServerSocket(int port)
throws IOException {
port = (port == 0 ? 1098 : port);
return new ServerSocket(port);

}
}
