/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distributed_one;

import com.sun.imageio.spi.RAFImageInputStreamSpi;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sridhar
 */
public class Start {
    
    public static void main(String[] args)
    {
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                String[] arg={};
                Server.Server(arg);
                
            }
        }).start();
        
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client c = new Client();   
                String[] arg = {"client1"};
                c.Client(arg);
                
            }
        }).start();
        
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client d = new Client();
                String[] arg = {"client2"};
                d.Client(arg);
                //Client.start_client("client2");
                
            }
        }).start();
        
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client e = new Client();
                String[] arg = {"client3"};
                e.Client(arg);
                //Client.start_client("client2");
                
            }
        }).start();
           
        
    }
    
}
