/*
    clase quee funciona comoo manejaddor del cliientte, corre sobre un threadd.
    
 */
package Cliente;

import Interfaces.LoginPartida;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */


public class JuegoCli extends Thread{
    String idJuego;// = "BURU";
    public Juego gui;
    int tcpPort;// = 7899;
    String tcpIP;// = "localhost";
    int mulPort;// = 6791;
    String mulIP;// = "228.5.6.7";
    LoginPartida Log; //Cooonexioon a RMI
    int puntos;
    
    
    public JuegoCli(String idJuego,int puntos, int tcpPort, String tcpIP, int mulPort, String mulIP , LoginPartida Log) {
        this.idJuego = idJuego;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;
        this.Log = Log;
        this.puntos = puntos;
    }

    @Override
    public void run(){
        try {
            //Conectar a multicast
            MulticastSocket s =null;
            InetAddress group = InetAddress.getByName(mulIP);
            s = new MulticastSocket(mulPort);
            s.joinGroup(group);
            //varriables dde ruun
            byte[] buffer;
            DatagramPacket monstruo;
            int id;
            String aux;
            String puntajes;
            //while de Muuulticast
            while(true){
                buffer = new byte[1000];
                monstruo = new DatagramPacket(buffer, buffer.length);
                puntajes = Log.puntaje();
                gui.cambiaPuntos(puntajes);
                gui.cambiaAvi("Juego!!");
                s.receive(monstruo);
                id = parseInt(new String(monstruo.getData(), 0, monstruo.getLength()));
                //ID cuando alguien gana es 100      
                if(id == 100){
                    s.receive(monstruo);
                    aux = (new String(monstruo.getData(), 0, monstruo.getLength()));
                    gui.ganador(aux);  //escribe quien gano
                    gui.setM();  //pone un cuaddrto en verde
                    gui.habilitaInicio();
                    puntajes = Log.puntaje();
                    gui.cambiaPuntos(puntajes);
                    break;  
                }else{
                    gui.setColores();
                    gui.setMonstruo(id);
                    }
                } 
            } catch (IOException ex) {
            Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
        }
            
} 
    
      

    //cuando le pegas a uun mostroo este metodo manda un aviso al servidor
    //usanddo sockeettss.
    public void golpe(){
        Socket s = null;
        try {
            //Aqui iniiciaa coonex con serrv ppparra avisar que tiene un punto
            puntos = puntos +1;
            s = new Socket(tcpIP, tcpPort);
            DataOutputStream out =
                    new DataOutputStream( s.getOutputStream());
            out.writeUTF(idJuego);
        }catch (IOException ex){
            Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);} 
        finally {
            if(s != null) try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void setGui(Juego gui){
        this.gui = gui;
    }


  
}
