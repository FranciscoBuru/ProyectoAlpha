/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Interfaces.LoginPartida;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */

//Adminiistrador del servidor

public class Administrador {
    String mulIP = "228.5.6.7";
    int mulPu = 6791;
    MulticastSocket s =null; 
    InetAddress group;
    TCP coenxTCP;
    boolean ganador = false;
    Multicast m;

    public Administrador() {
    
    }

    public void setM(Multicast m){
        this.m = m;
        
    }

    public synchronized void ganador(String nom) {
        System.out.println(nom);
        m.ganador(nom);  
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        try {
            
            Administrador a = new Administrador(); //levanta adm
            Multicast m = new Multicast();
            m.setAdm(a);
            a.setM(m);
            m.iniciaMulticast();
            TCP tcp = new TCP(7899);               //Levanta TCP
            Partida engine = new Partida();        //Levanta Partida
            tcp.setP(engine);                      //sets
            engine.setAdm(a);
            tcp.setAdm(a);                         //..
            tcp.start();                           //inicia hilo de mensajes TCP
            
            //Levanta rmi, aguas con la liga
            System.setProperty("java.security.policy", "file:/C:/Users/Mariella/Documents/ITAM/Semestre 8/Sistemas Distribuidos/ProyectoAlpha/src/Servidor/server.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            LocateRegistry.createRegistry(1099);
            String name = "Login";
            LoginPartida stub = (LoginPartida) UnicastRemoteObject.exportObject( engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,  stub);
            
            // while del administrador. Lo que mantiene al administrador vivo.
            while(true){
                if(!engine.revListos()){  
                    Thread.sleep(200);
                }else{
                    if(!engine.enCurso &&  engine.finJuago){
                        engine.inicioPartida();
                        m.start();  //inicia el hilo de multicast
                        engine.finJuago = false; 
                    }else if(engine.enCurso &&  !engine.finJuago){ // sucede cuando entra un nuevo jugador
                        engine.siguePartida();
                    }else{
                        //Reinicia juego
                        //Tengo que asegurarme de que todos esten listos
                        //antes de iniciar
                        if(engine.revListos()){ 
                            engine.enCurso = false;
                            engine.inicioPartida();
                            m = new Multicast();
                            m.setAdm(a);
                            a.setM(m);
                            m.iniciaMulticast();
                            m.start();
                            engine.finJuago = false;
                        }else{
                            Thread.sleep(100);
                        }
                    }
                }
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
}

