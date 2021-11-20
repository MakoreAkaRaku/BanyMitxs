package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Home extends Treballador {
    private static int numHomesBany = 0;
    private static Semaphore portaHome = new Semaphore(1);
    private static Semaphore accHomes = new Semaphore(3);
    private static boolean canEnter = false;

    public Home(String nomH, Semaphore pGenere) {
        super(nomH, pGenere);
    }

    @Override
    public void run() {
        System.out.println(this + " arriba al despatx.");
        Random r = new Random();
        while (nAccesBany < 2) {
            System.out.println(this + " treballa");
            try {
                sleep(r.nextLong(0,10000));
                accHomes.acquire();             //Contador de homes al bany (màxim 3)
                portaHome.acquire();            //mutex de processos homes(només un home pot entrar).
                if (numHomesBany == 0) {
                    permGenere.acquire();       //Mutex de gènere (només un gènere pot entrar al bany).
                }
                numHomesBany++;
                nAccesBany++;
                System.out.println(this + ": he accedit al bany, " + nAccesBany + "/2 fets. Homes al bany:" + numHomesBany);
                portaHome.release();

                //Temps que fa les seves necesitats l'home al bany.
                sleep(r.nextLong(0,MAXTEMPSBANY));

                //L'home demana permís per sortir per tornar al seu lloc de feina.
                portaHome.acquire();
                numHomesBany--;
                System.out.println(this + ": he sortit del bany, queden " + numHomesBany + " homes restants.");
                if (numHomesBany == 0){
                    System.out.println("***EL BANY ESTA BUIT***");
                    permGenere.release();
                }
                portaHome.release();
                accHomes.release();             //L'home surt del bany i dona pas a que un altra home pugui entrar.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(this + " torna a casa");
    }

}
