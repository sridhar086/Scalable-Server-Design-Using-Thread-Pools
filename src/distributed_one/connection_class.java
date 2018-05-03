package distributed_one;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sridhar
 */
    public class connection_class
    {
        public DataInputStream din;
        public DataOutputStream dout;
        public Socket socket;
        //public PrintWriter out;
        
        
        connection_class(Socket soc)
        {
            try {
                din  = new DataInputStream((soc.getInputStream()));
                //out = new PrintWriter(soc.getOutputStream(), true);

                dout = new DataOutputStream(soc.getOutputStream());
                socket = soc;
            } catch (IOException ex) {
                
            }

        }
    }
