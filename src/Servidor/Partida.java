/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;
import Interfaces.Conex;
import Interfaces.LoginPartida;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author Francisco
 */

//Intento dedd objeto partda de java
//Guarda a los jugadores que se dan de alta
//E intenta guardar el status de los jugadores
//Para que los jugadores puedan hhacer loggin despues de
//hacer logout y poder jugar

//Es la clase que usa RMI para mandar datos de conexion a los jugadores
//Y varias cosas más
public class  Partida implements LoginPartida  {
    
    public boolean finJuago;
    public boolean enCurso;
    public ArrayList<Jugador> jugadores;
    public String ultima;
    Administrador adm;

    public Partida() {
        finJuago = true;
        enCurso = false;
        this.jugadores = new  ArrayList<Jugador>();
    }

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }
    
    //Se supone que revisa si la partida ya terminó
    //(Que loos jugadores que estan logged ya estén listos para jugar)
    //Creo que este está mal
    public boolean revListos(){
        int num = jugadores.size();
        boolean res = false;
        System.out.println(num);
        if (num > 0) {
            res = true;
            for(int i = 0; i < num ; i++){
                if(jugadores.get(i).isEnJuego()){
                    res = jugadores.get(i).isListo() && res; 
                }
            }
            if(res){
                for(int i = 0; i < num ; i++){
                    if(!jugadores.get(i).isEnJuego()){
                        jugadores.remove(i);
                        i = i-1;
                        num = num-1; 
                    }
                }    
            }   
        }
        return res;
    }
    

    //Regresa una conexioon al usuario
    @Override
    public Conex Conect(String IDPlayer) throws RemoteException {
        //if(jugadores.contains(IDPlayer)){}
        int num = jugadores.size();
        boolean existe = false;
        int i = 0;
        Jugador aux = new Jugador(IDPlayer);
        while(i < num && !existe){
            existe = jugadores.get(i).equals(aux);
            i++;
        }
        if(existe && !jugadores.get(i-1).isEnJuego()){
            jugadores.get(i-1).setEnJuego(true);
            return new Conex(IDPlayer,jugadores.get(i-1).getPuntos(), 7899, "localhost", 6791, "228.5.6.7", adm.arreMon);  
        }else if(existe){
            return new Conex(null);
        }else{
            jugadores.add(new Jugador(IDPlayer, 0));
            System.out.println("Agrego nuevo");
            return new Conex(IDPlayer, 7899, "localhost", 6791, "228.5.6.7");
        }
        
    }
    //Resetea puntos a cero
    public void limpiaRonda() {
        try {
            this.puntaje();
            int num = jugadores.size();
            finJuago = true;
            for(int i = 0; i < num ; i++){
                jugadores.get(i).resetPuntos();
                adm.arreMon = new int[5];
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //El jugador lo mandda para decir que est[a listo
    @Override
    public void listo(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        jugadores.get(index).setListo(true);
    }
    //El jugaddor loo manda para avisar que esta saliendo de la partida
    @Override
    public void logout(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        jugadores.get(index).setEnJuego(false);
    }
    //Le manda un string con los puntajes de todos los jugadores
    @Override
    public String puntaje() throws RemoteException {
        if(!finJuago){
            ultima = jugadores.toString();
        }
        return ultima;
        
    }
    //metodo que se supoone pone a todos los jugaddores 
    //listos para iniciar partida
    public void inicioPartida() {
        limpiaRonda();
        enCurso = true;
        int num = jugadores.size();
        for(int i = 0; i < num ; i++){
            jugadores.get(i).setAcabo(false);
            jugadores.get(i).setListo(false);
            if(!jugadores.get(i).isEnJuego()){
                jugadores.remove(i);
                i = i-1;
                num = num-1;
            }
        }
        
    }

    @Override
    public int misPuntos(String IDPlayer) throws RemoteException {
        int index;
        Jugador aux = new Jugador(IDPlayer);
        index = jugadores.indexOf(aux);
        return jugadores.get(index).getPuntos();
    }

    void siguePartida() {
        enCurso = true;
        int num = jugadores.size();
        for(int i = 0; i < num ; i++){
            jugadores.get(i).setAcabo(false);
            jugadores.get(i).setListo(false);
        }
    }
    
}
