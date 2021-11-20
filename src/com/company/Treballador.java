package com.company;

import java.util.concurrent.Semaphore;
public abstract class Treballador extends Thread {

    protected Semaphore permGenere; //Semafor que dona pas a un proces Dona o Home segons la race condition.
    protected String nomTreb;       //Nom del treballador.
    protected int nAccesBany;       //Nombre d'accesos al bany que ha fet.
    protected static final int MAXTEMPSBANY = 6000;

    public Treballador(String nom, Semaphore pG) {
        this.nomTreb = nom;
        permGenere = pG;
        nAccesBany = 0;
    }

    @Override
    public String toString() {
        return nomTreb;
    }

    @Override
    public abstract void run();
}
