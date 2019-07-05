import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
               int op=1;
            do{ 
            Scanner in = new Scanner(System.in);
            System.out.println(" ");
            System.out.println("Select an operation: ");
            System.out.println("1.Register to Vote");
            System.out.println("2.Verify Voter");
            System.out.println("3.Vote");
            System.out.println("4.Tally Results");
            System.out.println("5.Announce Winner");
            System.out.println("0.Quit");
            System.out.println(" ");
            op = in.nextInt();
            in.nextLine();
            if(op==1){
                System.out.println("Enter voter name:");
                String name=in.nextLine();
                String response=stub.registerVoter(name);
                System.out.println(response);
            }
                
            else if(op==2){
                System.out.println("Enter Voter ID");
                String id=in.nextLine();
                String response=stub.verifyVoter(id);
                System.out.println(response);
            }
                
            else if(op==3){
                System.out.println("Enter Candidate Name and Your Voter ID");
                String name=in.nextLine();
                String id=in.nextLine();
                String response=stub.vote(name,id);
                System.out.println(response);
            }
                
            else if(op==4){
                Map<String,Integer> result=stub.tallyResults();
                for(Map.Entry<String,Integer> et:result.entrySet()){
                    System.out.println("Candidate Name:"+et.getKey()+" votes:"+et.getValue());
                }
            }
            else if(op==5){
                String st=stub.winner();
                 System.out.println(st);
            }
            else if(op==0){
                break;
            }
            else{
                System.out.println("Invalid Option");
            }
        }while(op!=0);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}