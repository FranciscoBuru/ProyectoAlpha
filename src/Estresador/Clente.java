/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estresador;

import Cliente.Juego;
import Interfaces.Conex;
import Interfaces.LoginPartida;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class Clente extends Thread{
    String idJuego;// = "BURU";
    int tcpPort= 7899;
    String tcpIP= "localhost";
    int mulPort= 6791;
    String mulIP= "228.5.6.7";
    int puntos = 0;
    LoginPartida Log;
    float promedio;
    Promedio p;

public Clente(String idJuego, Promedio p) {
        this.idJuego = idJuego;
        this.p = p;
        //this.Log = Log;
    }

    public float getPromedio() {
        return promedio;
    }

    @Override
    public void run(){
        try {
            //Conectar a multicast
            Random rand = new Random();
            int aux2;
            MulticastSocket s =null;
            InetAddress group = InetAddress.getByName(mulIP);
            //s = new MulticastSocket(mulPort);
            //s.joinGroup(group);
            //varriables dde ruun
            byte[] buffer;
            DatagramPacket monstruo;
            int id;
            String aux;
            String puntajes;
            long tiempo = 0;
            int golpes = 0;
            //while de Muuulticast
            while(true){
                buffer = new byte[1000];
                monstruo = new DatagramPacket(buffer, buffer.length);
//                puntajes = Log.puntaje();
//                gui.cambiaPuntos(puntajes);
//                gui.cambiaAvi("Juego!!");
                //s.receive(monstruo);
                //id = parseInt(new String(monstruo.getData(), 0, monstruo.getLength()));
                ///id = 0;
                //ID cuando alguien gana es 100      
                if(golpes == 5){
                    //s.receive(monstruo);
                    //aux = (new String(monstruo.getData(), 0, monstruo.getLength()));
//                    gui.ganador(aux);  //escribe quien gano
//                    gui.setM();  //pone un cuaddrto en verde
//                    gui.habilitaInicio();
//                    puntajes = Log.puntaje();
//                    gui.cambiaPuntos(puntajes);
                   // s.close();
                    
                    promedio = tiempo/golpes;
                    p.actSuma(promedio);
                    System.out.println(promedio); 
                    break;  
                }else{
                   aux2 = rand.nextInt(3);
                    //System.out.println(aux2);
                    if(aux2 == 0){
                        tiempo = tiempo + this.golpe();
                        golpes++;
                        //System.out.println(golpes);
                    }
                }
                Thread.sleep(10);
            } 
            
            } catch (IOException ex) {
            Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
        }
            
} 
    
      

    //cuando le pegas a uun mostroo este metodo manda un aviso al servidor
    //usanddo sockeettss.
    public long golpe(){
        Socket s = null;
        long time;
        try {
            //Aqui iniiciaa coonex con serrv ppparra avisar que tiene un punto
            puntos = puntos +1;
            //System.out.println("123");
            s = new Socket(tcpIP, tcpPort);
            DataOutputStream out =
                    new DataOutputStream( s.getOutputStream());
            time = System.currentTimeMillis();
            out.writeUTF(idJuego);
           // System.out.println("1234");
            DataInputStream in = new DataInputStream(s.getInputStream());
            in.readUTF();
           // System.out.println("12345");
            time = System.currentTimeMillis() - time;
            return time;
        }catch (IOException ex){
            Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } 
        finally {
            if(s != null) try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void main(String[] args) {
        try {
            Clente c;
            Promedio p = new Promedio();
            Conex con;
            System.setProperty("java.security.policy", "file:/C:/Users/Francisco/Documents/NetBeansProjects/ProyectoAlpha/src/Cliente/client.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            String name = "Login";
            Registry registry = LocateRegistry.getRegistry("localhost");  //Aqui va la IP del servidor
            LoginPartida Log = (LoginPartida) registry.lookup(name);
            for(int i = 0; i < 500 ; i++){
                con = Log.Conect(i+1 + "");
                c = new Clente(i+1 + "" , p);  
                c.start();
            }
            System.out.println(p.getProm());   
        } catch (RemoteException ex) {
            Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Clente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}


class Promedio {
    
    
    float prom = 0;
    
    public synchronized void actSuma(float num){
        prom = prom + num;
    }

    public float getProm() {
        return prom;
    }
    
    
    
}

