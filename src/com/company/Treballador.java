package com.company;

import java.util.concurrent.Semaphore;

/**
 *
 */
public abstract class Treballador extends Thread {

    protected Semaphore permGenere; //Semafor que dona pas a un proces Dona o Home segons la race condition.
    protected Semaphore porta;      //Semafor que deixa pasar tant a un proces Dona u Home,
                                    // mantenint l'ordre d'entrada.
    protected String nomTreb;       //Nom del treballador.
    protected int nAccesBany;       //Nombre d'accesos al bany que ha fet.
    protected static final int MAXTEMPSBANY = 6000;
    protected static final int MAXTEMPSFEINA = 2000;

    public Treballador(String nom, Semaphore pG, Semaphore p) {
        this.nomTreb = nom;
        permGenere = pG;
        nAccesBany = 0;
        porta = p;
    }

    @Override
    public String toString() {
        return nomTreb;
    }
}
