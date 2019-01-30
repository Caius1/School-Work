package luokat;
import apulaiset.*;

/**Sokkelossa liikuskelevaa robottia mallintava luokka. Robotti on mönkijän
 * vihollinen ja nämä kaksi oliota taistelevat keskenään, jos kohtaavat toisensa.
 * <p>
 * Robotti on autonominen olio, jonka liikkeisiin käyttäjä ei voi pelin sisällä
 * vaikuttaa. Robottia liikuttaa Automaatti-apuluokka. Automaatissa robotille
 * arvotaan seuraava liikesuunta pseudosatunnaisluvun avulla. Pseudosatunnaisluku
 * arvotaan tesktitiedostosta ladattavalla siemenluvulla, joka edellä mainittua
 * kautta vaikuttaa robottien liikkeisiin.
 * <p>
 * Robotilla on energia-attribuutti, joka säilyy samana koko pelin ajan. Robotti
 * toteuttaa Suunnallinen-rajapinnan pakkauksesta "apulaiset", joten robotti
 * muistaa liikesuuntansa ja osaa myös kertoa sen.
 * 
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class Robotti extends Sisalto implements Suunnallinen {

    /** Attribuutti robotin viimeisimmälle liikesuunnalle. */
    private char suunta;

    /** Robotin oletusrakentaja. */
    public Robotti(){}
    /**Robotin kuormitettu rakentaja.
     *
     * @param rivind Robotin rivi-indeksi.
     * @param sarind Robotin sarakeindeksi.
     * @param energia Parametriarvo energia-attribuutille.
     * @param ilmansuunta Parametriarvo suunnalle, johon mänkijä on tällä
     * hetkellä menossa.
     */
    public Robotti(int rivind, int sarind, int energia, char ilmansuunta) {
        super(rivind, sarind, energia);
        suunta(ilmansuunta);
    }

    /** Robotin suunnan palauttava lukumetodi.
     *
     * @return Vakioitu, yksi neljästä pääilmansuuntaa kuvastavasta merkistä.
     */
    @Override
    public char suunta() {
       return suunta;
    }
    
    /** Robotin suunnan asetusmetodi.
     *
     * @param ilmansuunta Uusi suunta, joka on jokin neljästä pääilmansuunnasta.
     * @throws IllegalArgumentException Jos parametri ei ollut jokin
     * Suunnallinen-rajapinnassa määritellyistä pääilmansuunnan symboleista.
     */
    @Override
    public void suunta(char ilmansuunta) throws IllegalArgumentException {
       if (ilmansuunta == POHJOINEN || ilmansuunta == ITA || ilmansuunta == ETELA || 
        ilmansuunta == LANSI)
           suunta = ilmansuunta;
       else
           throw new IllegalArgumentException("Virheellinen ilmansuunta!");
    }

    /** Metodi poistaa robotin edellisestä sijainnistaan. Edellinen sijainti
     * päätellään robotin viimeisimmän liikesuunnan mukaan.
     * 
     * @param osat Taulukko, josta robotin edellinen sijainti haetaan.
     */
    public void poistaRoboKaytavalta(Osa[][] osat) {

        Kaytava edelPaikka = null; //Apumuuttujan edelliselle sijainnille
        boolean poistettu = false; //Lippumuuttujan, onko poisto tehty
        int i = 0; //Paikan laskuri

        //Haetaan viite robotin edelliseen sijaintiin
        switch (suunta()) {
            case POHJOINEN:
                edelPaikka = (Kaytava)osat[rivi()+1][sarake()];
                break;
            case ITA:
                edelPaikka = (Kaytava)osat[rivi()][sarake()-1];
                break;
            case ETELA:
                edelPaikka = (Kaytava)osat[rivi()-1][sarake()];
                break;
            case LANSI:
                edelPaikka = (Kaytava)osat[rivi()][sarake()+1];
                break;
            //Kaiken varalta virheilmoitus
            default: System.out.println("Virhe poistaRoboKaytavalta"); break;
        }

        //Haetaan poistettavaa roboa edellisen paikan listalta kunnes löytyy
        while(!poistettu) {
            Object paikassa = edelPaikka.alkio(i);
            //Poistetaan paikan robo löytyy vastaava tunnus
            if (this.equals(paikassa)) {
                edelPaikka.poista(i);
                poistettu = true;
            }
            i++;
        }
    }

    /**{@inheritDoc}
     * 
     * @return Robotin tietue, sisältää sijainnin, energian ja suunnan. */
    @Override
    public String toString() {
        String robottiTietue = ("Robotti  " +EROTIN+ super.toString() +suunta+ "   " +EROTIN +RIVIVAIHTO);
        return (robottiTietue);
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