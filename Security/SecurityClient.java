import java.io.*;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.math.*;

//import com.sun.java.util.jar.pack.Package.File;

public class SecurityClient {
  public static void main (String [] args ){
    Communication comm = new Communication(12345, "127.0.0.1");
  }
}

class Communication{

  public static String FILE_TO_RECEIVED = "recievedMedia4.mp3";
  public final static String FILE_TO_SEND = "recievedMedia.mp3";
  public final static int FILE_SIZE = 6022386; 
  Scanner sc;
  Socket sock = null;
  String path;
  byte[] recievedBytes = new byte[FILE_SIZE];
  private int _key[];  // The 128 bit key.
  private byte _keyBytes[];  // original key as found
  private int _padding; 

  public Communication(int port, String host){

    try {
      byte key[] = new BigInteger("39e858f86df9b909a8c87cb8d9ad599", 16).toByteArray();
          int klen = key.length;
          _key = new int[4];
          
          // Incorrect key length throws exception.
          if (klen != 16)
            throw new ArrayIndexOutOfBoundsException(this.getClass().getName() + ": Key is not 16 bytes");

          int j, i;
          for (i = 0, j = 0; j < klen; j += 4, i++)
            _key[i] = (key[j] << 24 ) | (((key[j+1])&0xff) << 16) | (((key[j+2])&0xff) << 8) | ((key[j+3])&0xff);

          _keyBytes = key;  // save for toString.
      sock = new Socket(host, port);
      System.out.println("Connecting...");
      System.out.print("Enter file path: ");
      sc = new Scanner(System.in);
      path = sc.nextLine();
      File myFile = new File(path);
      //byte[] fileBytes = new byte[(int)myFile.length()];
      SendFile(myFile);
      sock.close();
      System.out.println("Sent. Preparing to recieve.");
      sock = new Socket(host, port);
      recievedBytes = FileRecieve();

      sc.close();
      if (sock != null) sock.close();
    }catch(Exception e){
      System.out.println(e);
    }

  }

  byte[] FileRecieve(){
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    byte[] mybytearray  = new byte [FILE_SIZE];

    try {
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(FILE_TO_RECEIVED);
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = bytesRead;
      do {
          bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
          if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);
  

      bos.write(mybytearray, 0 , current);
      int[] ret = new int[mybytearray.length];
      for (int i = 0; i < mybytearray.length; i++)
      {
          ret[i] = mybytearray[i] & 0xff; // Range 0 to 255, not -128 to 127
      }
      byte[] output = decode(ret);
      bos.flush();
      System.out.println("File " + FILE_TO_RECEIVED
          + " downloaded (" + current + " bytes read)");
          
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      System.out.println(new String(output));
    } catch (Exception e) {
      System.out.println(e);
    }
    return mybytearray;
  }

  void SendFile(File myFile){
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;

    try{
      byte [] mybytearray  = new byte [(int)myFile.length()];
      fis = new FileInputStream(myFile);
      bis = new BufferedInputStream(fis);
      bis.read(mybytearray,0,mybytearray.length);
      os = sock.getOutputStream();
      System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
      os.write(mybytearray,0,mybytearray.length);
      os.flush();
      System.out.println("Done.");

      if (bis != null) bis.close();
      if (os != null) os.close();
    }catch(Exception e){
      System.out.println(e);
    }

  }

  public byte [] decode(int b[])
   {
      // create the large number and start stripping ints out, two at a time.
      int intCount = b.length;

      byte outb[] = new byte[intCount * 4];
      int tmp[] = new int[2];

      // decipher all the ints.
      int i, j;
      for (j = 0, i = 0; i < intCount; i += 2, j += 8)
      {
         tmp[0] = b[i];
         tmp[1] = b[i+1];
         decipher(tmp);
         outb[j]   = (byte)(tmp[0] >>> 24);
         outb[j+1] = (byte)(tmp[0] >>> 16);
         outb[j+2] = (byte)(tmp[0] >>> 8);
         outb[j+3] = (byte)(tmp[0]);
         outb[j+4] = (byte)(tmp[1] >>> 24);
         outb[j+5] = (byte)(tmp[1] >>> 16);
         outb[j+6] = (byte)(tmp[1] >>> 8);
         outb[j+7] = (byte)(tmp[1]);
      }

      return outb;
   }

   public byte [] decode(byte b[], int count)
   {
      int i, j;

      int intCount = count / 4;
      int ini[] = new int[intCount];
      for (i = 0, j = 0; i < intCount; i += 2, j += 8)
      {
         ini[i] = (b[j] << 24 ) | (((b[j+1])&0xff) << 16) | (((b[j+2])&0xff) << 8) | ((b[j+3])&0xff);
         ini[i+1] = (b[j+4] << 24 ) | (((b[j+5])&0xff) << 16) | (((b[j+6])&0xff) << 8) | ((b[j+7])&0xff);
      }
      return decode(ini);
   }

   public byte [] decipher(byte v[])
   {
      byte y=v[0];
      byte z=v[1];
      int sum=0xC6EF3720;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      // sum = delta<<5, in general sum = delta * n 

      while(n-->0)
      {
         z -= (y << 4)+c ^ y+sum ^ (y >> 5)+d;
         y -= (z << 4)+a ^ z+sum ^ (z >> 5)+b;
         sum -= delta;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }

   public int [] decipher(int v[])
   {
      int y=v[0];
      int z=v[1];
      int sum=0xC6EF3720;
      int delta=0x9E3779B9;
      int a=_key[0];
      int b=_key[1];
      int c=_key[2];
      int d=_key[3];
      int n=32;

      // sum = delta<<5, in general sum = delta * n 

      while(n-->0)
      {
         z -= (y << 4)+c ^ y+sum ^ (y >> 5) + d;
         y -= (z << 4)+a ^ z+sum ^ (z >> 5) + b;
         sum -= delta;
      }

      v[0] = y;
      v[1] = z;

      return v;
   }
}
  

