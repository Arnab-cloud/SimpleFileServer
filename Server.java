import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

class Server {

    public static void main(String[] args) {
        SocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9000);
        try (ServerSocket sock = new ServerSocket()) {
            sock.bind(endpoint);
            try (Socket clientSock = sock.accept()) {
                PrintWriter writer = new PrintWriter(
                    clientSock.getOutputStream(),
                    true
                );

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSock.getInputStream())
                );

                String input = "You have connected to a simple file server";

                while (input != null && !input.equals("QUIT")) {
                    writer.println(input + '\n');

                    input = reader.readLine();
                    System.out.println("Clinet: " + input);
                }

                writer.println("Exiting...");
                writer.close();
                reader.close();
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
