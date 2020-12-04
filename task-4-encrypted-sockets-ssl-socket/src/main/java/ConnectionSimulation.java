import model.ResponseMessage;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ConnectionSimulation {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36";

    private static final String certificatePath = "localhost.cer";

    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            System.out.println("Two arguments required: <port-number> <number-simulated-rounds>");
            System.out.println("Syntax: java " + ConnectionSimulation.class.getSimpleName()
                    + " <port-number> <number-simulated-rounds>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);
        URL  url = new URL("https://localhost:" + port + "/http_post.html");

        setDefaultSSLSocketFactory(certificatePath);

        int numberOfRounds = Integer.parseInt(args[1]);

        double averageNumberOfTurns = 0.0;

        for(int i = 0; i < numberOfRounds; i++) {
            String clientId = performGetRequestToGetClientId(url);

            int startingNumber = ThreadLocalRandom.current().nextInt(1, numberOfRounds + 1);

            int numberOfTurns = playRound(url, clientId, startingNumber);
            System.out.println("Number of turns needed in " + (i + 1) + ". round: " + numberOfTurns);

            averageNumberOfTurns = ((averageNumberOfTurns * i) + numberOfTurns) / (i + 1);
        }

        System.out.println("Simulated number of rounds: " + numberOfRounds);
        System.out.println("Average number of turns per round: " + averageNumberOfTurns);
    }

    private static int playRound(URL url, String clientId, int number) throws IOException {

        int turnsToWin = 0;

        int lowerBound = 1;
        int upperBound = 101;

        while(true) {
            turnsToWin++;

            String response = performPostRequest(url, clientId, String.valueOf(number));

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

    private static String performPostRequest(URL url, String clientId, String number) throws IOException {

//        HttpRequest httpRequest = HttpRequest.newBuilder()
//                .uri()


        String inputString = "number=" + number;

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "text/html");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Cookie", "clientId=" + clientId);
        connection.setDoOutput(true);

        try(OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input);
            outputStream.flush();
        }

        //System.out.println(connection.getResponseMessage());

//        try(DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream())) {
//            String numberString = "number=" + URLEncoder.encode(number, StandardCharsets.UTF_8);
//            dataOutputStream.writeBytes(numberString);
//            dataOutputStream.flush();
//        }

        connection.connect();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String responseLine;
            while((responseLine = reader.readLine()) != null) {
                stringBuilder.append(responseLine.trim());
            }
            return stringBuilder.toString();
        }
    }

    private static String performGetRequestToGetClientId(URL url) throws IOException {

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);

        connection.connect();

        String cookies = connection.getHeaderField("Set-Cookie");

        return cookies.split("clientId=")[1].split(";")[0];
    }

    private static void setDefaultSSLSocketFactory(String certificatePath) {
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

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException
                | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
    }
}
