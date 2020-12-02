package com.felixseifert.kth.networkprogramming.task4.email.smtp;

import com.felixseifert.kth.networkprogramming.task4.email.Constants;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class SMTPClient
{

    public static void main(String[] args) throws Exception
    {
        int delay = 1000;
        String username = Base64.encodeBase64String(Constants.USERNAME.getBytes(StandardCharsets.UTF_8));
        String password = Base64.encodeBase64String(Constants.PASSWORD.getBytes(StandardCharsets.UTF_8));

        SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("smtp.kth.se", 465);
        DataOutputStream dataOutputStream= new DataOutputStream(sslSocket.getOutputStream());
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        (new Thread(() -> {
            try
            {
                while((bufferedReader.readLine()) != null)
                    System.out.println("SERVER RESPONSE: "+bufferedReader);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        })).start();

        send(dataOutputStream,"EHLO smtp.gmail.com\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"AUTH LOGIN\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,username + "\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,password + "\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"MAIL FROM:<sanskar@kth.se>\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"RCPT TO:<sanskar@kth.se>\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"DATA\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"Subject: Email test\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"Test 1 2 3\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,".\r\n");
        Thread.sleep(delay);
        send(dataOutputStream,"QUIT\r\n");
    }

    private static void send(DataOutputStream dataOutputStream, String command) throws Exception
    {
        dataOutputStream.writeBytes(command);
        System.out.println("CLIENT REQUEST: "+command);
    }
}