import java.net.*;
import java.rmi.*;

public class Server{
    public static void main(String[] args){
        try {
            //System.setSecurityManager(new RMISecurityManager());
            ServerInterfaceImplementation implementation = new ServerInterfaceImplementation();
            Naming.rebind("Server", implementation);
        }catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}