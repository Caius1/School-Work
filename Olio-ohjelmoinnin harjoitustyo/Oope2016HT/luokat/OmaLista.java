package luokat;
import fi.uta.csjola.oope.lista.*;

/** Oope2016HT-harjoitustyön pääasiallinen tiedon säilöntämuoto.
 * <p>
 * Kuuluu ulokat-pakkaukseen, koska tässä talnteessa mielestäni on luonnollista,
 * tietomuoto on siellä, missä sitä tarvitaan eniten. Näin OmaLista-luokan käyttö
 * helpottuu huomattavasti.
 * <p>
 * Kutsutaan import-lauseella OmaListan yliluokat käyttöön.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class OmaLista extends LinkitettyLista {

    /** Listan alkiot säilyttävät kasvavan suuruusjärjestyksen,
     * jos lisäys tehdään tällä operaatiolla.
     * <p>
     * Lisätään parametri alkio kutsuvan olion listalle.
     *
     * @param alkio Viite olioon, joka halutaan lisätä listalle.
     * @throws IllegalArgumentException jos oliolla ei ole
     * Comparable-rajapinnan toteutusta. Virhettä ei tosin sen kummemmin käsitellä.
     */
    @SuppressWarnings("unchecked") //Estetään kääntäjän varoitus.
    public void lisaaListalle(Object alkio) throws IllegalArgumentException {

        //Lippumuuttuja, false kunnes lisättävä alkio on löytänyt paikkansa
        boolean alkioLisatty = false;
        //Apumuuttujat, joita vertaamalla etsitään alkion paikka listalla
        Object edellinen, paikassa, seuraava;
        //Tarkistetaan, että alkio totetuttaa Comparable-rajpinnan
        boolean compOK = (alkio instanceof Comparable);

        //Lisätään alkio listalle, jos rajapinta toteutuu
        if (compOK){

            //Laskuri verrattavien listan alkioiden paikalle
            int paikka = 0;

            //Jos lista on tyhjä, lisätään alkio listan alkuun
            if (onkoTyhja()) {
                lisaaAlkuun(alkio);
                paikka++;
            }
            //Etsitään alkiolle paikka, jos lista ei ole tyhjä
            else {
                do {
                    //Listalla jo olevat alkiot, joihin lisättävää alkiota verrataan
                    edellinen = alkio(paikka-1);
                    paikassa = alkio(paikka);
                    seuraava = alkio(paikka+1);
                    /* Muutetaan apumuuttujat Comparable-tyyppisiksi, koska alkio()
                     * operaatio palauttaa Object-tyypin viitteen, joka ei toteuta
                     * Comparable-rajapintaa */
                    Comparable edelVert = (Comparable)edellinen;
                    Comparable paikasVert = (Comparable)paikassa;
                    //Ollaan listan alussa ja paikassa suurempi kuin lisättävä
                    if (edellinen == null && paikasVert.compareTo(alkio) > 0) {
                        lisaaAlkuun(alkio);
                        alkioLisatty = true;
                    }
                    //Ollaan päädytty listan loppuun, lisätään alkio viimeiseksi
                    else if (paikassa == null) {
                        lisaaLoppuun(alkio);
                        alkioLisatty = true;
                    }
                    //Ei verrata ekaan listan alkioon, eikä olla listan lopussakaan
                    else if (paikka > 0) {
                        //Samankokoisten viimeiseksi kun seuraava (paikassa) suurempi
                        if (edelVert.compareTo(alkio) == 0 && paikasVert.compareTo(alkio) > 0)
                            alkioLisatty = lisaa(paikka, alkio);
                        //Pienemmän ja suuremman väliin
                        else if (edelVert.compareTo(alkio) < 0 && paikasVert.compareTo(alkio) > 0)
                            alkioLisatty = lisaa(paikka, alkio);
                    }
                    //Katsotaan seuraavaa paikkaa, jos paikkaa ei löytynyt
                    paikka++;
                }
                //Etsitään paikkaa, kunnes alkio on lisätty listalle
                while (!alkioLisatty);
            }
        }
        //Heitetään poikkeus, jos Comparable-rajapinta ei toteudu.
        else
            throw new IllegalArgumentException();
    }
}