package client;
import server.RemoteInterface;

import java.rmi.*;

public class LocalClient{
    public static void main(String args[]){
        try {
            RemoteInterface remoteInterface = (RemoteInterface) Naming.lookup("//localhost:1234/myObject");

            System.out.println(remoteInterface.getFirstEmail());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


