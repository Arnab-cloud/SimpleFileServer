import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

class Client {

    public static void main(String[] args) {
        SocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9000);
        try (Socket sock = new Socket()) {
            sock.connect(endpoint);

            PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(sock.getInputStream())
            );

            Scanner cmdScanner = new Scanner(System.in);
            String cmd = "";
            String serverMsg = "";

            while (!sock.isClosed()) {
                serverMsg = reader.readLine();
                while (serverMsg != null && !serverMsg.isEmpty()) {
                    System.out.println("Server: " + serverMsg);
                    serverMsg = reader.readLine();
                }
                if (serverMsg == null) break;

                System.out.print("> ");

                cmd = cmdScanner.nextLine();
                writer.println(cmd);
            }

            cmdScanner.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
