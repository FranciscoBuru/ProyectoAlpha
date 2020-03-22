/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.DataInputStream;
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
    //Actualiza puntos, si alguno ya tiene 5 entonces hay un ganador
    //y manda a ese ganaddor por Multicast (Da la orden)
    public synchronized void actpuntos(String nom){
        int puntos;
        int index;
        boolean res = false;
        index = pa.getIndex(nom); // el número de casilla del arreglo en el que está ese jugador
        puntos = pa.getPuntos(index); // actualiza los puntos del jugador y te regresa los nuevos puntos.
        if(puntos == 5){
            res = true; 
            System.out.println(nom);
            adm.ganador(nom);
            pa.limpiaRonda(); // Deja todo listo para el siguiente juego. Se espera a que todos los jugadores
            // le pongan listo para que empiece la nueva partida.
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
