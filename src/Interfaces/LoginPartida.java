/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Francisco
 */

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LoginPartida extends Remote{
    
    public Conex Conect( String IDPlayer ) throws RemoteException;

    public void listo(String IDPlayer)throws RemoteException;
    
    public void logout(String IDPlayer)throws RemoteException;
    
    public String puntaje()throws RemoteException;
    
}
