package com.company;

import java.util.concurrent.Semaphore;

public class Main {
    private static Semaphore permisGenere;
    private static Semaphore porta;
    private static String[] homes = {"Juan","Esteve","Narcis","Marc","Xesc","Vicent"};
    private static String[] dones = {"Juana","Catalina","Isabel","Rosa","Maria","Pixeri"};

    public static void main(String[] args) {
        permisGenere = new Semaphore(1);
        porta = new Semaphore(1);
        Thread[] treballadors = new Thread[homes.length + dones.length];

        //Creacio de threads d'homes.
        for (int i = 0; i < homes.length; i++) {
            treballadors[i] = new Home(homes[i], permisGenere,porta);
        }

        //Creació de threads de dones.
        for (int i = 0; i < dones.length; i++) {
            treballadors[i + homes.length] = new Dona(dones[i], permisGenere,porta);
        }

        //Executa els processos.
        for (int i = 0; i < treballadors.length; i++) {
            treballadors[i].start();
        }

        //Esperar a que acabin els processos.
        for (int i = 0; i < treballadors.length; i++) {
            try {
                treballadors[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
