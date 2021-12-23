package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class Home extends Treballador {
    private static int numHomesBany = 0;
    private static Semaphore accHomes = new Semaphore(3);
    private static Semaphore mutexH = new Semaphore(1);

    public Home(String nomH, Semaphore pGenere, Semaphore portaBany) {
        super(nomH, pGenere, portaBany);
    }

    @Override
    public void run() {
        System.out.println(this + " arriba al despatx.");
        Random r = new Random();
        while (nAccesBany < 2) {
            System.out.println(this + " treballa");
            try {
                sleep(r.nextLong(0, MAXTEMPSFEINA));
                porta.acquire();                //Mutex de la porta del bany (tant homes com dones).
                if (numHomesBany == 0) {
                    permGenere.acquire();       //Mutex de gènere (només un gènere pot entrar al bany).
                }
                accHomes.acquire();             //Contador de homes al bany (màxim 3)

                //Secció Crítica: Entra al bany.
                mutexH.acquire();
                numHomesBany++;
                nAccesBany++;
                System.out.println(this + ": he accedit al bany, " + nAccesBany + "/2 fets. Homes al bany:" + numHomesBany);
                mutexH.release();

                porta.release();

                //Temps que fa les seves necessitats al bany.
                sleep(r.nextLong(0, MAXTEMPSBANY));

                //Secció Crítica: Surt del bany.
                mutexH.acquire();
                numHomesBany--;
                System.out.println(this + ": he sortit del bany, queden " + numHomesBany + " homes restants.");
                accHomes.release();             //L'home surt del bany i dona pas a que un altre pugui entrar.
                if (numHomesBany == 0) {        //Si es el darrer, allibera el permis de genere del bany.
                    System.out.println("***EL BANY ESTA BUIT***");
                    permGenere.release();
                }
                mutexH.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this + " torna a casa");
    }
}
