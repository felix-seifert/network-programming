import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {

    public static Integer highestClientId = 0;

    private static final String keystorePassword = "localhost";
    private static final String keystorePath = "localhost.jks";

    public static void main(String[] args) throws IOException {

        if(args.length != 1) {
            System.out.println("One argument required: <port-number>");
            System.out.println("Syntax: java " + GameServer.class.getSimpleName() + " <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        Socket clientSocket;
        SSLServerSocket serverSocket = createServerSocket(port);
        // ServerSocket serverSocket = new ServerSocket(port);

        Map<Integer, List<String>> clientGuessesMap = new HashMap<>();

        System.out.println( "Server started.\nListening for connections on port: " + port + " ...\n" );

        while(true) {
            clientSocket = serverSocket.accept();
            ClientManager clientManager = new ClientManager(clientSocket, clientGuessesMap);
            Thread thread = new Thread(clientManager);
            thread.start();
        }
    }

    private static SSLServerSocket createServerSocket(int port) {

        InputStream inputStream = GameServer.class.getClassLoader().getResourceAsStream(keystorePath);

        SSLServerSocket sslServerSocket = null;

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            keyStore.load(inputStream, keystorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory socketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) socketFactory.createServerSocket(port);

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | KeyManagementException | NoSuchProviderException | IOException e) {
            e.printStackTrace();
        }

        return sslServerSocket;
    }
}