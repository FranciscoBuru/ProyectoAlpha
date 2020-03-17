/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class Puntos extends Thread  {
    
    
    boolean  hayG = false;
    Administrador adm;
    Partida pa;
    Socket clientSocket;
    DataInputStream in;

    //MManejador de los puntoss de los juggadores, actualkiza los puntos de cada
    //uno cuando recive un mensaje por TCP ded cada uno.
    
    public Puntos(Administrador adm, Socket clientSocket, Partida pa) {
        
        try {
            this.adm = adm;
            this.pa = pa;
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
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
        System.out.println("Actualizando");   
        System.out.println(pa.jugadores.size());
        index = pa.jugadores.indexOf(new Jugador(nom));
        puntos = pa.jugadores.get(index).incPuntos();
        System.out.println("Puntos J: " + puntos);  
        //Entra cuandddo hay un gganaddor
        if(puntos == 5){
            res = true;
            hayG=true;
            System.out.println("Ganador");  
            adm.ganador(nom);
            pa.limpiaRonda();
        }
    }
        
    
    
    @Override
    public void run(){
        try {
            String id;
            id = in.readUTF();
            System.out.println(id);
            actpuntos(id);
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
