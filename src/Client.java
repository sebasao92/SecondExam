import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {

    private int clientId;
    private Socket socket;
    private DataInputStream inputBuffer = null;
    private DataOutputStream outputBuffer = null;
    Scanner scanner = new Scanner(System.in);

    public void openConnection(String ip, int port, int clientId) {

        this.clientId = clientId;
        try {
            socket = new Socket(ip, port);
            showText("Connecting to :" + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            showText("An error has occurred while opening connection: " + e.getMessage());
        }
    }

    public static void showText(String text) {
        System.out.println(text);
    }

    public void openFlows() {
        try {
            inputBuffer = new DataInputStream(socket.getInputStream());
            outputBuffer = new DataOutputStream(socket.getOutputStream());
            outputBuffer.flush();
        } catch (IOException e) {
            showText("An error has occurred while opening flows" + e.getMessage());
        }
    }

    public void send(String message) {

        try {
            outputBuffer.writeUTF(message);
            outputBuffer.flush();
        } catch (IOException e) {
            showText("IOException in send() method " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            inputBuffer.close();
            outputBuffer.close();
            showText("Connection has finished");
        } catch (IOException e) {
            showText("IOException in closeConnection() method " + e.getMessage());
        }
    }

    public void executeConnection() {

        new Thread(() -> {
            readData();
        }).start();
    }

    public void readData() {

        String inputData = "";
        try {
            do {
                inputData = inputBuffer.readUTF();
                showText("\n[Server] => " + inputData);
            } while (inputData.equals(""));
        } catch (IOException e) {
            System.out.println("An error has occurred while reading data" + e.getMessage());
        }
    }

    public void writeData() {

        String inputString = "";
        System.out.print("[Client #" + clientId + "] => ");
        inputString = scanner.nextLine();
        if(inputString.length() > 0)
            send(inputString);
    }


}
