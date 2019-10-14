import java.util.Scanner;

public class MultiClient {

    private static Client[] clients = new Client[5];
    private static Scanner scanner = new Scanner(System.in);
    private static int index;

    public static void menu(){

        System.out.println("Menu");
        for(int i = 1; i<6; i++){
            System.out.println(i + ". Client #" + i);
        }
        System.out.println("6. Exit");
    }

    public static void createClients(){

        for(int i = 0; i<5; i++){
            clients[i] = new Client();
            clients[i].openConnection("localhost",4000+i, i+1);
            clients[i].openFlows();
        }
    }

    public static void controlMenu(){

        do {
            menu();
            try {
                index = scanner.nextInt();
                if(index<6 && index>0) {
                    clients[index-1].executeConnection();
                    clients[index-1].writeData();
                } else {
                    if(index!=6)
                        throw new Exception();
                }
            } catch (Exception ex){
                System.out.println("Wrong command");
                index = 0;
                scanner.nextLine();
            }
        } while(index != 6);

        for(int i = 0; i<5; i++){
            clients[i].closeConnection();
        }

        System.out.println("Successful exit");
    }

    public static void main(String... args) {

        createClients();
        controlMenu();
    }
}
