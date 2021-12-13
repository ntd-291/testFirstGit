/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testthreadchatbox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class client {
    private InetAddress host;
    private int port;

    public client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    void excute() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap vao ten cua ban: ");
        String name = sc.nextLine();

        Socket client = new Socket(host, port);
        ReadClient read = new ReadClient(client);
        read.start();
        WriteClient write = new WriteClient(client, name);
        write.start();
        sc.close();

    }

    public static void main(String[] args) throws IOException {

        client client = new client(InetAddress.getLocalHost(), 1);
        client.excute();

    }
}

class ReadClient extends Thread {
    private Socket client;

    public ReadClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                String sms = dis.readUTF();
                System.out.println(sms);
            }
        } catch (Exception e) {
            try {
                dis.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("Ngat ket noi server");
            }
        }
    }
}

class WriteClient extends Thread {
    private Socket client;
    private String name;

    public WriteClient(Socket client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = null;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            sc = new Scanner(System.in);
            while (true) {
                String sms = sc.nextLine();
                dos.writeUTF(name + ": " + sms);
            }
        } catch (Exception e) {
            try {
                dos.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("Ngat ket noi server");
            }
        }
    }
}
