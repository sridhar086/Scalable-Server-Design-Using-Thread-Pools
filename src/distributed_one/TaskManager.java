/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sridhar
 */
public class TaskManager {

    static synchronized String SHA1FromBytes(byte  [] data) {
        
    MessageDigest digest = null; 
    try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) { System.out.println("No such algorithm exception "+ex);
            
        }
    byte[] hash  = digest.digest(data);
    BigInteger hashInt = new   BigInteger(1, hash);
    return  hashInt.toString(16); 
    
    }
 
    
    
    static synchronized  boolean todo() 
    {
        
        connection_class obj = Server.queue_poll();
        if (obj!=null)
        {
            try { 
                byte[] b = new byte[8];
                obj.din.read(b);
                obj.dout.writeUTF(SHA1FromBytes(b));
                /*
                while(true)
                {
                int a = 
                //System.out.println("boooooooooooooooooooom the length is "+a);
                if(a == 8)
                {break;}
                }*/
                
                //Server.print_bytes(b);
                //System.out.println("sha 1 "+SHA1FromBytes(b));
                //byte[] output_hashcode = SHA1FromBytes(b).getBytes();
                //System.out.println("length of valid hashcode is "+output_hashcode.length);
                //obj.dout.write(output_hashcode);
                
                //obj.out.print(SHA1FromBytes(b));
                //System.out.println("returned a valid hashcode");
                //zdin.close();
                //dout.close();
            
                //new Random().nextBytes(b);
                //boolean check = true;
                
            } catch (IOException ex) {     System.out.println("issue with writng code ??");       }
            
            
        Server.queue_add(obj);    
            return true;
        }
        else
        {
        
            
            return false;
        }
        
        
    }
    
}
