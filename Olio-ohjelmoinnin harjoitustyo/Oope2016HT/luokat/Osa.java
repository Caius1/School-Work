package luokat;
import apulaiset.Paikallinen;

/**
 * Luokkahierarkian juuriluokka, josta periytyvät sokkelossa käytettävät Rakenne-
 * ja Sisältö-osat, eli Rakenne- ja Sisalto-luokat.
 * <p>
 * Kuvaa sokkelon kaikille osille/olioille yhteisiä piirteitä ja toimintoja.
 * <p>
 * Toteuttaa rajapinnan Paikallinen, koska kaikki sokkelon osat ja oliot
 * tietävät paikkansa.
 * 
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public abstract class Osa implements Paikallinen {

    /** Osan rivi-indeksi. */
    private int rivind;
    /** Osan sarakeindeksi. */
    private int sarind;

    /** Jokaiselle oliolle määritettävä tunnus, jolla olio voidaan yksilöidä.
     * Tunnukselle ei ole asetusaksessoria, koska sitä ei voi eikä tarvitse
     * asettaa muualla kuin rakentajassa. */
    private int uniikkiId;
    /** Laskuri, joka pitää kirjaa käytetyistä tunnuksista.
     * Tunnuksia annetaan kokonaislukuina 0, 1, 2, 3 ja niin edelleen kasvavasti. */
    private static int idLaskuri = 0;

    /** Vakioitu tulostettava merkki mönkijälle. */
    private final char MONKIJA = 'M';
    /** Vakioitu tulostettava merkki robotille. */
    private final char ROBOTTI = 'R';
    /** Vakioitu tulostettava merkki esineelle. */
    private final char ESINE = 'E';
    /** Vakioitu tulostettava merkki seinälle. */
    private final char SEINA = '.';
    /** Vakioitu tulostettava merkki käytävälle. */
    private final char KAYTAVA = ' ';

    /** Erotin tulosteiden muotoiluun. */
    protected final String EROTIN = "|";
    /** Rivinvaihto tulosteiden muotoiluun. */
    protected final String RIVIVAIHTO = "\n";

    /** Vakioitu virheilmoitus. */
    protected final String VIRHE = "Virhe!";

    /** Osan oletusrakentaja. */
    public Osa(){}
    /**Osan kuormitettu rakentaja.
     * <p>
     * Asettaa oliolle sijainnin ideksit sekä tunnuksen.
     * 
     * @param riviind Osan rivi-indeksi.
     * @param saraind Osan sarakeindeksi.
     * @throws IllegalArgumentException Jos asetettava indeksi on negatiivinen.
     */
    public Osa(int riviind, int saraind) throws IllegalArgumentException {
        
        if (riviind >= 0 && saraind >= 0) {
            rivind = riviind;
            sarind = saraind;
        }
        else
            throw new IllegalArgumentException("Virheellinen indeksi!");
        
        uniikkiId = idLaskuri++;
    }
    
    /**Lukuaksessori olion kätketylle tunnukselle.
     * @return Olion int-tyyppinen tunnus. */
    protected int uniikkiId() {
        return uniikkiId;
    }
    
    /** Paikan rivi-indeksin lukumetodi.
     * @return Paikan rivi-indeksi. */
    @Override
    public int rivi() {
        return rivind;
    }

    /** Paikan rivi-indeksin asetusmetodi.
     *
     * @param ind Asetettava rivi-indeksi.
     * @throws IllegalArgumentException Jos indeksi on negatiivinen.
     */
    @Override
    public void rivi(int ind) throws IllegalArgumentException {
        if (ind > 0)
            rivind = ind;
        else
            throw new IllegalArgumentException("Virheellinen indeksi!");
    }

    /** Paikan sarakeindeksin lukumetodi.
     * @return Paikan sarakeindeksi. */
    @Override
    public int sarake() {
        return sarind;
    }

    /** Paikan sarakeindeksin asettava metodi.
     *
     * @param ind sarakeindeksi.
     * @throws IllegalArgumentException jos indeksi on negatiivinen.
     */
    @Override
    public void sarake(int ind) throws IllegalArgumentException {
        if (ind > 0)
            sarind = ind;
        else
            throw new IllegalArgumentException("Virheellinen indeksi!");
    }

    /** Tutkii käytäväpaikalla olevan olion (seinä tai käytävä) ja palauttaa
     * käyttäjälle tulostettavan merkin.
     * 
     * @param osa Sokkelon osa, jota visualisoidaan palautettavalla merkillä.
     * @return Char-tyypin merkki, joka kuvastaa joko seinää tai käytävän sisältöä.
     */
    public char kerroMerkki(Object osa) {

        //Alustetaan merkki huutomerkillä, jotta huomataan virheet kartan tulostuksessa
        char merkki = '!';

        //Jos on seinää
        if (osa instanceof Seina)
            merkki = SEINA;
        //Jos on käytävää
        else if (osa instanceof Kaytava) {
            //Apumuuttuja, jolla saadaan kiinni käytäväosan tiedoista
            Kaytava kaytava = (Kaytava)osa;

            //Lippumuuttujat, jos käytävällä on mönkijä tai robotti.
            boolean monTavattu = false;
            boolean robTavattu = false;

            //Jos käytäväpaikka on tyhjä, tulostetaan tyhjä käytävä
            if (kaytava.onkoTyhja())
                merkki = KAYTAVA;
            //Käytävällä on sisältöä
            else {
                //Käydään käytävän lista läpi
                int koko = kaytava.koko();
                Object sisOsa; //Apumuuttuja sisältöosalle
                for (int i = 0; i < koko; i++) {
                    sisOsa = kaytava.alkio(i);
                    if (sisOsa instanceof Monkija)
                        monTavattu = true;
                    else if (sisOsa instanceof Robotti)
                        robTavattu = true;
                }

                //Lippumuuttujat kertovat mitä käytävällä on
                if (monTavattu && robTavattu) //Ilmoitetaan virhetilanteesta
                    System.out.println("Mönkijä ja robotti samalla käytäväpaikalla!");
                else if (monTavattu)
                   merkki = MONKIJA;
                else if (robTavattu)
                    merkki = ROBOTTI;
                //Jos käytävällä ei ollut robottia eikä mönkijää, siinä on esine
                else
                    merkki = ESINE;
            }
        }
        else //Virheilmoitus kaiken varalta
            System.out.println("Virhe Osa.kerroMerkki()!");

        //Palautetaan tulostettava merkki
        return merkki;
    }

    /**Lisää tarvittavan määrän välilyöntejä täytteeksi indeksilukujen ja
     * energian tietokenttään.
     * <p>
     * Muotoilumetodi on ainoastaan indekseille ja energialle, koska muissa
     * tulostustilanteissa tiedon pituus ja tarvittava kentän "täyte" on tiedossa.
     * Esimerkiksi suunnan tulostuksessa on tehokkaampaa olla käyttämättä
     * erillistä metodia/operaatiota.
     * 
     * @param tieto Int-tyyppinen luku, arvo voi olla indeksi tai energia.
     * @return String-muotoinen kenttä tiedolle, jossa on itse tieto sekä kentän
     * muotoilevat välilyönnit ja erotin.
     */
    protected String muotoileKentta(int tieto) {
        String kentta; //Palautettava kenttä
        //Lisätään kenttään välilyöntejä niin kauan kuin kentän pituus on alle 4
        kentta = Integer.toString(tieto);
        while (kentta.length() < 4) {
            kentta += " ";
        }
        //Palautetaan kenttä erottimen kanssa
        return (kentta + EROTIN);
    }
    
    /** Kertoo, onko paikkaan sallittua asettaa sisältöä (mönkijä, robotti tai esine).
     * @return True, jos paikka on käytettävissä. */   
    @Override
    public abstract boolean sallittu();
    
    /** Palauttaa merkkijonon, jossa on tulostuskelpoiset tietokentät kutsuvan
     * olion tiedoille.
     * 
     * @return Olion rivi- ja sarakeindeksin tietokentät. */
    @Override
    public String toString() {
        return (muotoileKentta(rivind) + muotoileKentta(sarind));
    }
    
    /** Vertailee kutsuvan olion ja parametrina saadun olion uniikkiId-attribuuttia.
     * 
     * @param o Osa/objekti jonka ID:tä halutaan verrata.
     * @return True, jos uniikkiId on molemmilla osapuolilla sama.
     */
    @Override
    public boolean equals(Object o) {
        Osa verrattava = (Osa)o;
        return (uniikkiId == verrattava.uniikkiId);
    }
}