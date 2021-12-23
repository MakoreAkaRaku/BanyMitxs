package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class Dona extends Treballador {
    private static int numDonesBany = 0;
    private static Semaphore accDones = new Semaphore(3);
    private static Semaphore mutexD = new Semaphore(1);

    public Dona(String nomD, Semaphore pGenere, Semaphore portaBany) {
        super(nomD, pGenere, portaBany);
    }

    @Override
    public void run() {
        System.out.println(this + " arriba al despatx.");
        Random r = new Random();
        while (nAccesBany < 2) {
            System.out.println(this + " treballa");
            try {
                sleep(r.nextLong(0, MAXTEMPSFEINA));
                porta.acquire();
                if (numDonesBany == 0) {
                    permGenere.acquire();
                }
                accDones.acquire();

                //Seccio Critica de Dones.
                mutexD.acquire();
                numDonesBany++;
                mutexD.release();

                nAccesBany++;
                System.out.println("\t" + this + ": he accedit al bany, " + nAccesBany + "/2 fets. Dones al bany:" + numDonesBany);
                porta.release();

                //Temps que fa les seves necessitats la dona al bany.

                r.nextLong(0, MAXTEMPSBANY);

                mutexD.acquire();
                numDonesBany--;
                System.out.println("\t" + this + ": he sortit del bany, queden " + numDonesBany + " dones restants.");
                accDones.release();
                if (numDonesBany == 0) {
                    System.out.println("***EL BANY ESTA BUIT***");
                    permGenere.release();
                }
                mutexD.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this + " torna a casa");
    }
}
