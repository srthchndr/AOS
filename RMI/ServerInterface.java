import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{

    public String checkIntegerPalindrome(long num) throws RemoteException;
    public String checkStringPalindrome(String str) throws RemoteException;
    public String checkPatternMatching(String mainStr, String patternStr) throws RemoteException;

}