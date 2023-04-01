package org.metehan;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class MyServer extends Thread{

    private ServerSocket serverSocket;
    @Getter
    private List<MyHandler> handlerList = new ArrayList<>();

    private MyServer() throws IOException {
        serverSocket = new ServerSocket(MyProperties.PORT);

    }
    public void send(MyHandler sender, String message){

        for(MyHandler handler : handlerList){

            if(handler != sender){
                handler.send(message);
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Server started on port " + MyProperties.PORT);

        while(true){

            try{
                Socket socket = serverSocket.accept();
                System.out.println("A client is connected on port " + socket.getPort());

                MyHandler handler = new MyHandler(this, socket);
                handlerList.add(handler);
                handler.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        MyServer server = new MyServer();
        server.start();
    }

}
