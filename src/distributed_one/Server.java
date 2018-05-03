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
import java.util.ArrayList;
import java.util.HashSet;
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

    /**
     * @param args the command line arguments
     */
    int threadcount = 10;
    static volatile ArrayList<Integer> active_threads = new ArrayList<Integer>();
    static volatile boolean shutdown = false;
    
    
    //Socket initializations
    private static ServerSocket listener;
    private static final LinkedList<connection_class> socket_queue = new LinkedList<connection_class>();
    

    
    public static connection_class queue_poll()
    {
        
        
        connection_class obj = null;
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
    
    public static void queue_add(connection_class con)
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
    
    public static void start_server() {
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
        
        try
        {
            
            ThreadpoolManager th = new ThreadpoolManager(10);
            System.out.println("Listening on the server\n");
            listener = new ServerSocket(portNumber);
            
            //System.out.println("is it possible");
            while (true) {
                //System.out.println("Waiting for a connection\n");
                //Socket socket = listener.accept();
                connection_class obj = new connection_class(listener.accept());
                socket_queue.add(obj);
                //System.out.println("will you add socket to the queue");
                
                //System.out.println("The connection class is added to the queue");
                
                /*
                System.out.println("accepted the connection \n");
                din  = new DataInputStream(socket.getInputStream());  
                dout = new DataOutputStream(socket.getOutputStream());
                //System.out.println("Trying to read the data ");
                byte[] b = new byte[8];
                //new Random().nextBytes(b);
                //boolean check = true;
                                
                din.read(b);
                
                //print_bytes(b);
                */                     
                
                
                
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
        System.out.println(TaskManager.count);
        
    }
    
    
}