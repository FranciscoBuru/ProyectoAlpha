/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Francisco
 */

//Objeto jugaddor de java.

public class Jugador implements Serializable {
    public String id;
    public int puntos;
    public boolean enJuego;
    public boolean listo;
    public boolean acabo;

    public Jugador(String id, int puntos) {
        this.id = id;
        this.puntos = puntos;
        this.enJuego = true;
        this.listo = false;
        this.acabo = false;
    }

    public Jugador(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  id + ": " + puntos + "\n";
    }
    //GGetters setterrss

    public boolean isAcabo() {
        return acabo;
    }

    public void setAcabo(boolean acabo) {
        this.acabo = acabo;
    }
    

    public boolean isListo() {
        return listo;
    }

    public void setListo(boolean listo) {
        this.listo = listo;
    }



    public boolean equals(Object o) {
        return o.getClass() == this.getClass() && ((Jugador)o).getId().equals(this.id);
    }

    
    
    public String getId() {
        return id;
    }

    public boolean isEnJuego() {
        return enJuego;
    }

    public void setEnJuego(boolean enJuego) {
        this.enJuego = enJuego;
    }

    public int getPuntos() {
        return puntos;
    }

    public int incPuntos() {
        this.puntos = this.puntos + 1;
        return this.puntos;
    }
    
    public void resetPuntos() {
        this.puntos = 0;
    }
    
    
    
    
    
    
}
