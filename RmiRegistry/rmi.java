package assignment3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*; 

class HashTable implements Interface{
    private Socket socket;
    private ServerSocket SS;
    private DataInputStream input;
    private DataOutputStream output;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    Hashtable registryHash;

    /*public hashTable(){
        try{
            
            input = new DataInputStream( new BufferedInputStream(socket.getInputStream()));
        
            output = new DataOutputStream( socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String inp;
            inp = input.readUTF();

            if(inp.equals("bind") || inp.equals("rebind")){
                String key = input.readUTF();
                System.out.println("Key: "+ val);
                Object value = ois.readObject();
                if(inp.equals("bind")){
                    bind(key, value);
                }else{
                    rebind(key, value);
                }
            }else if(value.equals("unbind")){
                String key = input.readUTF();
                unbind(key);
            }else if(inp.equals("lookup")){
                String key = input.readUTF();
                Object obj = lookup(key);
                oos.writeObject(obj);
            }else{
                String lst = list();
                output.writeUTF(lst);
            }
        }catch( IOException e){
            System.out.println(e);
        }
    }*/
    
    //creating a hash table 
    //Hashtable h = new Hashtable();

    void bind(String key, Object value){

        socket = new Socket("localhost", 12345);

        registryHash = (Hashtable)ois.readObject();
        if (registryHash.containsKey(key)) 
            System.out.println("Cannot Bind. Name already exists.");
        else
            registryHash.put(key, value);
        
        oos.writeObject(registryHash);
        socket.close();
    }

    void rebind(String key, Object value){

        socket = new Socket("localhost", 12345);

        registryHash = (Hashtable)ois.readObject();
        if (registryHash.containsKey(key)){
            unbind(key);
            registryHash.put(key, value);
        }     
        else
            registryHash.put(key, value);

        oos.writeObject(registryHash);
        socket.close();
    }

    void unbind(String key){
        socket = new Socket("localhost", 12345);

        registryHash = (Hashtable)ois.readObject();
        if(registryHash.containsKey(key)){
            registryHash.remove(key);
        }
        else
            System.out.println("Entry doesnt exist");

        oos.writeObject(registryHash);
        socket.close();
    }

    Object lookup(String key){
        socket = new Socket("localhost", 12345);

        registryHash = (Hashtable)ois.readObject();  
        if(registryHash.containsKey(key)){
            oos.writeObject(registryHash);
            socket.close();
            return registryHash.get(key);
        }
        else{
            oos.writeObject(registryHash);
            socket.close();
            return null;
        }          
    }

    String list(){
        socket = new Socket("localhost", 12345);

        registryHash = (Hashtable)ois.readObject();

        if (registryHash.isEmpty()){
            oos.writeObject(registryHash);
            socket.close();
            return "No entries in the registry";    
        }
        else{
            Set sKey = registryHash.keySet();
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : sKey) {
                stringBuilder.append(str).append("\n");
            }
            oos.writeObject(registryHash);
            socket.close();
            return stringBuilder;
        } 
    }
}

/*class rmi       
{ 
        public static void main(String[] arg) 
    { 
        hashTable htable = new hashTable(); 
    } 
} */

