import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class server {
    public static void main(String args[]) {
        ServerSocket echoServer = null;
        String line;
        DataInputStream is;
        PrintStream os;
        Socket clientSocket = null;

        try {
            echoServer = new ServerSocket(2222);
        } catch (IOException e) {
            System.out.println(e);
        }

    /*
     * Create a socket object from the ServerSocket to listen to and accept
     * connections. Open input and output streams.
     */
        System.out.println("The server started. To stop it press <CTRL><C>.");
        try {
            clientSocket = echoServer.accept();
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            while (true) { /* As long as we receive data, echo that data back to the client. */
                line = is.readLine();
                os.println("From server: " + line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
