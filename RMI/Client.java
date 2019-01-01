import java.rmi.*;
import java.util.Scanner;

public class Client{
    public static void main(String[] args){
        try {
            //System.setSecurityManager(new RMISecurityManager());
            String serverURL = "rmi://localhost/Server";
            ServerInterface remoteServer = (ServerInterface)Naming.lookup(serverURL);
            int choice;
            
            while(true){ 
                Scanner scan = new Scanner(System.in);
                System.out.println("_____________________________________________________________");
                System.out.print(" 1. Check Integer Plaindrome \n 2. Check String Plaindrome \n 3. Pattern Matching \n Enter you choice: ");
                choice = scan.nextInt();
                switch(choice){
                    case 1: 
                        Long num;
                        System.out.print(" Enter a number to check for palindrome: ");
                        num = scan.nextLong();
                        System.out.println(remoteServer.checkIntegerPalindrome(num));
                        break;

                    case 2:
                        String str;
                        System.out.print(" Enter a string to check for palindrome: ");
                        scan.nextLine();
                        str = scan.nextLine();
                        System.out.println(remoteServer.checkStringPalindrome(str));     
                        break;      
                    
                    case 3:
                        String mainStr, patternStr;
                        System.out.print(" Enter a String to be searched for Pattern: ");
                        scan.nextLine();
                        mainStr = scan.nextLine();

                        System.out.print(" Enter the pattern to be matched: ");
                        patternStr = scan.nextLine();
                        System.out.println(remoteServer.checkPatternMatching(mainStr, patternStr));
                        break;

                    default: 
                        System.out.println(" You have entered a wrong choice");
                        break;

                }
                System.out.println("_____________________________________________________________");
                String qChoice;

                if(choice == 1 || choice > 3){
                    scan.nextLine();
                }

                System.out.print("Press N to quit or something else to continue: ");
                qChoice = scan.nextLine();
                
                if(qChoice.equals("N") || qChoice.equals("n")){
                    break;
                }else{
                    continue;
                }
            }
        }catch(Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
