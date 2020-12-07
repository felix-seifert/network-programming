package com.felixseifert.kth.networkprogramming.task4.email.imap;

import com.felixseifert.kth.networkprogramming.task4.email.Constants;

import java.io.IOException;
import java.util.Arrays;

public class ReceiveMailApplication {

    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            System.out.println("Two argument required: <username>, <password>");
            System.out.println("Syntax: java " + ReceiveMailApplication.class.getSimpleName() + " <username>, <password");
            System.exit(0);
        }

        IMAPClient imapClient = new IMAPClient();

        imapClient.connect();
        imapClient.login(args[0], args[1]); // 0 and 1 represents username and password respectively.
        imapClient.executeCommand("LIST \"\" *");
        Integer number = imapClient.getNumber();
        imapClient.executeCommand("FETCH "+ number+ " RFC822");
        imapClient.logout();
        try{
            imapClient.disconnect();
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }
}