/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class Puntos extends Thread  {
    
    Administrador adm;
    Partida pa;
    Socket clientSocket;
    DataInputStream in;
    //Esstresaamiieinto
    //DataOutputStream out;
    //------------------
    
    
    
    //MManejador de los puntoss de los juggadores, actualkiza los puntos de cada
    //uno cuando recive un mensaje por TCP ded cada uno.
    
    public Puntos(Administrador adm, Socket clientSocket, Partida pa) {
        
        try {
            this.adm = adm;
            this.pa = pa;
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            //Esstresaamiieinto
            //out = new DataOutputStream(clientSocket.getOutputStream());
            //------------------
        } catch (IOException ex) {
            Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Actualiza puntos, si alguno ya tiene 5 entonces hay un ganaddor
    //y manda a ese ganaddor por Multicast (DDa la ordedn)
    public synchronized void actpuntos(String nom){
        int puntos;
        int index;
        boolean res = false;
        index = pa.getIndex(nom);
        puntos = pa.getPuntos(index);
        if(puntos == 5){
            res = true; 
            System.out.println(nom);
            adm.ganador(nom);
            pa.limpiaRonda();
        }
    }
        
    
    
    @Override
    public void run(){
        try {
            //inii temp
            String id;
            id = in.readUTF();
            System.out.println(id);
            actpuntos(id);
            //Esstresaamiieinto
            //out.writeUTF("a");
            //-----------------------
            clientSocket.close();
            //fin tpo
        } catch (IOException ex) {
            Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
