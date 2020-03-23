/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class Multicast extends Thread {
    
    String mulIP = "228.5.6.7";;
    int mulPu = 6791;
    MulticastSocket s =null; //-----------------------------------------------------------------
    InetAddress group;
    boolean envTopos = true; // cuando todos los jugadores ponen start se empiezan a mandar topos
    String ganador;
    Administrador adm;

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }

    public void setMulIP(String mulIP) {
        this.mulIP = mulIP;
    }

    public void setEnvTopos(boolean envTopos) {
        this.envTopos = envTopos;
    }
    
    public void setMulPu(int mulPu) {
        this.mulPu = mulPu;
    }

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
    
    public void ganador(String usr){
        System.out.println(usr);
        this.ganador = usr;
        this.envTopos = false;
    }
    
    @Override
    public void run(){
        int aux;
        Random rand = new Random();
        
        while(envTopos){
            try {
                //System.out.println("123");
                aux = rand.nextInt(11) + 1;
                
                String  myMessage= aux + "";
                byte [] m = myMessage.getBytes();
                //Tambien cambia el socket de abajo.
                DatagramPacket messageOut =
                        new DatagramPacket(m, m.length, group, mulPu);
                //Manda mensajes
                s.send(messageOut);
                System.out.println("HOALGOLAGGOLLAGOLA");
                aux = (rand.nextInt(3) + 3)*100;
                Thread.sleep(800);

            } catch (IOException ex) {
                Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        }
        System.out.println("Mandando Ganador");  
        try {
            String  myMessage= "100";
            byte [] m = myMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, mulPu);
            s.send(messageOut);
            System.out.println(ganador);
            m = ganador.getBytes();
            messageOut = new DatagramPacket(m, m.length, group, mulPu);
            s.send(messageOut);
            System.out.println("Ganador Mandaddo");  
            
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    System.out.println("+++++++++++++++++++++++++++++++++++++");
    }
}
