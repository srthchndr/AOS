import java.io.*;  
import java.net.*;
import java.util.*;

class SilverGroup extends Thread{
    private int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream input;
    private DataInputStream serverInput;
    private DataOutputStream output;

    public SilverGroup(int port){
        this.port = port;
    }

        public void run(){
            try{
                serverSocket = new ServerSocket(port);
                    System.out.println("Server Started");
    
                    System.out.println("Waiting for client");
    
                    socket = serverSocket.accept();
                    System.out.println("Client connected to Gold");
    
                    input = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
    
                    output = new DataOutputStream( socket.getOutputStream());
    
                    output.writeUTF("Do you want to Buy something?");
                    String st, readLine, request, requestItem, item;
                    String servrResponse = ""; 
                    int quantity, quant, points;
    
                    while(true){
                        st = input.readUTF();
    
                        if(st.equals("yes")){
    
                            String servrFileResponse = "";
                            String writeString = "";
                            BufferedReader br = new BufferedReader(new FileReader("SilverGItems.txt"));
                            while((readLine = br.readLine()) != null){
                                servrFileResponse += readLine;
                                servrFileResponse += "\n";
                            }

                             String serverFileResponse = servrFileResponse;
                            serverFileResponse += "Enter Item Name and its quantity with space";
                            output.writeUTF(serverFileResponse);
    
                            request = input.readUTF();
                            StringTokenizer requestToken = new StringTokenizer(request);
                            while(requestToken.hasMoreElements()){
                                requestItem = requestToken.nextElement().toString();
                                quant = Integer.parseInt(requestToken.nextElement().toString());        
    
                                    StringTokenizer token = new StringTokenizer(servrFileResponse);
                                    while(token.hasMoreElements()){
                                        item = token.nextElement().toString();
                                        quantity = Integer.parseInt(token.nextElement().toString());
                                        points = Integer.parseInt(token.nextElement().toString());
                                        if(item.equals(requestItem)){
                                            if(quantity >= quant){
                                                quantity = quantity - quant;
                                            }else{
                                                output.writeUTF("Your requirement is more than our stock");
                                            }
                                        }    
                                        writeString += item + "\t" + quantity + "\t" + points + "\n";
    
                                    }
                                    BufferedWriter writer = new BufferedWriter(new FileWriter("SilverGItems.txt"));
                                    writer.write(writeString);
    
                                    writer.close();
                                }
                        }else if(st.equals("no")){
                            servrResponse = "Thanks for your time.";
                            output.writeUTF(servrResponse);
                            // close connection 
                            socket.close(); 
                            input.close();
                            output.close();
                            break;
                        }else if(st.equals("q")){
                            // close connection 
                            socket.close(); 
                            input.close();
                            output.close();
                            break;
                        }
    
                    } 
            }  
        catch(IOException i) 
        { 
            System.out.println("Error here");
            System.out.println(i); 
        } 
    }
}

class GoldGroup extends Thread{
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream input;
    private DataOutputStream output;
    private int port;

    public GoldGroup(int port){
        this.port = port;
    }

    public void run() {
        try{
            serverSocket = new ServerSocket(port);
                System.out.println("Server Started");

                System.out.println("Waiting for client");

                socket = serverSocket.accept();
                System.out.println("Client connected to Gold");

                input = new DataInputStream( new BufferedInputStream(socket.getInputStream()));

                output = new DataOutputStream( socket.getOutputStream());

                output.writeUTF("Do you want to Buy something?");
                String st, readLine, request, requestItem, item;
                String servrResponse = ""; 
                int quantity, quant, points;

                while(true){
                    st = input.readUTF();

                    if(st.equals("yes")){

                        String servrFileResponse = "";
                        String writeString = "";
                        BufferedReader br = new BufferedReader(new FileReader("GoldGItems.txt"));
                        while((readLine = br.readLine()) != null){
                            servrFileResponse += readLine;
                            servrFileResponse += "\n";
                        }

                         String serverFileResponse = servrFileResponse;
                        serverFileResponse += "Enter Item Name and its quantity with space";
                        output.writeUTF(serverFileResponse);

                        request = input.readUTF();
                        StringTokenizer requestToken = new StringTokenizer(request);
                        while(requestToken.hasMoreElements()){
                            requestItem = requestToken.nextElement().toString();
                            quant = Integer.parseInt(requestToken.nextElement().toString());        

                                StringTokenizer token = new StringTokenizer(servrFileResponse);
                                while(token.hasMoreElements()){
                                    item = token.nextElement().toString();
                                    quantity = Integer.parseInt(token.nextElement().toString());
                                    points = Integer.parseInt(token.nextElement().toString());
                                    if(item.equals(requestItem)){
                                        if(quantity >= quant){
                                            quantity = quantity - quant;
                                        }else{
                                            output.writeUTF("Your requirement is more than our stock");
                                        }
                                    }    
                                    writeString += item + "\t" + quantity + "\t" + points + "\n";

                                }
                                BufferedWriter writer = new BufferedWriter(new FileWriter("GoldGItems.txt"));
                                writer.write(writeString);

                                writer.close();
                            }
                    }else if(st.equals("no")){
                        servrResponse = "Thanks for your time.";
                        output.writeUTF(servrResponse);
                        // close connection 
                        socket.close(); 
                        input.close();
                        output.close();
                        break;
                    }else if(st.equals("q")){
                        // close connection 
                        socket.close(); 
                        input.close();
                        output.close();
                        break;
                    }

                } 
        } 
        catch(IOException i) 
        { 
            System.out.println("Error here");
            System.out.println(i); 
        }
    }
}

class PlatinumGroup extends Thread{
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream input;
    private DataOutputStream output;
    private int port;

    public PlatinumGroup(int port){
        this.port = port;
    }

    public void run() {
        try{
            serverSocket = new ServerSocket(port);
                System.out.println("Server Started");

                System.out.println("Waiting for client");

                socket = serverSocket.accept();
                System.out.println("Client connected to Gold");

                input = new DataInputStream( new BufferedInputStream(socket.getInputStream()));

                output = new DataOutputStream( socket.getOutputStream());


                output.writeUTF("Do you want to Buy something?");
                String st, readLine, request, requestItem, item;
                String servrResponse = ""; 
                int quantity, quant, points;

                while(true){
                    st = input.readUTF();

                    if(st.equals("yes")){

                        String servrFileResponse = "";
                        String writeString = "";
                        BufferedReader br = new BufferedReader(new FileReader("PlatinumGItems.txt"));
                        while((readLine = br.readLine()) != null){
                            servrFileResponse += readLine;
                            servrFileResponse += "\n";
                        }

                         String serverFileResponse = servrFileResponse;
                        serverFileResponse += "Enter Item Name and its quantity with space";
                        output.writeUTF(serverFileResponse);

                        request = input.readUTF();
                        StringTokenizer requestToken = new StringTokenizer(request);
                        while(requestToken.hasMoreElements()){
                            requestItem = requestToken.nextElement().toString();
                            quant = Integer.parseInt(requestToken.nextElement().toString());        

                                StringTokenizer token = new StringTokenizer(servrFileResponse);
                                while(token.hasMoreElements()){
                                    item = token.nextElement().toString();
                                    quantity = Integer.parseInt(token.nextElement().toString());
                                    points = Integer.parseInt(token.nextElement().toString());
                                    if(item.equals(requestItem)){
                                        if(quantity >= quant){
                                            quantity = quantity - quant;
                                        }else{
                                            output.writeUTF("Your requirement is more than our stock");
                                        }
                                    }    
                                    writeString += item + "\t" + quantity + "\t" + points + "\n";

                                }
                                output.writeUTF(writeString);
                                BufferedWriter writer = new BufferedWriter(new FileWriter("PlatinumGItems.txt"));
                                writer.write(writeString);

                                writer.close();
                            }
                    }else if(st.equals("no")){
                        servrResponse = "Thanks for your time.";
                        output.writeUTF(servrResponse);
                        // close connection 
                        socket.close(); 
                        input.close();
                        output.close();
                        break;
                    }else if(st.equals("q")){
                        // close connection 
                        socket.close(); 
                        input.close();
                        output.close();
                        break;
                    }

                } 
        } 
        catch(IOException i) 
        { 
            System.out.println("Error here");
            System.out.println(i); 
        }
    }
}

class GroupServer{
    public static void main(String[] args) {
        SilverGroup silverGroup = new SilverGroup(14653);
        silverGroup.start();

        GoldGroup goldGroup = new GoldGroup(14654);
        goldGroup.start();

        PlatinumGroup platinumGroup = new PlatinumGroup(14655);
        platinumGroup.start();

    }
}