package luokat;

/** Kaytava-luokka mallintaa yhtä "ruutua" sokkelon käytävistä.
 * <p>
 * Kaytava-luokka sisältää metodeja, jotka liittyvät käytävällä olevien olioiden
 * käsittelyyn. Tässä tapauksessa käsittely tarkoittaa olioiden lisäämistä
 * käytävälle tai niiden poistamista käytävältä.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class Kaytava extends Rakenne {

    /** Tiedon käytäväpaikalla olevista olioista säilyttävä lista. */
    private OmaLista kaytavaLista;
    
    /** Käytävän oletusrakentaja. */
    public Kaytava(){}
    /** Käytävän kuormitettu rakentaja.
     * 
     * @param rivind Käytäväosan rivi-indeksi.
     * @param sarind Käytäväosan sarakeindeksi.
     */
    public Kaytava(int rivind, int sarind) {
        super(rivind, sarind);
        kaytavaLista = new OmaLista();
    }
    
    /** {@inheritDoc}
     *
     * @return True, koska käytävälle voi sijoittaa sisältöä.
     */   
    @Override
    public boolean sallittu() {
        return true;
    }
    
    /** Lisää käytävälle asian. Lisäys tapahtuu kutsumalla OmaLista-luokan
     * lisaaListalle-metodia ja välittämällä lisättävän asian parametrina.
     * 
     * @param asia Lisättävä asia kuten esine, mönkijä tai robotti.
     */
    public void lisaaKaytavalle(Object asia) {
        kaytavaLista.lisaaListalle(asia);
    }
    
    /**Poistaa mönkijän käytävältä.
     * Käydään käytävän listaa läpi kunnes mönkijän on poistettu.
     * 
     * @return Viite käytävältä poistettuun mönkijään.
     */
    public Object poistaMonkija() {
        
        Object poistettu; //Apumuuttuja
        boolean monPoistettu = false; //Lippumuuttuja
        int i = 0;
        
        //Käydään käytävän listaa läpi alusta alkaen...
        do {
            poistettu = kaytavaLista.alkio(i);
            //Tarkistetaan poistetun luokka
            if (poistettu instanceof Monkija) {
                //Poistetaan löydetty mönkijä
                poistettu = kaytavaLista.poista(i);
                monPoistettu = true;
            }
            else i++;
        }
        //...kunnes mönkijä on poistettu
        while (!monPoistettu);
        //Palautetaan viite poistettuun mönkijään
        return poistettu;
    }
    
    /** Poistetaan parametrina saatu robotti operaation kutsun saavalta listalta.
     * <p>
     * Poisto tapahtuu vertaamalla poistettavan robotin tunnuslukua listan muiden
     * olioiden tunnuslukuihin. Kun täsmäävä ID löytyy, poistetaan kyseisen paikan
     * robotti listalta.
     * 
     * @param poistettava Robotti, joka listalta poistetaan.
     */
    public void poistaRobotti(Robotti poistettava) {

        int roboId = poistettava.uniikkiId(); //Poistettavan robotin tunnus
        int paikassaId; //Paikassa olevan robotin tunnus
        int i = 0; //Laskuri listan paikalle

        //Käydään listaa läpi...
        do {
            //Haetaan listan paikassa olevan robon tunnus
            paikassaId = ((Osa)kaytavaLista.alkio(i)).uniikkiId();
            //Jos paikasta löytyy poistettavan robon tunnus, poistetaan se paikasta
            if (roboId == paikassaId)
                kaytavaLista.poista(i);
            else //Muuten tarkastellaan seuraavaa paikkaa
                i++;
        }
        //..kunnes yhtenevät ID:t löytyivät eli robotti on poistettu
        while(roboId != paikassaId);
    }
    
    /* **** Apumetodit käytävän listan käsittelyyn ****************************/
    
    /**Palauttaa tietyssä paikassa sijaitsevan listan alkion.
     * 
     * @param paikka Listasta haettavan alkion indeksiluku.
     * @return Parametrina annetussa listan paikassa sijaitseva alkio.
     */
    public Object alkio(int paikka) {
        return kaytavaLista.alkio(paikka);
    }
    
    /**Poistaa ja palauttaa listan tietyssä paikassa sijaitsevan alkion.
     * 
     * @param paikka Listasta poistettavan alkion indeksiluku.
     * @return Parametrina annetusta listan paikasta poistettu alkio.
     */
    public Object poista(int paikka) {
        return kaytavaLista.poista(paikka);
    }
    
    /**Palauttaa listan koon int-tyyppisenä lukuna.
     * 
     * @return Int-luku, joka kertoo, montako alkiota listalla on.
     */
    public int koko() {
        return kaytavaLista.koko();
    }
    
    /**Kertoo, onko listalla alkioita eli onko lista tyhjä.
     * 
     * @return True, jos listalla ei ole alkioita.
     */
    public boolean onkoTyhja() {
        return kaytavaLista.onkoTyhja();
    }
    
    /* **** Apumetodit päättyy ***/

    /** {@inheritDoc}
     * 
     * @return Käytävän tiedot sekä käytävän sisältö yhtenä String-merkkijonona.
     */
    @Override
    public String toString() {
        
        String kaytavaTietue = ("Kaytava  " +EROTIN+ super.toString() +RIVIVAIHTO);
        String kaytavanSisalto = "";
        
        for (int i = 0; i < kaytavaLista.koko(); i++) {
            kaytavanSisalto += (kaytavaLista.alkio(i).toString());
        }
        
        return (kaytavaTietue + kaytavanSisalto);
    }
}