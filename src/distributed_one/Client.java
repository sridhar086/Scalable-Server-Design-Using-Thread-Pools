/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sridhar
 */
class Client {
    
    
    void print_bytes(byte[] b)
    {
        System.out.println("The system out is "+b.length);
        for(int i = 0;i<8;i++)
        {
        System.out.println("bytes are "+i+" "+Integer.toHexString(b[i]));
        }
    }
    
    String SHA1FromBytes(byte  [] data) {
        
    MessageDigest digest = null; 
    try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) { System.out.println("No such algorithm exception "+ex);
            
        }
    byte[] hash  = digest.digest(data);
    BigInteger hashInt = new   BigInteger(1, hash);
    return  hashInt.toString(16); 
    
    }
 
    
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream out;
    private BufferedReader read;
    private LinkedList<String> hashcodes = new LinkedList<String>();
    
    
    
    
    public void start_client(String client_name)
    {        
        String server_host = "127.0.0.1";
        int server_port = 9091;
        int message_rate = 1;
        try
        {
            socket = new Socket(server_host, server_port);
            System.out.println("Connection from "+client_name);
            while(true)
            {
                
            byte[] var = new byte[8];
            new Random().nextBytes(var);
            String orig_hashcodes = SHA1FromBytes(var);
            //System.out.println("sha 1 "+SHA1FromBytes(var));
            hashcodes.add(orig_hashcodes);
            //print_bytes(var);
            // takes input from terminal
            input  = new DataInputStream(socket.getInputStream());
 
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());            
            out.write(var);
            //System.out.println("written the data to the server from "+client_name);
            //byte[] input_hashcode = new byte[40];
            //input.read(input_hashcode);
            //String hashcode = new String(input_hashcode);
            String hashcode =            input.readUTF();
            //System.out.println("read the data from server"+hashcode+" the original is "+orig_hashcodes);
            if (orig_hashcodes.equalsIgnoreCase(hashcode))
            {
                System.out.println("Hashcodes matches for "+client_name);
                //System.out.println("hashcodes matches "+hashcode+" "+orig_hashcodes);
            }
            Thread.sleep(1000/message_rate);
            //socket.close();
        }    
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
}
