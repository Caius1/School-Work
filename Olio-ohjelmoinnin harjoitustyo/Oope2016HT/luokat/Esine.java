package luokat;

/**Esinettä mallintava luokka.
 * <p>
 * Esine on sokkelon passiivinen sisältöosa, joka sijaitsee jollain käytävällä
 * ja sisältää energiaa. Mönkijä voi kerätä esineen varastoonsa, mutta robotit
 * eivät vuorovaikuta esineiden kanssa lainkaan.
 * 
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class Esine extends Sisalto {

    /** Esineen oletusrakentaja. */
    public Esine(){}
    /**Esineen kuormitettu rakentaja.
     *
     * @param riviind Esineen rivi-indeksi.
     * @param saraind Esineen sarakeindeksi.
     * @param energ Parametriarvo energia-attribuutille.
     */
    public Esine(int riviind, int saraind, int energ) {
        super(riviind, saraind, energ);
    }

    /** {@inheritDoc}
     * 
     * @return Esineen tiedot eli sijainti ja energia.
     */
    @Override
    public String toString() {
        String esineTietue = ("Esine    " +EROTIN+ super.toString() +RIVIVAIHTO);
        return esineTietue;
    }

    /**{@inheritDoc}
     * 
     * @param o {@inheritDoc}
     * @return {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**{@inheritDoc}
     * 
     * @param o {@inheritDoc}
     * @return {@inheritDoc} */
    @Override
    public int compareTo(Sisalto o) {
        return super.compareTo(o);
    }
}