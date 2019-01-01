import java.net.*; 
import java.io.*;
import java.util.*;

class Client{

    private Socket socket;
    private DataInputStream input, serverInput;
    private DataOutputStream output;

    public Client(String address, int port){
        
        try{
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new DataInputStream(System.in);
            serverInput = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            output = new DataOutputStream(socket.getOutputStream());

            String value;
            System.out.print("Username : ");
            value = input.readLine();
            //System.out.print("\n");
            output.writeUTF(value);

            System.out.print("Password : ");
            value = input.readLine();
            //System.out.print("\n");
            output.writeUTF(value);

            String servInp = serverInput.readUTF();
            
            if(servInp.equals("Authorized!")){
                System.out.println(servInp);
                while(true){
                    servInp = serverInput.readUTF();
                    System.out.println(servInp);
                    String msg = input.readLine();
                    output.writeUTF(msg);

                    if(msg.equals("q")){
                        try{ 
                            input.close(); 
                            output.close(); 
                            socket.close(); 
                        }catch(IOException i) 
                        { 
                            System.out.println(i); 
                        } 
                    }
                }
            }else{
                System.out.println("");
            }

        }catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        }  

    }
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 14652);
    }
}