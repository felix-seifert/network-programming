package androidClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


@SuppressLint("Registered")
public class MainActivity extends Activity {

    private DataOutputStream out;
    private DataInputStream in;
    TextView connectedStatus;
    TextView emailView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ipAddressText = findViewById(R.id.ipAddress);
        final EditText serverPortText = findViewById(R.id.serverPort);
        connectedStatus = findViewById(R.id.connectedStatus);
        emailView = findViewById(R.id.email);


        Button send = findViewById(R.id.connect);
        Button perform = findViewById(R.id.perform);

        perform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new ClientHandler(out,in)).start();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new ClientThread(ipAddressText.getText().toString(), Integer.parseInt(serverPortText.getText().toString()))).start();
            }
        });
    }

    class ClientHandler implements Runnable {
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public ClientHandler(DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
            this.dataOutputStream = dataOutputStream;
            this.dataInputStream = dataInputStream;
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeUTF("perform");
                dataOutputStream.flush();
                final String response = dataInputStream.readUTF();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emailView.setText(response);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ClientThread implements Runnable {

        String host;
        Integer port;


        public ClientThread(String host, Integer port) {
            this.host=host;
            this.port=port;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(host,port);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connectedStatus.setText("Connected Successfully\n");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}