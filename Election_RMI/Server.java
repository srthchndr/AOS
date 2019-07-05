import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;

public class Server implements Hello {
    
    public Server() {}
    
    
    List<Voter> voters=new ArrayList<Voter>();
    static ArrayList<String> al=new ArrayList<String>();
    static ArrayList<String> candidatesList=new ArrayList<String>();
    List<String> voted=new ArrayList<String>();;
    Map<String,String> votes=new HashMap<String,String>();
    Map<String,Integer> elecResults=new HashMap<String,Integer>();
    
    public static void Reader(){
        File file = new File("candidates.txt");
        BufferedReader br;
        try {
        br = new BufferedReader(new FileReader(file));
        String st; 
        while ((st = br.readLine()) != null) {
            candidatesList.add(st);
              }
            } catch (IOException e) {
                e.printStackTrace();
            } 
    }
    
    
    public static void Generate(){
        int i;
        char c;
        char d;
        String r;
        for(int x=0;x<1001;x++){
        Random rnd = new Random();
        c = (char) (rnd.nextInt(26) + 'A');
        d = (char) (rnd.nextInt(26) + 'A');
        i = 1000 + rnd.nextInt(9000);
        r=Character.toString(c)+Character.toString(d)+Integer.toString(i);
        al.add(r);
        }
    }

    public String registerVoter(String s){
        Voter v=new Voter();
        v.id=al.get(0);
        v.name=s;
        voters.add(v);
        al.remove(0);
        return v.id;

    }
            

    public String verifyVoter(String s){
        String st="Not a valid voter";
        for(Voter v:voters){
            if(v.id.equalsIgnoreCase(s)){
                 st="The voter ID "+s+" is registered with the name "+v.name;
            }
        }
       return st; 
    }

    public String vote(String candidateName,String voterId){
        if(!candidatesList.contains(candidateName)){
            return "Not a valid candidate";
        }
        if(voted.contains(voterId)){
            return "Already voted";
        }
        String st="Invalid";
        for(Voter v:voters){
            if(v.id.equalsIgnoreCase(voterId)){
                st="Valid";
            }
        }

        if(st.equalsIgnoreCase("Invalid"))
            return "Invalid voter";

        votes.put(voterId,candidateName);
        voted.add(voterId);
        return "Voting is successfull";
    }

    public Map<String,Integer> tallyResults(){
        for(String c:candidatesList){
            elecResults.put(c,Collections.frequency(votes.values(), c));
        }
       return elecResults; 
    } 

    public String winner(){
     return elecResults.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");
            Generate();
            Reader();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
