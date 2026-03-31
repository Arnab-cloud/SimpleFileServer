import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

class ClientThread extends Thread {

    private static int clientIdGenerator = 0;
    private final Socket clientSocket;
    private final int clientId;

    ClientThread(Socket sock) {
        this.clientSocket = sock;
        this.clientId = getNextClientId();
    }

    private static int getNextClientId() {
        return clientIdGenerator++;
    }

    public int getClientId() {
        return this.clientId;
    }

    @Override
    public void run() {
        try (this.clientSocket) {
            PrintWriter writer = new PrintWriter(
                this.clientSocket.getOutputStream(),
                true
            );

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.clientSocket.getInputStream())
            );

            String input = "You have connected to a simple file server";

            while (input != null && !input.equals("QUIT")) {
                writer.println(input + '\n');

                input = reader.readLine();
                System.out.println(
                    "Clinet - " + this.getClientId() + ": " + input
                );
            }

            writer.println("Exiting...");
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Server {

    public static void main(String[] args) {
        SocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9000);
        try (ServerSocket sock = new ServerSocket()) {
            sock.bind(endpoint);
            while (true) {
                try {
                    Socket clientSock = sock.accept();
                    new ClientThread(clientSock).start();
                } catch (IOException e) {
                    throw e;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
