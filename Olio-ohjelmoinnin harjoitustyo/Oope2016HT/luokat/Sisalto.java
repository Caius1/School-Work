package luokat;

/**
 * Sokkelon sisältöosien (mönkijä, robotti ja esine) yhteisiä piirteitä ja
 * toimintoja kuvaava luokka.
 * <p>
 * Perii Osa-luokan, ja on yliluokka Monkija-, Robotti- ja Esine-luokille.
 * <p>
 * Toteuttaa Comparable-rajapinnan, joka sidotaan Sisalto-tyyppiseksi, koska
 * kaikki Comparablella vertailtavat oliot ovat Sisalto-luokan jälkeläisiä.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public abstract class Sisalto extends Osa implements Comparable<Sisalto> {

    /** Olion sisältämä energiamäärä. */
    private int energia = 0;

    /** Sisältöosan oletusrakentaja. */
    public Sisalto(){}
    /** Sisältöosan kuormitettu rakentaja.
     *
     * @param rivind Sisältöosan rivi-indeksi.
     * @param sarind Sisältöosan sarakeindeksi.
     * @param energ Parametriarvo energia-attribuutille. */
    public Sisalto(int rivind, int sarind, int energ) {
        super(rivind, sarind);
        energia = energ;
    }

    /** Lukumetodi olion energialle.
     * @return Olion enegia. */
    public int energia() {
        return energia;
    }

    /** Asetusmetodi olion energialle.
     * @param energ Oliolle asetettava energia.
     * @throws IllegalArgumentException Jos parametri on negatiivinen.
     */
    public void energia(int energ) {
        energia = energ;
    }

    /** {@inheritDoc}
     * <p>
     * Sisältö-osien kohdalla metodia ei käytetä missään vaiheessa.
     *
     * @return True, koska sisältöosat eivät määritä onko paikka sallittu vai ei. */   
    @Override
    public boolean sallittu() {
        return true;
    }

    /** {@inheritDoc}
     * @return Olion energian tietokenttä. */
    @Override
    public String toString() {
        return (super.toString() + super.muotoileKentta(energia));
    }
    
    /**{@inheritDoc}
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
    
    /** Toteutetaan Comparable-rajapinnan compareTo-metodi.
     * <p>
     * Rajapinnan geneerinen tyyppi on kiinnitetty Sisalto-tyyppiin, koska kaikki
     * compareTo-metodin toteuttavat luokat periytyvät Sisalto-luokasta.
     * 
     * @param o Osa (verrokki), johon kutsun/viestin saava olio vertaa itseään.
     * @return -1, jos vertaava on pienempi kuin verrokki.<br>
     * 0, jos yhtä suuret.<br>
     * 1, jos vertaava on verrokkia suurempi.
     */
    @Override
    public int compareTo(Sisalto o) {
        
        if (energia() < o.energia())
            return -1;
        else if (energia() == o.energia())
            return 0;
        else if (energia() > o.energia())
            return 1;
        else { //Varaudutaan virheisiin ilmoituksella
            System.out.println("Virhe Monkija.compareTo!");
            return 2;
        }
    }
}