import model.ResponseMessage;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ConnectionSimulation {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36";

    private static final String certificatePath = "localhost.cer";

    private static SSLContext sslContext;

    public static void main(String[] args) throws IOException, URISyntaxException {

        if(args.length != 2) {
            System.out.println("Two arguments required: <port-number> <number-simulated-rounds>");
            System.out.println("Syntax: java " + ConnectionSimulation.class.getSimpleName()
                    + " <port-number> <number-simulated-rounds>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        URI uri = new URI("https://localhost:" + port + "/http_post.html");

        setSSLContext(certificatePath);

        int numberOfRounds = Integer.parseInt(args[1]);

        double averageNumberOfTurns = 0.0;

        for(int i = 0; i < numberOfRounds; i++) {

            String clientId = performGetRequestToGetClientId(uri);

            int startingNumber = ThreadLocalRandom.current().nextInt(1, numberOfRounds + 1);

            int numberOfTurns = playRound(uri, clientId, startingNumber);
            System.out.println("Number of turns needed in " + (i + 1) + ". round: " + numberOfTurns);

            averageNumberOfTurns = ((averageNumberOfTurns * i) + numberOfTurns) / (i + 1);
        }

        System.out.println("Simulated number of rounds: " + numberOfRounds);
        System.out.println("Average number of turns per round: " + averageNumberOfTurns);
    }

    private static int playRound(URI uri, String clientId, int number)
            throws IOException {

        int turnsToWin = 0;

        int lowerBound = 1;
        int upperBound = 101;

        while(true) {
            turnsToWin++;

            String response = performPostRequest(uri, clientId, String.valueOf(number));

            if(response.contains(ResponseMessage.EQUAL.label)) {
                break;
            }

            String[] splits = response.split("<br>");
            String relevantResponsePart;

            if(splits.length <= 3) {
                relevantResponsePart = splits[0];
            }
            else {
                relevantResponsePart = splits[splits.length - 3];
            }


            if(relevantResponsePart.contains(ResponseMessage.LOW.label)) {
                lowerBound = number + 1;
            }
            else if(relevantResponsePart.contains(ResponseMessage.HIGH.label) ){
                upperBound = number - 1;
            }
            number = lowerBound == upperBound || lowerBound > upperBound ? lowerBound :
                    new Random().ints(lowerBound, upperBound).findFirst().getAsInt();
        }

        return turnsToWin;
    }

    private static String performPostRequest(URI uri, String clientId, String number)
            throws IOException {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslContext(sslContext)
                .build();

        String[] headerArray = {
                "Content-Type", "application/x-www-form-urlencoded",
                "Accept", "text/html",
                "User-Agent", USER_AGENT,
                "Cookie", "clientId=" + clientId};

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("number=" + number);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers(headerArray)
                .POST(body)
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(response).body();
    }

    private static String performGetRequestToGetClientId(URI uri) throws IOException {

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslContext(sslContext)
                .build();

        String[] headerArray = {
                "Accept", "text/html",
                "User-Agent", USER_AGENT};

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers(headerArray)
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String cookies = Objects.requireNonNull(response).headers()
                .firstValue("Set-Cookie").orElseThrow();

        return cookies.split("clientId=")[1].split(";")[0];
    }

    private static void setSSLContext(String certificatePath) {
        InputStream inputStream = ConnectionSimulation.class.getClassLoader().getResourceAsStream(certificatePath);

        try {
            X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                    .generateCertificate(inputStream);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry(Integer.toString(1), certificate);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            ConnectionSimulation.sslContext = sslContext;

        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException
                | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
    }
}
