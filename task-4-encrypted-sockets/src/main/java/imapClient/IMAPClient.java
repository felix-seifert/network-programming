package imapClient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;

public class IMAPClient {
    private static final String HOST = "webmail.kth.se";
    private static final int PORT = 993;
    private Socket clientSocket = null;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private int tagNumber = 0;

    public void connect() throws IOException {
        connect(HOST, PORT);
    }

    public void connect(String host, int port) throws IOException {
        try {
            SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            SSLSocket socket = null;
            try{
                socket = (SSLSocket)sf.createSocket(host,port);
            }
            catch(MalformedURLException e){
                System.out.println(e.getMessage());
            }
            assert socket != null;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Unable to find the host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.exit(1);
        }

        log("Successfully Connected to " + host + " at port " + port);
        getAndReadResponse();
    }

    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected();
    }

    public void disconnect() throws IOException {
        if (!isConnected())
            throw new IllegalStateException("No connection exists");

        clientSocket.close();
        in.close();
        out.close();

        log("Disconnected from the host");
    }

    public String getAndReadResponse() throws IOException {
        String res = in.readLine();

        if (res == null) return null;

        if (res.startsWith("-ERR"))
            throw new RuntimeException("Server has returned an error: " + res.replaceFirst("-ERR ", ""));

        log("Server: " + res);
        return res;
    }

    public void executeCommand(String command) throws IOException {
        String tag = "a" + tagNumber++;
        String request = tag + " " + command;
        log("Client: " + request);

        out.write(request + "\r\n");
        out.flush();
        while(!getAndReadResponse().startsWith(tag)) { }
    }

    public void login(String username, String password) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LOGIN ").append(username).append(" ").append(password);
        executeCommand(stringBuilder.toString());
    }

    public void logout() throws IOException {
        executeCommand("LOGOUT");
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

        public Integer getNumber() throws IOException {

          Integer count = 0;
          String command = "SELECT Inbox";

            String tag = "a" + tagNumber++;
            String req = tag + " " + command;
            log("Client: " + req);

            out.write(req + "\r\n");
            out.flush();
            count=Integer.parseInt(getAndReadResponse().replaceAll("[^0-9]", ""));
            return count;
        }

}