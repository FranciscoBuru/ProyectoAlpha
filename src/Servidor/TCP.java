/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */

//Conexion TCP, cada conex es un hilo y el escuchador de conexc est[a en
//un hilo indepenmdiante.

public class TCP extends Thread  {
    
    Administrador adm;
    Partida pa;
    boolean  hayG = false;
    ServerSocket listenSocket;
    
    public TCP(int serverPort) {
        try {
            //int serverPort = 7899;
            listenSocket = new ServerSocket(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(TCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }
    
    public void setP(Partida pa) {
        this.pa = pa;
    }

    @Override
    public void run(){
        boolean aux;
        try {      
            while(true){
                Socket clientSocket = listenSocket.accept(); 
                Puntos p = new Puntos(adm,clientSocket,pa);
                p.start();
            }
        }
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        } finally {
            try {
                listenSocket.close();
            } catch (IOException e){
                System.out.println(e);
            }
        }
    }
}

