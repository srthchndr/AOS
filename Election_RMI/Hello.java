import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface Hello extends Remote {
    
    String registerVoter(String s) throws RemoteException;
    
    String verifyVoter(String s) throws RemoteException;
    
    String vote(String name,String id) throws RemoteException;
    
    Map<String,Integer> tallyResults() throws RemoteException;
    
    String winner()throws RemoteException;
}