import toiminta.Kayttoliittyma;

/**
 * Ajoluokka olio-ohjelmoinnin perusteet -kurssin harjoitustyölle.
 * <p>
 * Tässä harjoitustyössä tehdään sokkelo, jossa on sisältönä seinien ja käytävien
 * lisäksi käyttäjän ohjailema mönkijä(1), robotti(min 1) ja esineitä(min 1).
 * <p>
 * Harjoitustyössä käytetään kurssin opettajan antamia apuluokkia ja rajapintoja
 * pakkauksissa "apulaiset" ja "fi.uta.csjola.oope.lista".
 * 
 * @author Kai Pirttiniemi (pirttiniemi.kai.a@student.uta.fi)
 * @version 1.2, 19.5.2016.
 */
public class Oope2016HT {

    /** Käynnistetään peli Kayttoliittyma-tyyppisen attribuutin kautta. */
    private static final Kayttoliittyma KAYTTOLIITTYMA = new Kayttoliittyma();
    
    /** Käynnistetään peli tervehtimällä käyttäjää tulostellaa, minkä jälkeen
     * kutsutaan käyttöliittymä-attribuutin metodia "lueKomento()", jossa
     * sijaitsee pelin pääsilmukka.
     * <p>
     * @param args Komentoriviparametrit, joita ei tässä ohjelmassa käytetä
     */
    public static void main(String[] args) {
        
        // Tervehditään käyttäjää
        System.out.println("***********");
        System.out.println("* SOKKELO *");
        System.out.println("***********");
        
        //Siirrytään attribuutin kautta pelin pääsilmukan sisältävään metodiin.
        KAYTTOLIITTYMA.lueKomento();
    }
}