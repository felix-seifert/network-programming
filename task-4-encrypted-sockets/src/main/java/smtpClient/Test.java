package smtpClient;


import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


class TotalTemp
{
    private static DataOutputStream dos;

    public static void main(String[] args) throws Exception
    {
        int delay = 1000;
        String user = "sanskar";
        String pass = "Torquerf&%1";
        String username = Base64.encodeBase64String(user.getBytes(StandardCharsets.UTF_8));
        String password = Base64.encodeBase64String(pass.getBytes(StandardCharsets.UTF_8));

        SSLSocket sock = (SSLSocket) SSLSocketFactory.getDefault().createSocket("smtp.kth.se", 465);
        final BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        (new Thread(() -> {
            try
            {
                String line;
                while((line = br.readLine()) != null)
                    System.out.println("SERVER: "+line);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        })).start();
        dos = new DataOutputStream(sock.getOutputStream());

        send("EHLO smtp.gmail.com\r\n");
        Thread.sleep(delay);
        send("AUTH LOGIN\r\n");
        Thread.sleep(delay);
        send(username + "\r\n");
        Thread.sleep(delay);
        send(password + "\r\n");
        Thread.sleep(delay);
        send("MAIL FROM:<sanskar@kth.se>\r\n");
        //send("\r\n");
        Thread.sleep(delay);
        send("RCPT TO:<sanskar@kth.se>\r\n");
        Thread.sleep(delay);
        send("DATA\r\n");
        Thread.sleep(delay);
        send("Subject: Email test\r\n");
        Thread.sleep(delay);
        send("Test 1 2 3\r\n");
        Thread.sleep(delay);
        send(".\r\n");
        Thread.sleep(delay);
        send("QUIT\r\n");
    }

    private static void send(String s) throws Exception
    {
        dos.writeBytes(s);
        System.out.println("CLIENT: "+s);
    }
}