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
    boolean nuevoMonstro = true;
    public Juego gui;
    int tcpPort;// = 7899;
    String tcpIP;// = "localhost";
    int mulPort;// = 6791;
    String mulIP;// = "228.5.6.7";
    LoginPartida Log; //Cooonexioon a RMI
    
    
    public JuegoCli(String idJuego, int tcpPort, String tcpIP, int mulPort, String mulIP , LoginPartida Log) {
        this.idJuego = idJuego;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;
        this.Log = Log;
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
                //System.out.println("Existo");
                //Me llegan por RMI los puntos de los demàs
                //y los esscribo con lo dde abajo
                puntajes = Log.puntaje();
                //puntajes.compareTo("[a: 0,b: 0]") == 0 ;
                gui.cambiaPuntos(puntajes);
                //cuuando corro golpe() camba lla var nuevoMonstro a true
                //se recive y escrbee el monsttro
                if(nuevoMonstro){
                    System.out.println("Entro");
                    s.receive(monstruo);
                    //mando id del nuevo mons
                    id = parseInt(new String(monstruo.getData(), 0, monstruo.getLength()));
                    //si llega 100 es que ya hay un ganaddor
                    //ddespuues del 100 hay llega uun str con el nombre del gana
                    //dor, 
                    if(id == 100){
                        s.receive(monstruo);
                        aux = (new String(monstruo.getData(), 0, monstruo.getLength()));
                        gui.ganador(aux);  //escribe quien gano
                        gui.setM();  //pone un cuaddrto en verde
//---------------------------FALTA---------------------------------------------
                        //Regonfigurar todo para empezar nuevo juego
                            /*
                                1. Vaciar cola multicast
                                2. Poner bootton ¨listo¨ ddispoonible
                        
                            */ 
                        puntajes = Log.puntaje();
                        gui.cambiaPuntos(puntajes);
                        break;  //Truena el juego y te regresa al loop inf
                                //por defauult de la interfaz de juego, hay que
                                //hacer lo necesario para popdder iniciiar un
                                //nueuvo juuego liiego luego.
                    }else{
                        gui.setMonstruo(id);
                        this.nuevoMonstro = false;
                    }
                }
            }  
        } catch (IOException ex) {
            Logger.getLogger(JuegoCli.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //cuando le peghas a uun mostroo este metodo manda un aviso al servidor
    //usanddo sockeettss.
    public void golpe(){
        Socket s = null;
        try {
            this.nuevoMonstro = true;
            //Aqui iniiciaa coonex con serrv ppparra avisar que tiene un punto
            
            s = new Socket(tcpIP, tcpPort);
            
            DataOutputStream out =
                    new DataOutputStream( s.getOutputStream());
            System.out.println("Evi111");
            out.writeUTF(idJuego);
            System.out.println("Envi222");
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
