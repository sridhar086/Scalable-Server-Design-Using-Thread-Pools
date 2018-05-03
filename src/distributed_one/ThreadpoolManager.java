/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import java.util.LinkedList;

/**
 *
 * @author sridhar
 */


class Threadpool implements Runnable
{
    volatile boolean full_shutdown = false;
    private int threadid;
    
    Threadpool(int threadid)
    {
        threadid = threadid;
        System.out.println("created a thread with thread ID: "+threadid);
        
    }
    

    @Override
    public void run() {
        try
        {
            while(!full_shutdown)
            {
                
                //System.out.println("Threads are running");
                boolean soc_value = TaskManager.todo();
                if (soc_value != false){
                System.out.println("processed by thread ID: "+this.threadid);
                }
                
                
            }
        
        }catch(Exception e)
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
