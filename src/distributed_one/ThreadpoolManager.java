/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import static distributed_one.TaskManager.SHA1FromBytes;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

/**
 *
 * @author sridhar
 */
class Threadpool extends Thread implements Runnable
{
    volatile boolean full_shutdown = false;
    
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
    
    
    Threadpool(int threadid)
    {
        System.out.println("Created thread with Thread ID: "+getId());       
    }
    

    @Override
    public void run() {
        try
        {
            while(!full_shutdown)
            {

                connection_class obj = Server.queue_poll();
                if (obj!=null && !obj.socket.isClosed())
                {
                //try { 
                    
                
                byte[] b = new byte[8];
                obj.din.read(b);
                obj.dout.writeUTF(Threadpool.SHA1FromBytes(b));
                Server.queue_add(obj);  
                System.out.println("Thread ID: "+getId());
                //} catch (IOException ex) {     System.out.println("issue with writng code ??");       }
                }
                else if (obj.socket.isClosed())
                {
                    
                }
                
            }
        
                
                //boolean soc_value = TaskManager.todo();
                /*if (soc_value != false){
                System.out.println("Thread ID: "+getId());
                }*/
                
                
            }
        
        catch(Exception e)
        {
            System.out.println("Polling concept is wrong because it exits out of the loop"+e.toString());
        }
        
        
    }
    void shutdown()    
    {
        full_shutdown = true;
    
    }
}

public class ThreadpoolManager {
    
    
    
    
        private volatile int THREADPOOLCOUNT;
    protected LinkedList<Thread> runnable_queue = new LinkedList<Thread>();
    
    ThreadpoolManager(int capacity)
    {
        THREADPOOLCOUNT = capacity;
        for (int i = 0;i < THREADPOOLCOUNT;i++)
        {
            //System.out.println("Creating threadpools");
            Thread th = new Thread(new Threadpool(i));
            th.start();
            
            runnable_queue.add(th);
        }
        
    } 
    
}
