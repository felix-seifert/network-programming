package server;
import java.rmi.*;

public interface RemoteInterface extends Remote {
    String getFirstEmail() throws RemoteException;
}