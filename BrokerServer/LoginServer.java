import java.io.*;  
import java.net.*;
import java.util.*;

class Server{
    
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream input;
    private DataOutputStream output;
    String clientUsername, clientPassword, username, password;
    int points;

    public Server(int port){

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started");

            System.out.println("Waiting for client");

            socket = serverSocket.accept();
            System.out.println("Client connected");

            input = new DataInputStream( new BufferedInputStream(socket.getInputStream()));

            output = new DataOutputStream( socket.getOutputStream());

            clientUsername = input.readUTF();
            System.out.println("Username: "+ clientUsername);
            clientPassword = input.readUTF();
            System.out.println("Password: "+ clientPassword);

            try{
                BufferedReader br = new BufferedReader(new FileReader("usersList.txt")); 
                String st; 
                while ((st = br.readLine()) != null) 
                {
                  StringTokenizer token = new StringTokenizer(st);
                  while(token.hasMoreElements())
                  {
                    username = token.nextElement().toString();
                    password = token.nextElement().toString();
                    points = Integer.parseInt(token.nextElement().toString());
                  }
                  if( clientUsername.equals(username)){
                    if(clientPassword.equals(password)){
                        String auth = "Authorized!";
                        output.writeUTF(auth);
                        ClientThread clientThread = new ClientThread(socket, input, output, username, points);
                        clientThread.start();            
                    }else{
                        String auth = "Unauthorized, Please enter username and password correctly.";
                        output.writeUTF(auth);
                    }
                }
                }
            }catch( EOFException e){
                System.out.println(e);
            }

    } 
    catch(IOException i) 
    { 
        System.out.println("Error here");
        System.out.println(i); 
    } 
    }
}

class LoginServer{
    public static void main(String[] args) {
        Server Server = new Server(14652);
    }
}

class ClientThread extends Thread{
    private Socket socket;
    private Socket serverSocket;
    private DataInputStream input, serverInput;
    private DataOutputStream output, serverOutput;
    String user;
    int p;

    public ClientThread(Socket socket, DataInputStream input, DataOutputStream output, String username, int points){
        this.socket = socket;
        this.input = input;
        this.output = output;
        user = username;
        p = points;

    }

    public void run(){
        try{

            if(p <= 999){
                serverSocket = new Socket("localhost", 14653);
            }else if(p >= 1000 && p <= 4999){
                serverSocket = new Socket("localhost", 14654);
            }else if(p >= 5000){
                serverSocket = new Socket("localhost", 14655);
            }

            System.out.println("Connected");

            serverInput = new DataInputStream(new BufferedInputStream(serverSocket.getInputStream()));

            serverOutput = new DataOutputStream(serverSocket.getOutputStream());

            String clientIn, servIn;

            servIn = serverInput.readUTF();
            System.out.println(servIn);
            output.writeUTF(servIn);

            while(true){
                clientIn = input.readUTF();
                serverOutput.writeUTF(clientIn);

                servIn = serverInput.readUTF();
                output.writeUTF(servIn);

                System.out.println(servIn);

                if(clientIn.equals("q")){
                    break;
                }
            }


            
        }catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        }

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