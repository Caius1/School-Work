package luokat;

/** Sokkelon rakenneosien (seinä ja käytävä) yhteisiä tietoja kuvaava luokka.
 * <p>
 * Perii Osa-luokan, ja on yliluokka Seina- ja Kaytava-luokille.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public abstract class Rakenne extends Osa {
    
    /** Rakenneosan oletusrakentaja. */
    public Rakenne(){}
    /** Rakenneosan kuormitettu rakentaja.
     * <p>
     * @param riviind Rakenneosan rivi-indeksi.
     * @param saraind Osan sarakeindeksi. */
    public Rakenne(int riviind, int saraind) {
        super(riviind, saraind);
    }
    
    /**{@inheritDoc}
     * @return Palauttaa vain yliluokan tiedot, koska Rakenne-luokka itsessään
     *  ei sisällä tietoa. */
    @Override
    public String toString(){
        return super.toString();
    }
}