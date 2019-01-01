import java.io.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.net.*;
import java.math.*;

public class SecurityServer {
  public static void main (String [] args ) throws IOException {
    int port = 12345;
    Communication comm = new Communication(port);
  }

}

class Communication{
  public final static String FILE_TO_SEND = "GodsPlan.mp3";  // you may change this
  public static String FILE_TO_RECEIVED = "recentlyRecievedMedia1.mp3";
  public final static int FILE_SIZE = 6022386;
  public ServerSocket servsock = null;
  public Socket sock = null;
  byte[] recievedBytes = new byte[FILE_SIZE];
  private int _key[];  // The 128 bit key.
  private byte _keyBytes[];  // original key as found
  private int _padding; 

  public Communication(int port){ 
    try {
      servsock = new ServerSocket(port);
      while (true) {
        System.out.println("Waiting...");
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
          sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          recievedBytes = FileRecieve();
          System.out.println("Recieved. Preparing to Send");
          sock.close();
          sock = servsock.accept();
          SendFile();
        }catch(Exception e){
          System.out.println(e);
        }
        finally {
          if (sock!=null) sock.close();
        }
      }
    }
    catch(Exception e){
      System.out.println(e);
    }
    finally {
      try{
        if (servsock != null) servsock.close();
      }catch(Exception e){
        System.out.println(e);
      }
    }
  }

  void SendFile(){
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ByteBuffer bb;
    try{
      File myfile = new File (FILE_TO_RECEIVED);
      byte [] mybytearray  = new byte [(int)myfile.length()];
      fis = new FileInputStream(myfile);
      bis = new BufferedInputStream(fis);
      bis.read(mybytearray,0,mybytearray.length);
      System.out.println("Going to encode");
      int enc[] = encode(mybytearray, mybytearray.length);
      System.out.println("Encoding Success");
      os = sock.getOutputStream();
      System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
      bb = ByteBuffer.allocate(enc.length);
      IntBuffer intBuffer = bb.asIntBuffer();
      intBuffer.put(enc); 
      os.write(bb.array(),0,(int)enc.length);
      
      os.flush();
      System.out.println("Done.");

      if (bis != null) bis.close();
      if (os != null) os.close();
    }catch(Exception e){
      System.out.println(e);
    }

  }

  byte[] FileRecieve(){
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    byte [] mybytearray  = new byte [FILE_SIZE];

    try {
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(FILE_TO_RECEIVED);
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = bytesRead;
      do {
          bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));;
          if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);
 
      bos.write(mybytearray, 0 , current);
      bos.flush();
      System.out.println("File " + FILE_TO_RECEIVED
           + " downloaded (" + current + " bytes read)");

     if (fos != null) fos.close();
      if (bos != null) bos.close();
    }catch (Exception e) {
      System.out.println(e);
    }
    return mybytearray;
  }

  int [] encode(byte b[], int count)
  {
    System.out.println("entered Encode");
     int j ,i;
     int bLen = count;
     byte bp[] = b;

     _padding = bLen % 8;
     if (_padding != 0)   // Add some padding, if necessary.
     {
        _padding = 8 - (bLen % 8);
        bp = new byte[bLen + _padding];
        System.arraycopy(b, 0, bp, 0, bLen);
        bLen = bp.length;
     }

     int intCount = bLen / 4;
     int r[] = new int[2];
     int out[] = new int[intCount];

     for (i = 0, j = 0; j < bLen; j += 8, i += 2)
     {
        // Java's unforgivable lack of unsigneds causes more bit
        // twiddling than this language really needs.
        r[0] = (bp[j] << 24 ) | (((bp[j+1])&0xff) << 16) | (((bp[j+2])&0xff) << 8) | ((bp[j+3])&0xff);
        r[1] = (bp[j+4] << 24 ) | (((bp[j+5])&0xff) << 16) | (((bp[j+6])&0xff) << 8) | ((bp[j+7])&0xff);
        System.out.println("Going to Encipher");
        encipher(r);
        out[i] = r[0];
        out[i+1] = r[1];
     }
     System.out.println("Exiting Encode");
     return out;
  }

  public int [] encipher(int v[])
  {
    System.out.println("Came to encipher int");
     int y=v[0];
     int z=v[1];
     int sum=0;
     int delta=0x9E3779B9;
     int a=_key[0];
     int b=_key[1];
     int c=_key[2];
     int d=_key[3];
     int n=32;

     while(n-->0)
     {
        sum += delta;
        y += (z << 4)+a ^ z+sum ^ (z >> 5)+b;
        z += (y << 4)+c ^ y+sum ^ (y >> 5)+d;
     }

     v[0] = y;
     v[1] = z;

     System.out.println("Exiting Encipher int");
     return v;
  }

  public byte [] encipher(byte v[])
  {
    System.out.println("Entered Encipher");
     byte y=v[0];
     byte z=v[1];
     int sum=0;
     int delta=0x9E3779B9;
     int a=_key[0];
     int b=_key[1];
     int c=_key[2];
     int d=_key[3];
     int n=32;

     while(n-->0)
     {
        sum += delta;
        y += (z << 4)+a ^ z+sum ^ (z >> 5)+b;
        z += (y << 4)+c ^ y+sum ^ (y >> 5)+d;
     }

     v[0] = y;
     v[1] = z;

     System.out.println("Exiting Encipher");
     return v;
  }
}

