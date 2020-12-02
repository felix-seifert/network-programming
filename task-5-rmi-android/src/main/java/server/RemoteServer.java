package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteServer {
    public static void main(String args[]){

        try{
            RemoteInterface remoteCounter = new RemoteInterfaceImpl();
            Registry reg = LocateRegistry.createRegistry(1234);   //Can be done manually as well
            reg.rebind("myObject", remoteCounter);
            System.out.println("Server is up!");
        }catch(Exception e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }

    }
}