package toiminta;
import apulaiset.In;
import luokat.Osa;

/** Käyttöliittymä-luokka, jossa tapahtuu ihmisen ja pelin vuorovaikutus.
 * <p>
 * Luokka kuuluu toiminta-pakkaukseen, joska sisältää luokat, joiden toiminta
 * näkyy käyttäjälle.
 * <p>
 * Import-lause tuo käytettäväksi apulaiset-pakkauksesta In-metodin, jolla
 * luetaan käyttäjän komento.
 * 
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class Kayttoliittyma {
    
    /* 
     * Vakioidut komennot, joita käyttäjä voi antaa.
     */
    /** Vakio komento pelin lopetukseen. */
    private final String LOPETA = "lopeta";
    /** Vakio komento kartan tulostukseen. */
    private final String KARTTA = "kartta";
    /** Vakio komento pelin tallennukseen. */
    private final String TALLENNA = "tallenna";
    /** Vakio komento pelin lataamiseen. */
    private final String LATAA = "lataa";
    /** Vakio komento mönkijän inventointiin. */
    private final String INVENTOI = "inventoi";
    /** Vakio komento vuoron ohittamiseen. */
    private final String ODOTA = "odota";
    /** Vakio komento esineiden muuntamiseen energiaksi. */
    private final String MUUNNA = "muunna";
    /** Vakio komento mönkijän liikuttamiseen. */
    private final String LIIKU = "liiku";
    /** Vakio komento ympäristön katseluun. */
    private final String KATSO = "katso";
    
    /* Vakiot liikkeen ja katsomisen suunnalle */
    private final char POHJOINEN = 'p';
    private final char ITA = 'i';
    private final char ETELA = 'e';
    private final char LANSI = 'l';
    
    /** Vakio virheilmoitukselle. */
    private final String VIRHE = "Virhe!";

    /** Logiikka-attribuutin kautta kutsutaan käyttäjänkomennon käsittelevää
     * metodia Logiikka-luokassa. */
    private final Logiikka LOGIIKKA = new Logiikka();
    
    /**
     * Luetaan käyttäjältä uusia komentoja kunnes sokkelo on läpäisty,
     * joko häviämällä tai voittamalla.
     */
    public void lueKomento() {
        
        /* 
         * Ohjelman käynnistyessä 1. kerran, suoritetaan lataa-metodi samaan
         * tapaan kuin jos käyttäjä antaisi "lataa"-komennon.
         */
        LOGIIKKA.lataa();
        
        /*
         * Do-while -silmukka, joka toimii tämän pelin pääsilmukkana, jossa
         * koko touhu pyörii, kunnes jokin seuraavista ehdoista täyttyy:
         * 1. Käyttäjä antaa komennon "lopeta"
         * 2. Robotti voittaa mönkijän taistelussa
         * 3. Kaikki esineet on kerätty
         */
        do {
            //Ohjeistetaan käyttäjää
            System.out.println("Kirjoita komento:");
            //Luetaan käyttäjän komento
            String komento = In.readString();
            //Tulkitaan annettu komento
            komennonKasittely(komento);
        
        }
        //Kunnes jokin em. ehdoista täyttyy
        while(!LOGIIKKA.onkoSokkeloLapaisty());
        
        //Kerrotaan käyttäjälle ohjelman lopetuksesta
        System.out.println("Ohjelma lopetettu.");
    }
    
    /**
     * Käsitellään käyttäjän antama komento ja suoritetaan sitä vastaava toiminto.
     *
     * @param komento Käyttäjältä saatu komento, erotetaan mahdolliset suunta-
     * tai lukumääräparametrit split-operaatiolla.
     */
    public void komennonKasittely(String komento) {

        //Palastellaan komento välilyönnistä taulukon kentiksi
        String[] syote = komento.split(" ");

        //Trimmataan syötekentistä tyhjät merkit pois, näin estetään yksi virhetilanne
        syote[0] = syote[0].trim();
        //Ohitetaan toisen kentän trimmaus, jos toista kenttää ei ole
        try {
            syote[1] = syote[1].trim();
        }
        catch(ArrayIndexOutOfBoundsException e) {}

        /* Päätellään komennonmukainen jatkotoiminta, joka komennon kohdalla
         * tarkistetaan lisäksi, että komentoja/parametreja on toiminnan
         * vaatima määrä. Esimerkiksi kartan tulostuksessa saa olla vain yksi
         * komento ilman muita parametreja. */
        String suunta = ""; //Apumuuttuja liike- ja katsomissuunnalle
        switch (syote[0]) {
            case LOPETA:
                if (syote.length == 1) {
                    LOGIIKKA.lopeta();
                }
                else
                    System.out.println(VIRHE);
                break;
            case KARTTA:
                if (syote.length == 1)
                    LOGIIKKA.tulostaKartta();
                else
                    System.out.println(VIRHE);
                break;
            case TALLENNA:
                if (syote.length == 1)
                    LOGIIKKA.tallenna();
                else
                    System.out.println(VIRHE);
                break;
            case LATAA:
                if (syote.length == 1)
                    LOGIIKKA.lataa();
                else
                    System.out.println(VIRHE);
                break;
            case INVENTOI:
                if (syote.length == 1)
                    LOGIIKKA.monkija().inventoi();
                else
                    System.out.println(VIRHE);
                break;
            case ODOTA:
                if (syote.length == 1)
                    LOGIIKKA.odota();
                else
                    System.out.println(VIRHE);
                break;
            case MUUNNA:
                //Tarkistetaan, että on annettu määrä ja vain yksi parametri
                if (syote.length == 2)
                    LOGIIKKA.monkija().muunna(syote[1]);
                else
                    System.out.println(VIRHE);
                break;
            case LIIKU:
                boolean voiLiikkua = false; //Onko syöte validi
                /* Tarkistetaan, että on annettu 1 merkin mittainen suunta ja,
                 * että parametreja annettiin vain 1 */
                char suunt = ' ';
                try {
                    suunta = syote[1].trim();
                    suunt = suunta.charAt(0);
                    if (suunta.length() == 1 && syote.length == 2)
                        //Tarkistetaan että suunta on jokin vakioiduista merkeistä
                        if (suunt == POHJOINEN || suunt == ITA || suunt == ETELA || suunt == LANSI)
                            voiLiikkua = true;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    voiLiikkua = false;
                }
                
                if (voiLiikkua) //Jos käyttäjän syötteitä oli oikea määrä
                    LOGIIKKA.liiku(suunt);
                else
                    System.out.println(VIRHE);
                
                break;
            case KATSO:
                try { //Tarkistetaan, että on annettu 1 merkin mittainen suunta
                    suunta = syote[1].trim();
                    if (suunta.length() == 1) {
                        Osa[][] osat = LOGIIKKA.osat();
                        LOGIIKKA.monkija().katso(suunta, osat);
                    }
                    else
                        System.out.println(VIRHE);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(VIRHE);
                }
                break;
            default:
                System.out.println(VIRHE);
                break;
        }
    }
}