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
                Server.start_server();
                
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
                c.start_client("client1");
                
            }
        }).start();
        
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                                Client d = new Client();
                d.start_client("client2");
                //Client.start_client("client2");
                
            }
        }).start();
        
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                                Client e = new Client();
                e.start_client("client3");
                //Client.start_client("client2");
                
            }
        }).start();
           
        
    }
    
}
