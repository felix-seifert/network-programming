package server;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class RemoteInterfaceImpl extends UnicastRemoteObject implements RemoteInterface {
    public RemoteInterfaceImpl()throws RemoteException {
    }

    public String getFirstEmail() {
        Session session =this.getImapSession();
        try{
            Store store = session.getStore("imap");
            store.connect(MailServerConstants.HOST, MailServerConstants.PORT, MailServerConstants.USERNAME, MailServerConstants.PASSWORD);

            IMAPFolder inbox = (IMAPFolder)store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();
            return getTextFromMessage(messages[messages.length-1]);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }
    private Session getImapSession(){
        Properties props = new Properties();
        props.setProperty("mail.store.protocol","imap");
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.imap.host", MailServerConstants.HOST);
        props.setProperty("mail.imap.port", String.valueOf(MailServerConstants.PORT));
        props.setProperty("mail.imap.ssl.enable","true");
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        return session;
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

}