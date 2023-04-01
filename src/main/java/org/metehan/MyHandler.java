package org.metehan;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyHandler extends Thread {

    private MyServer server;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public MyHandler(MyServer server, Socket socket) throws IOException {

        this.server = server;
        this.socket = socket;

        InputStream in = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(in);
        reader = new BufferedReader(isr);

        OutputStream out = socket.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(out);
        writer = new BufferedWriter(osr);
    }
    public void send(String response){
        try {
            writer.write(response+"\r\n");
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {

            try {
                String request = reader.readLine();
                System.out.println(request);

                if(request.split("~")[1].equals("null") ){
                    throw new Exception();
                }
                server.send(this, request);

            } catch (Exception e) {
                System.out.println("Client is disconnected " + socket.getPort());
                server.getHandlerList().remove(this);
                break;
            }
        }
    }
}
