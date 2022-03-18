package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Marc Roman Colom - 43235793W
 * @author Andreas Manuel Korn - X-4890193-W
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
        System.out.println("\t" + this + " arriba al despatx.");
        Random r = new Random();
        while (nAccesBany < 2) {
            System.out.println("\t" + this + " treballa");
            try {
                sleep(r.nextInt(MAXTEMPSFEINA));
                porta.acquire();                    //Mutex de la porta del bany (tant homes com dones).
                if (numDonesBany == 0) {
                    permGenere.acquire();           //Mutex de gènere (només un gènere pot estar al bany).
                }
                accDones.acquire();                 //Contador de dones al bany (màxim 3)

                //Seccio Critica de Dones.
                mutexD.acquire();
                numDonesBany++;
                nAccesBany++;
                System.out.println("\t" + this + ": he accedit al bany, " + nAccesBany + "/2 fets. Dones al bany:" + numDonesBany);
                mutexD.release();

                porta.release();                            //Amolla la porta per a que un altre pugui entrar.

                //Temps que fa les seves necessitats la dona al bany.
                sleep(r.nextInt(MAXTEMPSBANY));

                mutexD.acquire();
                numDonesBany--;
                System.out.println("\t" + this + ": he sortit del bany, queden " + numDonesBany + " dones restants.");
                accDones.release();                 //La dona surt del bany i dona pas a que un altre pugui entrar.
                if (numDonesBany == 0) {            //En cas de que sigui la darrera en sortir, allibera el permís de genere.
                    System.out.println("***EL BANY ESTA BUIT***");
                    permGenere.release();
                }
                mutexD.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\t" + this + " torna a casa");
    }
}