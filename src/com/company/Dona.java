package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Dona extends Treballador {
    private static int numDonesBany = 0;
    private static Semaphore portaDona = new Semaphore(1);
    private static Semaphore accDones = new Semaphore(3);

    public Dona(String nomD, Semaphore pGenere) {
        super(nomD, pGenere);
    }

    @Override
    public void run() {
        System.out.println(this + " arriba al despatx.");
        Random r = new Random();
        while (nAccesBany < 2) {
            System.out.println(this + " treballa");
            try {
                sleep(r.nextLong(0, 10000));
                accDones.acquire();                         //
                portaDona.acquire();
                if (numDonesBany == 0) {
                    permGenere.acquire();
                }
                numDonesBany++;
                nAccesBany++;
                System.out.println("\t" + this + ": he accedit al bany, " + nAccesBany + "/2 fets. Dones al bany:" + numDonesBany);
                portaDona.release();

                //Temps que fa les seves necessitats la dona al bany.

                r.nextLong(0, MAXTEMPSBANY);

                portaDona.acquire();
                numDonesBany--;
                System.out.println("\t" + this + ": he sortit del bany, queden " + numDonesBany + " dones restants.");
                if (numDonesBany == 0) {
                    System.out.println("***EL BANY ESTA BUIT***");
                    permGenere.release();
                }
                portaDona.release();
                accDones.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this + " torna a casa");
    }

    //TODO: Tenemos que buscar problema de inanición, ya que no lo encuentro. También mirar de perfeccionar el problema.
}
