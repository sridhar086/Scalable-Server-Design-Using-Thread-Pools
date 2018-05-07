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
    public class ConnectionClass
    {
        public DataInputStream din;
        public DataOutputStream dout;
        public Socket socket;
        public int clientid;
        //public PrintWriter out;
        
        
        ConnectionClass(Socket soc,int clientid)
        {
            try {
                this.din  = new DataInputStream((soc.getInputStream()));
                //out = new PrintWriter(soc.getOutputStream(), true);

                this.dout = new DataOutputStream(soc.getOutputStream());
                this.socket = soc;
                this.clientid = clientid;
            } catch (IOException ex) {
                
            }

        }
    }
