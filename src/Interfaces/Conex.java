/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.Serializable;

/**
 *
 * @author Francisco
 */


//Clase usada para regresar los datos de conexiion al jugador
public class Conex implements Serializable {
    String idJugador;
    int puntos;
    int tcpPort;// = 7899;
    String tcpIP;// = "localhost";
    int mulPort;// = 6791;
    String mulIP;// = "228.5.6.7";
    int arreMon[];
    
    public Conex(String idJugador, int puntos, int tcpPort, String tcpIP, int mulPort, String mulIP, int arreMon[]) {
        this.idJugador = idJugador;
        this.puntos = puntos;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;
        this.arreMon = arreMon;
    }

    public Conex(String idJugador) {
        this.idJugador = idJugador;
    }
    
    public Conex(String idJugador, int tcpPort, String tcpIP, int mulPort, String mulIP) {
        this.idJugador = idJugador;
        this.puntos = -1;
        this.tcpPort = tcpPort;
        this.tcpIP = tcpIP;
        this.mulPort = mulPort;
        this.mulIP = mulIP;
    }

    public String getIdJugador() {
        return idJugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public String getTcpIP() {
        return tcpIP;
    }

    public int getMulPort() {
        return mulPort;
    }

    public String getMulIP() {
        return mulIP;
    }

    public int[] getArreMon() {
        return arreMon;
    }
}
