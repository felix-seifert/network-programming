import IO.FileIOManager;
import IO.InputReader;
import IO.OutputWriter;
import model.ResponseMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.regex.Pattern;

public class ClientManager implements Runnable {

    private Socket client;

    private InputReader in;
    private OutputWriter out;

    private String methodType;

    private Map<Integer, List<String>> clientGuessesMap;

    ClientManager(Socket client, Map<Integer, List<String>> clientGuessesMap) {
        this.client = client;
        this.clientGuessesMap = clientGuessesMap;
    }

    @Override
    public void run() {

        try {
            in = new InputReader(this.client.getInputStream());
            out = new OutputWriter(this.client.getOutputStream());

            String startLine = in.readNextLine();
            Integer cookieId = null;

            if (startLine == null || startLine.isBlank()) {
                closeConnection();
                return;
            }

            String receivedContents = startLine + "\r\n" + new String(in.read()).trim();

            if (receivedContents.contains("Cookie")) {
                cookieId = Integer.parseInt(receivedContents.split("clientId=")[1].split("\r")[0]);
            }

            System.out.println(receivedContents);

            StringTokenizer stk = new StringTokenizer(startLine, " ");
            String req = stk.nextToken(), path = "", httpType = "";

            if (stk.hasMoreTokens()) path = stk.nextToken();
            if (stk.hasMoreTokens()) httpType = stk.nextToken();

            if (!httpType.equalsIgnoreCase("HTTP/1.1")) {
                invalidHandler();
                return;
            }
            if (req.equalsIgnoreCase("GET")) {
                this.setMethodType("GET");
                getHandler(cookieId, receivedContents);
                return;
            }
            if (req.equalsIgnoreCase("POST")) {
                this.setMethodType("POST");
                postHandler(cookieId, receivedContents);
                return;
            }
            invalidHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getHandler(Integer id, String receivedContents) throws IOException {
        List<String> responses = new ArrayList<>();
        if (receivedContents.contains("Cookie")) {
            responses = clientGuessesMap.get(id);
        } else {
            GameServer.highestClientId = GameServer.highestClientId + 1;
        }
        String getReply = new String(FileIOManager.readFileBytes("http_post.html"))
                .replaceFirst("<h4>Result -> </h4>", "<h4>Result ->" + responses + " </h4>");
        httpResponseHandler("200 OK", "text/html", getReply.getBytes());
    }

    private void postHandler(Integer id, String receivedContents) throws IOException {
        String data = receivedContents.substring(receivedContents.lastIndexOf("number=") + "number=".length());
        String responseMessage = "";


        if (isNumeric(data)) {
            responseMessage = NumberGuessService.guessNumber(Integer.parseInt(data)).label + " <br>";
        }


        if (Objects.isNull(clientGuessesMap.get(id))) {
            List<String> responsesMessages = new ArrayList<>();
            clientGuessesMap.put(id, responsesMessages);
        }

        if (isNumeric(data) && NumberGuessService.guessNumber(Integer.parseInt(data)).equals(ResponseMessage.EQUAL)) {
            responseMessage = responseMessage + clientGuessesMap.get(id).size();
        }

        List<String> responsesMessages = clientGuessesMap.get(id);
        responsesMessages.add(responseMessage);
        clientGuessesMap.put(id, responsesMessages);

        String postReply = new String(FileIOManager.readFileBytes("http_post.html"))
                .replaceFirst("<h4>Result -> </h4>", "<h4> Result ->"
                        + clientGuessesMap.get(id).toString() + " </h4>");

        httpResponseHandler("200 OK", "text/html", postReply.getBytes());
    }

    private void invalidHandler() throws IOException {
        httpResponseHandler("400 BAD REQUEST", null, null);
    }

    private void httpResponseHandler(String status, String MMI, byte[] contents) throws IOException {
        out.writeLine("HTTP/1.1 " + status);
        if (MMI != null) {
            out.writeLine("Content-Type: " + MMI);
            out.writeLine("Content-Length: " + contents.length);
            out.writeLine("Connection:  keep-alive");
            if (this.methodType.equals("GET")) {
                out.writeLine("Set-Cookie: clientId=" + GameServer.highestClientId
                        + "; expires=Wednesday,31-Dec-20 21:00:00 GMT");
            }
        }
        out.writeLine();
        if (contents != null) out.write(contents);

        closeConnection();
    }

    void closeConnection() throws IOException {
        out.close();
        in.close();
        client.close();
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    private Boolean isNumeric(String data) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(data).matches();
    }
}