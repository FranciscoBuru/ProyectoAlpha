/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Interfaces.LoginPartida;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    String mulIP = "228.5.6.7";;
    int mulPu = 6791;
    MulticastSocket s =null; 
    InetAddress group;
    TCP coenxTCP;
    boolean ganador = false;
    //Hashtable< String,Integer> jugadores;
    //inicia comunicacioion multicast
    public void iniciaMulticast(){
        try {
            group = InetAddress.getByName(mulIP);
            s = new MulticastSocket(mulPu);
            s.joinGroup(group);
            s.setTimeToLive(1);
            //s.leaveGroup(group);
            } catch (UnknownHostException ex) { 
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Mandda los 5 monstros del juego
    public void partida(){
        int aux;
        Random rand = new Random();
        //this.jugadores = new Hashtable< String,Integer>();
        for(int i = 0 ; i < 5 ; i++){
            try {
                System.out.println("123");
                aux = rand.nextInt(11) + 1;
                String  myMessage= aux + "";
                byte [] m = myMessage.getBytes();
                //Tambien cambia el socket de abajo.
                DatagramPacket messageOut =
                        new DatagramPacket(m, m.length, group, mulPu);
                //Manda mensajes
                s.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        }
        
    }
    //Hay que acabar este metodo
    public void nuevoJuego(){
    }
    
    //manda el nombre del ganador con multicast
    public synchronized void ganador(String nom){
        ganador = true;
        System.out.println("Manddanddo Ganador");  
        try {
            String  myMessage= "100";
            byte [] m = myMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, mulPu);
            s.send(messageOut);
            m = nom.getBytes();
            messageOut = new DatagramPacket(m, m.length, group, mulPu);
            s.send(messageOut);
            System.out.println("Ganador Mandaddo");  
            
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        try {
            
            Administrador a = new Administrador(); //levanta adm
            a.iniciaMulticast();                   //Inicia multicast 
            TCP tcp = new TCP(7899);               //Levanta TCP
            Partida engine = new Partida();        //Levanta Partida
            tcp.setP(engine);                      //sets
            tcp.setAdm(a);                         //..
            tcp.start();                           //inicia hilo de mensajes TCP
            
            //Levanta rmi, aguas con la liga
            System.setProperty("java.security.policy", "file:/C:/Users/Francisco/Documents/NetBeansProjects/ProyectoAlpha/src/Servidor/server.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            LocateRegistry.createRegistry(1099);
            String name = "Login";
            LoginPartida stub = (LoginPartida) UnicastRemoteObject.exportObject( engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,  stub);
            
            //iiintento de poder iniiciar partiida cuando jugadores le ponen redy
            //Cuando hace login un usuuario que se habia salido se mandan otros 
            //5 monstros .. :(
            while(true){
                //revListos() está en Partida.java, noo jala bien
                if(!engine.revListos()){  
                    Thread.sleep(200);
                }else{
                    engine.inicioPartida();
                    a.partida();  //manda todos los topos
                    System.out.println("-------------------------");
                }
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
}

