import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

public class ServerInterfaceImplementation extends UnicastRemoteObject implements ServerInterface{
    public ServerInterfaceImplementation() throws RemoteException{

    }

    @Override
    public String checkIntegerPalindrome(long num) throws RemoteException{
        long palindrome = num;
        long reverse = 0;
    
        // Compute the reverse
        while (palindrome != 0) {
            int remainder = (int)palindrome % 10;
            reverse = reverse * 10 + remainder;
            palindrome = palindrome / 10;
        }
    
        if(num == reverse)
            return(num + " is a Palindrome");
        else
            return(num + " is not a Palindrome");
        
    }

    @Override
    public String checkStringPalindrome(String str) throws RemoteException{
        String reverse = "";
        int length = str.length();
       
        for (int i = length - 1; i >= 0; i--)
           reverse = reverse + str.charAt(i);
           
        if (str.equalsIgnoreCase(reverse))
           return("The string is a palindrome.");
        else
           return("The string is not a palindrome.");
    }

    @Override
    public String checkPatternMatching(String mainStr, String patternStr) throws RemoteException{
        Pattern pattern = Pattern.compile(patternStr); 

        Matcher m = pattern.matcher(mainStr); 

        String result = "";
        while (m.find()) 
            result += "\n Pattern found from " + m.start() + " to " + (m.end()-1);
        return(result);
    }
}
