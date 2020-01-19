
// A Java program for a Client
import java.net.*;
import java.io.*;

public class EchoClient {
    // initialize socket and input output streams
    private Socket socket      = null;
    private DataInputStream user_input = null;
    private DataInputStream socket_in = null;
    private DataOutputStream socket_out = null;

    // constructor to put ip address and port
    public EchoClient(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            user_input = new DataInputStream(System.in);

            // sends output to the socket
            socket_in  = new DataInputStream(socket.getInputStream());
            socket_out = new DataOutputStream(socket.getOutputStream());

            int userInputSize = 0;
            byte[] userInputBuffer = new byte[2048];

            int serverResponseSize = 0;
            byte[] serverResponseBuffer = new byte[1024];

            // Read the data from client until EOF is reached
            while((userInputSize = user_input.read(userInputBuffer, 0, 2048)) != -1) {

                try {
                    // Shout out to server
                    socket_out.write(userInputBuffer, 0, userInputSize);

                    // This is for dynamic size of input from client. Might be useful for your project-1
                    ByteArrayOutputStream dynamicBuffer = new ByteArrayOutputStream();

                    // Listen to the echo sound from server
                    while (dynamicBuffer.size() != userInputSize &&
                            (serverResponseSize = socket_in.read(serverResponseBuffer, 0, 1024)) != -1) {

                        dynamicBuffer.write(serverResponseBuffer, 0, serverResponseSize);
                    }
                    System.out.println("[SEVER RESPONSE]:" + dynamicBuffer.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println("Other Exception: " + i);
        }


        // close the connection
        try {
            user_input.close();
            socket_out.close();
            socket_in.close();
            socket.close();
        } catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        EchoClient client = new EchoClient("cse224.sysnet.ucsd.edu", 5555);
    }
}
