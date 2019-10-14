import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int serverId;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream inputBuffer = null;
    private DataOutputStream outputBuffer = null;
    String inputString = "";
    final String EXIT_COMMAND = "chao";

    public void openConnection(int port) {
        try {

            serverSocket = new ServerSocket(port + (serverId -1));
            showText("Waiting input connection to port " + (port + (serverId -1)) + "...\n");
            socket = serverSocket.accept();
            showText("Connection has been enabled with Client #"+ serverId +": " + socket.getInetAddress().getHostName() + "\n\n\n");
        } catch (Exception e) {
            showText("An error has occurred while opening connection: " + e.getMessage());
        }
    }

    public void openFlows() {

        try {
            inputBuffer = new DataInputStream(socket.getInputStream());
            outputBuffer = new DataOutputStream(socket.getOutputStream());
            outputBuffer.flush();
        } catch (IOException e) {
            showText("An error has occurred while opening flows " + e.getMessage());
            System.exit(0);
        }
    }

    public void readData() {

        try {
            do {
                inputString = inputBuffer.readUTF();
                showText("\n[Client #"+ serverId + "] => " + inputString);
                //send();
            } while (!inputString.equals(EXIT_COMMAND));
        } catch (IOException e) {
            closeConnection();
        }
    }

    public void send() {
        try {
            outputBuffer.writeUTF("Client #" + serverId + ", at this moment we don't have service");
            outputBuffer.flush();
        } catch (IOException e) {
            showText("An error has occurred in send method" + e.getMessage());
        }
    }

    public static void showText(String text) {
        System.out.print(text);
    }

    public void writeData() {

        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!inputString.equals("")) {
                System.out.println(inputString);
                send();
                inputString = "";
            }
        }
    }

    public void closeConnection() {

        try {
            inputBuffer.close();
            outputBuffer.close();
            socket.close();
        } catch (IOException e) {
            showText("An error has occurred in closeConnection(): " + e.getMessage());
        } finally {
            showText("Connection has been finished");
        }
    }

    public void executeConnection(int port, int index) {

        new Thread(() -> {
            while (true) {
                try {
                    serverId = index+1;
                    openConnection(port);
                    openFlows();
                    readData();
                } finally {
                    closeConnection();
                }
            }
        }).start();
    }
}
