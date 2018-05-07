/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sridhar
 */



class Server {

    private static double mean(ArrayList<Double> array) {
        double d = 0;
        for(double a: array)
        {
            d+=a;
        }
        double len = array.size();
        
        return d/len;
    }
    
    private static ArrayList<Double> persecond(ArrayList<Double> array, double second)
    {
        ArrayList<Double> arr = new ArrayList<Double>();
        for(double a: array)
        {
            arr.add((a/second));
        }
        return arr;
    }

    /**
     * @param args the command line arguments
     */
    public static int threadcount = 10;
    static volatile ArrayList<Integer> active_threads = new ArrayList<Integer>();
    static volatile boolean shutdown = false;
    
    
    //variables for performance evaluation - START
    public static double MessagesProcessed = 0;
    public static Hashtable<Integer,Double> ClientMessages;
    
    
    public static double MessagesProcessedHistory = 0;
    public static Hashtable<Integer,Double> ClientMessagesHistory;
    
    public static int NoClients = 0;
    
    //variables for performance evaluation - END

    //Socket initializations
    private static ServerSocket listener;
    private static final LinkedList<ConnectionClass> socket_queue = new LinkedList<ConnectionClass>();
    

    public synchronized static void update_params(int y, int clientid)
    {
       if(y == 0 )   //this is to update the parameters from the message processing threads
       {
       Server.MessagesProcessed+=1.0;
       if(Server.ClientMessages.containsKey(clientid))
       {double clm = Server.ClientMessages.get(clientid);
       Server.ClientMessages.put(clientid, clm+1);}
       else
       {Server.ClientMessages.put(clientid, 1.0);}
       
       }
       else //this is to display general output to console every 20 seconds
       {
           // <editor-fold desc="calculating the parameters">
           MessagesProcessedHistory = MessagesProcessed;
           double Mean = 0;
           double SD = 0;
           Date d = new Date();
           double seconds = 10.0;
           //Collection<Integer> CurrentMessageClient =  Server.ClientMessages.values();
           
           ArrayList<Double> CurrentMessageClient = persecond(new ArrayList<Double>(Server.ClientMessages.values()),seconds);
           
           
           if(ClientMessagesHistory.size() != 0)
           {
               
               // <editor-fold desc="averaging previous and current throughput values.">
               //Collection<Integer> PrevMessageClient = Server.ClientMessagesHistory.values();
               ArrayList<Double> PrevMessageClient = persecond(new ArrayList<Double>(Server.ClientMessagesHistory.values()),seconds);
               double CurrentMean = mean(CurrentMessageClient);
               double temp = 0;
               for (int i = 0; i < CurrentMessageClient.size(); i++)
               {
                 double val = CurrentMessageClient.get(i);
                 // Step 2:
                 double squrDiffToMean = Math.pow(val - CurrentMean, 2);
                 // Step 3:
                 temp += squrDiffToMean;
               }
               // Step 4:
               double meanOfDiffs = (double) temp / (double) (CurrentMessageClient.size());     
               double CurrentSD = Math.sqrt(meanOfDiffs);
               
               
               double PrevMean = mean(PrevMessageClient);
               temp = 0;
               for (int i = 0; i < CurrentMessageClient.size(); i++)
               {
                 double val = CurrentMessageClient.get(i);
                 // Step 2:
                 double squrDiffToMean = Math.pow(val - PrevMean, 2);
                 // Step 3:
                 temp += squrDiffToMean;
               }
               // Step 4:
               meanOfDiffs = (double) temp / (double) (CurrentMessageClient.size());     
               double PrevSD = Math.sqrt(meanOfDiffs);
               
               double pair = 2;              
               Mean = (CurrentMean + PrevMean)/ pair;
               SD = (CurrentSD+PrevSD)/pair;
               // </editor-fold>
               
           }
           else
           {
               // <editor-fold desc="averaging only current throughput values.">
               double CurrentMean = mean(CurrentMessageClient);
               double temp = 0;
               for (int i = 0; i < CurrentMessageClient.size(); i++)
               {
                 double val = CurrentMessageClient.get(i);
                 // Step 2:
                 double squrDiffToMean = Math.pow(val - CurrentMean, 2);
                 // Step 3:
                 temp += squrDiffToMean;
               }
               // Step 4:
               double meanOfDiffs = (double) temp / (double) (CurrentMessageClient.size());     
               double CurrentSD = Math.sqrt(meanOfDiffs);
               
               Mean = CurrentMean;
               SD = CurrentSD;
               // </editor-fold>
                    
           }
           
           for(int key: Server.ClientMessages.keySet()){
            //System.out.println("Value of "+key+" is: "+hm.get(key));
            Server.ClientMessagesHistory.put(key, Server.ClientMessages.get(key));
           }
           
           
           
           System.out.println(d +" Server Throughput:"+MessagesProcessed/seconds+" messages/s"
                   +" Active Client Connections:"+NoClients
                   +" Mean per-client throughput:"+Mean+" messages/s"
                   +" Std Dev. per-client throughput:"+SD+" messages/s");
           
           Server.ClientMessages.clear();
           Server.MessagesProcessed = 0;
           
        // </editor-fold>
       }
    
    }
    
    
    public synchronized static ConnectionClass queue_poll()
    {    
        ConnectionClass obj = null;
        try{
            //System.out.println("size of linkedlist is " + socket_queue.size());
            if (socket_queue.size() != 0)
            {obj = socket_queue.poll();}
        }
        catch(Exception e){
            //System.out.println("polling fails because the value is null");
        }        
        if(obj != null)
        {
            //System.out.println("POLLING DOESNT FAIL THIS TIME");
            return obj;       
        } else{
            //System.out.println("returning null socket");
            return null;           
        }
    }
    
    public synchronized static void queue_add(ConnectionClass con)
    {      
        socket_queue.add(con);
        //System.out.println("added socket back to queue");        
    }
    
    static void print_bytes(byte[] b)
    {
        System.out.println("The system out is "+b.length);
        for(int i = 0;i<8;i++)
        {
        System.out.println("server bytes are "+i+" "+Integer.toHexString(b[i]));
        }
    }
    
    void check_for_active_threads()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                while(!shutdown)
                {
                active_threads.add( Thread.activeCount());
                }
                }
                catch(Exception e)
                        {
                        System.out.println("The exception is e "+e.toString());
                        }
                
            }
        }).start();
        
        
    }
    
    static DataInputStream din = null;
    static DataOutputStream dout = null;
    
    public static void Server(String[] args) {
        // TODO code application logic here
        /*
        System.out.println("Creating threads");
        ThreadpoolManager threads = new ThreadpoolManager(10);       
        for (int i = 0; i < 10;i++)
        {
            threads.runnable_queue.get(i).start();
        }
        */    
        
        int portNumber = 9091;
        ClientMessages  = new Hashtable<Integer,Double>();
        ClientMessagesHistory  = new Hashtable<Integer,Double>();
        try
        {
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    
                    try {
                        while(true)
                        {
                        Thread.sleep(10000);
                        Server.update_params(1,0);                       
                        }
                    } catch (InterruptedException ex) {                        
                    }                   
                }
            }).start();
            if (args.length > 0)
            {
                portNumber = Integer.parseInt(args[0]);
                threadcount = Integer.parseInt(args[1]);
            }
            
            ThreadpoolManager th = new ThreadpoolManager(threadcount);
            System.out.println("Listening on the server\n");
            listener = new ServerSocket(portNumber);
            int cid = 1 ;
            //System.out.println("is it possible");
            while (true) {
                //System.out.println("Waiting for a connection\n");
                //Socket socket = listener.accept();
                
                ConnectionClass obj = new ConnectionClass(listener.accept(),cid);
                socket_queue.add(obj);
                NoClients+=1;
                cid +=1;             
                
            }

        }
        catch(Exception e){}
        
        
        
              
        
        
        
        
        
        
        
        
        
        
        
        
        /*
        try {
            for (int i = 0; i < 10;i++)
        {
            threads.runnable_queue.get(i).join();
        }
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }   */
        
        
        shutdown = true;
        
        Set<Integer> uniqueValues = new HashSet<Integer>(active_threads);
        for (int val : uniqueValues)
        {
            System.out.println(val);
        }
        System.out.println(uniqueValues.size());
        
        
    }
    
    
}