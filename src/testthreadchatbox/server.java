/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testthreadchatbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static testthreadchatbox.server.listSK;

/**
 *
 * @author Admin
 */
public class server {
    private int port;
    public static ArrayList<Socket> listSK;

    public server(int port) {
        this.port = port;
    }

    private void excute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        WriteServer write = new WriteServer();
        write.start();
        System.out.println("Server is listening");
        while (true) {

            Socket socket = server.accept();
            System.out.println("Server da ket noi voi" + socket);
            listSK.add(socket);
            ReadServer read = new ReadServer(socket);
            read.start();
        }

    }

    public static void main(String[] args) throws IOException {
        server.listSK = new ArrayList<>();
        server server = new server(1);
        server.excute();

    }
}

class ReadServer extends Thread {
    private Socket server;

    public ReadServer(Socket server) {
        this.server = server;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(server.getInputStream());
            while (true) {
                String sms = dis.readUTF();
                if (sms.contains("exit")) {
                    listSK.remove(server);
                    System.out.println("Da ngat ket noi server");
                    dis.close();
                    server.close();
                    continue;
                }
                for (Socket item : listSK) {
                    if (item.getPort() != server.getPort() && item.getInetAddress() != server.getInetAddress()) {
                        DataOutputStream dos = new DataOutputStream(item.getOutputStream());
                        dos.writeUTF(sms);
                    }

                }
                System.out.println(sms);
            }
        } catch (Exception e) {
            try {
                dis.close();
                server.close();
            } catch (IOException ex) {
                System.out.println("Ngat ket noi server");
            }
        }
    }
}

class WriteServer extends Thread {

    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = new Scanner(System.in);
        while (true) {
            String sms = sc.nextLine();
            try {
                for (Socket item : listSK) {
                    dos = new DataOutputStream(item.getOutputStream());
                    dos.writeUTF("server: " + sms);
                }
                sc.close();

            } catch (IOException ex) {
                Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}