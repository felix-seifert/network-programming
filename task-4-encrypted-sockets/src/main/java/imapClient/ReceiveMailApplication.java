package imapClient;

import java.io.IOException;

public class ReceiveMailApplication {

    public static void main(String[] args) throws IOException {
        IMAPClient imapClient = new IMAPClient();

        imapClient.connect();
        imapClient.login(Constants.USERNAME, Constants.PASSWORD);
        imapClient.executeCommand("LIST \"\" *");
        Integer number = imapClient.getNumber();
        imapClient.executeCommand("FETCH "+ number+ " RFC822");
        imapClient.logout();
        imapClient.disconnect();
    }
}