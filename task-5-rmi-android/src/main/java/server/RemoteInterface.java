package server;
import java.rmi.*;

public interface RemoteInterface extends Remote {
    void getFirstEmail() throws RemoteException;
}