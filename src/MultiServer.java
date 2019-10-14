public class MultiServer {

    public static void createThread(Server server, int index){

        new Thread(()->{
            server.executeConnection(Integer.parseInt("4000"), index);
            server.writeData();
        }).start();
    }

    public static void createThreads(){
        Server server;

        for(int i = 0; i<5; i++) {
            server = new Server();
            createThread(server,i);
        }
    }

    public static void main(String... args){
        createThreads();
    }
}
