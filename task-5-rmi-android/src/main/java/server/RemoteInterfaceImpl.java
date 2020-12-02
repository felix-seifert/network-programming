package server;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class RemoteInterfaceImpl extends UnicastRemoteObject implements RemoteInterface {
    public RemoteInterfaceImpl()throws RemoteException {
    }

    public void getFirstEmail() {
        Session session =this.getImapSession();
        try{
            Store store = session.getStore("imap");
            store.connect(MailServerConstants.HOST, MailServerConstants.PORT, MailServerConstants.USERNAME, MailServerConstants.PASSWORD);

            IMAPFolder inbox = (IMAPFolder)store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.getMessages();
            System.out.println(messages[messages.length-1].getContent());
        } catch (Exception e){
            System.out.println(e.getMessage());
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

}