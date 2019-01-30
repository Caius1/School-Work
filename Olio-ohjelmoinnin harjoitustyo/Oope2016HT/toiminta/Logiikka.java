package toiminta;

import apulaiset.Automaatti;
import java.io.*;
import luokat.*;

/** Suoritetaan pelin toiminnot, päättelyt yms.
 * <p>
 * Logiikka-luokka sisältää pelin toiminnot, jotka näkyvät pelin käyttäjälle.
 * <p>
 * Import-lauseilla otetaan käyttöön:<br>
 * -Automaatti-luokka, jolla liikutetaan robotteja<br>
 * -java.io -pakkauksesta työkalut "sokkelo.txt" tiedoston käsittelyyn<br>
 * -luokat -pakkauksesta sokkelon osat, sisältö ja rakenne
 * 
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.1, 8.4.2016.
 * <p>8.4.2016: Lisätty tallennukseen ekan rivin muotoilun tarkistus ja
 * liikkumiseen tarkennettu parametrien tarkistusta.
 */
public class Logiikka {

    /** Monkija-tyyppinen olioattribuutti, jonka kautta päästään käsiksi mönkijän
     * operaatioihin.
     */
    private Monkija monkija;
    
    /** Lista, joka säilyttää tiedon sokkelossa vaanivista roboteista. */
    private OmaLista robotit;

    /** Vakiomerkki tulosteiden muotoiluun, käytetään myös tiedostoa luettaessa. */
    private final String EROTIN = "|";

    /* 
     * Vakiot sokkelo-tiedoston olioluokkien tunnistukseen
     */
    /** Vakio mönkijä-oliolle. */
    private final String MON = "Monkija";
    /** Vakio robotti-oliolle. */
    private final String ROB = "Robotti";
    /** Vakio esine-oliolle. */
    private final String ESI = "Esine";
    /** Vakio seinä-oliolle. */
    private final String SEI = "Seina";
    /** Vakio käytävä-oliolle. */
    private final String KAY = "Kaytava";

    
    /** Vakio ilmoitukselle tappiosta taistelussa. */
    private final String TAPPIO = "Tappio!";
    /** Vakio ilmoitukselle mönkijän voitosta taistelussa. */
    private final String VOITTO = "Voitto!";

    /** Vakio tiedoston nimelle, jota käytetään pelin lataamiseen ja
     * tallentamiseen. */
    private final String TIEDNIMI = "sokkelo.txt";

    /** Taulukko, joka säilöö viitteet sokkelon muodostaviin rakenne-olioihin. */
    private Osa[][] osat;

    /** Lippumuuttuja, joka määrittää lopetetaanko peli. */
    private boolean sokkeloLapaisty = false;

    /** Sokkelo.txt -tiedoston ensimmäinen rivi. */
    private String ekarivi;

    /**Lukuaksessori monkija-oliolle.
     * 
     * @return Monkija-tyyppinen monkija-olio.
     */
    protected Monkija monkija() {
        return monkija;
    }
    
    /**Lukuaksessori osat-taulukolle.
     * 
     * @return Osa-tyyppinen taulukko.
     */
    protected Osa[][] osat(){
        return osat;
    }
    
    /** Komennolla "liiku" liikutetaan mönkijää komennon perässä annettavaan
     * suuntaan.
     * <p>
     * Liikkeen suunta välitetään parametrina mönkijän liiku-metodille, josta
     * palautuu tieto liikkuiko mönkijä. Jos mönkijä liikkui, kerätään mönkijän
     * paikassa olevat esineet ja taistellaan paikassa olevien ja paikkaan 
     * saapuvien robottien kanssa. Jos edellisessä vaiheessa kerättiin sokkelon
     * viimeinen esine, peli päättyy. Jos mönkijä ei liikkunut, ei tehdä muuta
     * kuin taistellaan mönkijän paikkaan saapuvien robottien kanssa.
     * 
     * @param suunta Pääilmansuunta, jota kohti liikutaan.
     */
    protected void liiku(char suunta) {
        boolean esineetKeratty; //Onko keräämättomiä esineitä
        
        boolean monLiikkui = monkija.liiku(suunta, osat);
        if (monLiikkui) { //Jos mönkijä liikkui
            tarkistaPaikka(true); //Kerätään esineet
            esineetKeratty = onkoEsineetKeratty();

            if (esineetKeratty) { //Kaikki esineet on kerätty, peli päättyy
                sokkeloLapaisty = true;
                tulostaKartta();
            }
            else { //Kaikkia esineitä ei ole vielä kerätty
                boolean tappio = tarkistaPaikka(false);
                if (!tappio) { //Jos ei hävitty uudessa paikassa jo olleille roboteille
                    Automaatti.paivitaPaikat(robotit, osat);
                    paivitaRobot(); //Robotit liikkuvat
                    tarkistaPaikka(false); //Taistellaan
                    tulostaKartta();
                }
            }
        }
        else { //Robotit liikkuvat joka tapauksessa
            Automaatti.paivitaPaikat(robotit, osat);
            paivitaRobot(); //Robotit liikkuvat
            tarkistaPaikka(false); //Taistellaan
            tulostaKartta();
        }
    }
    
    /** Mönkijä ohittaa vuoronsa, jolloin vain robotit liikkuvat.
     * <p>
     * Metodi suorittaa seuraavaa:
     * <ol>
     *  <li>Liikutetaan robotteja ja päivitetään niiden paikat sokkelossa</li>
     *  <li>Taistellaan mönkijän paikkaan mahdollisesti saapuneiden robottien kanssa</li>
     *  <li>Tulostetaan kartta</li>
     * </ol>
     */
    protected void odota() {
        Automaatti.paivitaPaikat(robotit, osat);
        paivitaRobot(); //Robotit liikkuvat
        tarkistaPaikka(false); //Taistellaan saapuneiden robojen kanssa
        tulostaKartta();
    }
    
    /** Tulostetaan kartta käyttäjälle.
     * <p>
     * Metodi käy läpi Osa-tyyppisen 2D-taulukon "osat" solu kerrallaan. Jokaisen
     * taulukon solun kohdalla solussa olevan viitten oliota käsketään kertomaan
     * tulostettava merkki kutsumalla kyseisen olion kerroMerkki()-metodilla.
     */
    protected void tulostaKartta() {
        //Taulukon koko apumuuttujiin
        int riveja = osat.length;
        int sarakkeita = osat[0].length;
        //Käydään osat-taulukko läpi ja pyydetään taulukon solua kertomaan merkki
        for (int i = 0; i < riveja; i++) { //Rivi kerrallaan
            for (int j = 0; j < sarakkeita; j++) { //Sarake kerrallaan
                System.out.print(osat[i][j].kerroMerkki(osat[i][j]));
            }
            System.out.println(); //vaihdetaan rivi
        }
    }
    
    /** Päivitetään robottien paikat osat-taulukossa.
     * <p>
     * Käydään läpi robotit-listalla olevat robotti-oliot. Jokaisen robotin
     * kohdalla kysytään robotilta sen sijainti ja sijoitetaan robotti uuden 
     * sijaintinsa listalle sekä poistetaan robotti edellisestä paikastaan.
     */
    private void paivitaRobot() {
        
        //Apumuuttujia
        int rivi, sarake;
        Kaytava uusiPaikka;
        Robotti robo;
        int i = 0;
        
        while (i < robotit.koko()) { //Kunnes on tultu listan loppuun
            robo = (Robotti)robotit.alkio(i);
            //Kysytään robotin paikka
            rivi = robo.rivi();
            sarake = robo.sarake();
            //Haetaan uusi paikka
            uusiPaikka = (Kaytava)osat[rivi][sarake];
            uusiPaikka.lisaaKaytavalle(robo); //Lisätään uudelle paikalle
            robo.poistaRoboKaytavalta(osat); //Poistetaan vanhalta paikalta
            i++;
        }
    }
    
    /** Tutkitaan mönkijän sijainti ja kerätään esineet varastoon tai taistellaan
     * robottien kanssa.
     * 
     * @param kerataan "Valitsin", jolla määritetään kerätäänkö esineitä vai 
     * taistellaanko robottien kanssa, true kun kerätään esineitä.
     * @return True, jos mönkijä häviää taistelussa.
     */
    private boolean tarkistaPaikka(boolean kerataan) {
        //Käytävä mönkijän sijainnissa
        Kaytava kaytava = (Kaytava)osat[monkija.rivi()][monkija.sarake()];
        int i = 0;
        boolean monkijaHavisi = false;
        Object paikassa;
        
        /* Kun kerätään esineitä:
         * Tarkastellaan mönkijän sijainnissa olevan käytävä-olion listaa.
         * Jos listalla tulee vastaan:
         *  -Esine, lisätään mönkijän varastoon ja poistetaan esine käytävältä
         *  -Mönkijä tai robotti, ohitetaan ja katsotaan seuraavaa listan oliota
         * Listan muutosten jälkeen listan tarkastelu aletaan aina alusta,
         *  koska listan sisällön paikkaindeksit muuttuvat. */
        if (kerataan) {
            do { //Käydään listaa läpi...
                paikassa = kaytava.alkio(i);
                if (paikassa instanceof Esine) {
                    //Lisätään löydetty esine varastoon
                    monkija.lisaaVarastoon(paikassa);
                    kaytava.poista(i);
                    i = 0;
                }
                else if (paikassa instanceof Monkija)
                    i++;
                else if (paikassa instanceof Robotti)
                    i++;
            } //...kunnes listan loppu tulee vastaan
            while(i < kaytava.koko());
        }
        /* Kun taistellaan robottien kanssa:
         * Käydään käytävän listaa läpi ja jos vastaan tulee robotti niin
         * taistellaan. Mönkijän kohdalla vain edetään seuraavaan listan olioon. */
        else {
            do { //Käydään listaa läpi...
                paikassa = kaytava.alkio(i);
                if (paikassa instanceof Monkija)
                    i++;
                else if (paikassa instanceof Robotti) {
                    //Taistellaan ja jännitetään kuinka taistelussa käy
                    monkijaHavisi = taistelu();
                    i = 0;
                }
            } //...kunnes lista loppuu tai mönkijä häviää taistelun
            while(i < kaytava.koko() == !sokkeloLapaisty);
        }
        return monkijaHavisi;
    }

    /** Taistellaan kaikkien mönkijän kanssa samassa paikassa olevien robottien
     * kanssa, jos mönkijän paikasta löytyi yksikin robotti.
     * <p>
     * Metodi käy läpi robotit-listan ja jos robotilla on sama sijainti kuin
     * mönkijällä, verrataan mönkijän ja robotin energioita, minkä perusteella
     * pienienergisempi häviää ja poistetaan pelistä.
     * 
     * @return True, jos mönkijä hävisi taistelun.
     */
    private boolean taistelu() {

        //Lippumuuttuja hävisikö mönkijä taistelun
        boolean monkijaHavisi = false;
        int i = 0;
        //Mönkijän sijainti
        int monRivi = monkija.rivi();
        int monSarake = monkija.sarake();
        int robRivi, robSarake; //Apumuuttujat robon sijainnille

        //Haetaan käytävä, jolla taistellaan
        Kaytava kaytava = (Kaytava)osat[monRivi][monSarake];

        do { //Käydään robotit-listaa läpi...
            Robotti robo = (Robotti)robotit.alkio(i);

            robRivi = robo.rivi();
            robSarake = robo.sarake();

            //Jos robotti on samassa paikassa kuin mönkijä
            if (monRivi == robRivi && monSarake == robSarake) {
                //Verrataan mönkijän energiaa robotin energiaan
                int vertaus = monkija.compareTo(robo);

                if (vertaus < 0) { //Jos mönkijän energia oli pienempi
                    sokkeloLapaisty = true;
                    //Poistetaan mönkijä pelistä
                    kaytava.poistaMonkija();
                    monkijaHavisi = true;
                }
                /* Jos mönkijä voitti, poistetaan hävinnyt robotti listalta ja
                 * jatketaan robotit-listaa eteenpäin. */
                else {
                    int robonEnergia = robo.energia();
                    kaytava.poistaRobotti(robo);
                    robotit.poista(i);
                    //Vähennetään mönkijän energiasta voitetun robotin energia
                    monkija.energia(monkija.energia() - robonEnergia);
                    i = 0;
                }
            }
            else //Muuten katsotaan seuraavaa roboa
                i++;

        } //...kunnes mönkijä häviää tai lista loppuu
        while (!sokkeloLapaisty && (i < robotit.koko()));

        //Tulostetaan ilmoitus joko voitosta tai häviöstä
        if (monkijaHavisi)
            System.out.println(TAPPIO);
        else
            System.out.println(VOITTO);

        return monkijaHavisi; //Palautetaan taistelun tulos
    }

    /** Käydään läpi sokkelo, eli osat-taulukko ja tutkitaan onko keräämättömiä
     * esineitä.
     * Jos sokkelosta löytyy yksikin keräämätön esinekäännetään lippumuuttuja.
     * 
     * @return True, jos kaikki esineet on kerätty.
     */
    private boolean onkoEsineetKeratty() {
        boolean esineetKerätty = true;
        Osa osa;
        
        //Käydään sokkelo läpi
        for (Osa[] osat1 : osat) { //Rivi kerrallaan
            for (int j = 0; j < osat[0].length; j++) { //Sarake kerrallaan
                osa = osat1[j];
                //Jos osa on seinää, ohitetaan seuraava vaihe
                if (osa instanceof Kaytava) {
                    Kaytava kaytava = (Kaytava)osa;
                    //Jos käytävä on tyhjä, ohitetaan seuraava
                    if (!kaytava.onkoTyhja())
                        for (int paikka = 0; paikka < kaytava.koko(); paikka++) {
                            Sisalto paikassa = (Sisalto)kaytava.alkio(paikka);
                            if (paikassa instanceof Esine) //Jos löydetään esine
                                esineetKerätty = false;
                        }
                }
            }
        }
        return esineetKerätty;
    }
    
    /** Ladataan peli tekstitiedostosta keskusmuistiin.
     * <p>
     * Tekstitiedosto "sokkelo.txt" luetaan rivi kerrallaan. Ensimmäinen rivi
     * säilötään erilliseen luokkamuuttujaan koko pelin ajaksi, jotta mahdollinen
     * tallennus helpottuisi hieman.
     * <p>
     * Ensimmäistä riviä lukuun ottamatta kaikki rivit käydään tutkimassa 
     * apumetodissa, joka palauttaa rivin perusteella luodun olion, joka puolestaan
     * sijoitetaan asianmukaiselle paikalle sokkelossa. Seinää lukuun ottamatta
     * kaikki oliot sijoitetaan käytävälle. Esineitä voidaan sijoittaa myös
     * mönkijän varastoon, mutta periaatteessa nekin ovat käytävällä.
     */
    protected void lataa() {
        //Luodaan lista, jolle säilötään tieto olemassa olevista roboteista
        robotit = new OmaLista();
        try {
            //Valmistellaan lukija
            FileInputStream syotevirta = new FileInputStream(TIEDNIMI);
            InputStreamReader lukija = new InputStreamReader(syotevirta);
            BufferedReader puskLukija = new BufferedReader(lukija);

            //Luetaan ensimmäinen rivi, josta saadaan siemenluku ja sokkelon koko
            String[] ekaRivi = new String[3];
            try {
                if (puskLukija.ready()) {
                    ekarivi = puskLukija.readLine();
                    //Erotellaan tiedot
                    ekaRivi = ekarivi.split("["+EROTIN+"]");
                }
                else
                    System.out.println("Ekaa riviä ei luettu...");
            }
            catch(IOException e) {
                System.out.println(e);
            }
            
            //Alustetaan Automaatti-apuluokka
            int siemen = Integer.parseInt(ekaRivi[0].trim());
            Automaatti.alusta(siemen);
            
            //Alustetaan taulukko, johon säilötään viitteet sokkelon osiin
            int rivilkm = Integer.parseInt(ekaRivi[1].trim());
            int saralkm = Integer.parseInt(ekaRivi[2].trim());
            osat = new Osa[rivilkm][saralkm];

            //Lippumuuttujat, joilla katsotaan, mihin listaan esine lisätään
            boolean kaytavalla = false;
            boolean monkijassa = false;
            
            //Indeksit, joilla tiedetään mihin kohtaan taulukkoa olio lisätään
            int riv = 0;
            int sarake = 0;
            
            //Apuoliomuuttuja viimeisimmästä kohdatusta käytävästä
            Kaytava viimeisinKaytava = new Kaytava();
            
            while (puskLukija.ready()) {
                //Nollataan sarakelaskuri, kun vaihdetaan riviä
                sarake = 0;
                //Täytetään riviä sarake kerrallaan.
                while (sarake < saralkm) {
                    //Luetaan rivi
                    String rivi = puskLukija.readLine();
                    //selvitetään rivin olio
                    Osa rivinOtus = tutkiOtus(rivi);

                    if (rivinOtus instanceof Seina) {
                        //Sijoitetaan seinä taulukkoon
                        osat[riv][sarake] = (Seina)rivinOtus;
                        sarake++;
                    }
                    else if (rivinOtus instanceof Kaytava) {
                        //Luotu käytävä viimeisimmäksi
                        viimeisinKaytava = (Kaytava)rivinOtus;
                        //Ollaan käytävän listalla
                        kaytavalla = true;
                        monkijassa = false;
                        //Sijoitetaan käytävä taulukkoon
                        osat[riv][sarake] = (Kaytava)rivinOtus;
                        sarake++;
                    }
                    else if (rivinOtus instanceof Esine) {
                        if (kaytavalla) //Esine käytävälle
                            viimeisinKaytava.lisaaKaytavalle((Esine)rivinOtus);
                        else if (monkijassa) //Esine mönkijän varastoon
                            monkija.lisaaVarastoon((Esine)rivinOtus);
                    }
                    else if (rivinOtus instanceof Monkija) {
                        //Ollaan mönkijän listalla
                        kaytavalla = false;
                        monkijassa = true;
                        //Sijoitetaan mönkijän viite attribuuttiolioon
                        monkija = (Monkija)rivinOtus;
                        //Lisätään mönkijä viimeisimmälle käytävälle
                        viimeisinKaytava.lisaaKaytavalle((Monkija)rivinOtus);
                    }
                    else if (rivinOtus instanceof Robotti) {
                        //Lisätään robotti viimeisimmälle käytävälle
                        viimeisinKaytava.lisaaKaytavalle((Robotti)rivinOtus);
                        //Lisätään robotti robotit-listaan luetussa järjestyksessä
                        robotit.lisaaLoppuun((Robotti)rivinOtus);
                    }
                }
                //Vaihdetaan taulukon riviä
                riv++;
            }
            
            //Suljetaan lukija
            puskLukija.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Tutkitaan, mikä parametrina saadulla rivillä oleva olio on luokaltaan.
     * <p>
     * Olio-tyypin selvityksen jälkeen metodi luo kyseisen olion kutsumalla
     * asianomaista rakentaaja ja välittämällä muuta tarvittavat tiedot, jotka
     * löytyvät samalta riviltä.
     * 
     * @param rivi Tekstitiedoston String- tyyppine rivi, jolla olio lymyää.
     * @return Viite luotuun, riviltä löytyneeseen olioon.
     */
    private Osa tutkiOtus(String rivi) {
        //Palastellaan rivi aputaulukkoon tarkasteltavaksi
        String[] otusRivilla = rivi.split("["+EROTIN+"]");

        //Erotellaan riviltä olion tiedot, esitellään ja alustetaan ne
        int rivind = Integer.parseInt(otusRivilla[1].trim());
        int sarind = Integer.parseInt(otusRivilla[2].trim());
        int energia = 0;
        String suunta = "";
        if (otusRivilla.length == 5) { //Jos oliolla on suunta
            energia = Integer.parseInt(otusRivilla[3].trim());
            suunta = otusRivilla[4];
        }
        else if (otusRivilla.length == 4) { //Jos oliolla on vain energia
            energia = Integer.parseInt(otusRivilla[3].trim());
        }

        //Tutkitaan 1. kentän perusteella, mikä otus on kyseessä
        switch (otusRivilla[0].trim()) {
            case MON:
                return (new Monkija(rivind, sarind, energia, suunta.charAt(0)));
            case ROB:
                return (new Robotti(rivind, sarind, energia, suunta.charAt(0)));
            case ESI:
                return (new Esine(rivind, sarind, energia));
            case SEI:
                return (new Seina(rivind, sarind));
            case KAY:
                return (new Kaytava(rivind, sarind));
            default:
                System.out.println("Ei saatu oliosta selvää...");
                return null;
        }
    }

    /**
     * Tulostetaan sokkelo kokonaisuudessaan kaikkine tietoineen ja sisältöineen
     * tekstitiedostoon "sokkelo.txt".
     * <p>
     * Tiedosto, johon sokkelo tallennetaan, on sama josta peli alussa ladataan,
     * joten pelin tallennus aina hävittää pelin alkuperäisen tilan kirjoittamalla
     * sen päälle.
     */
    protected void tallenna(){
        //Tiedostoon tulostettava teksti.
        String teksti = "";
        
        //Varaudutaan tiedostonkäsittelyn virheisiin try-catch -lauseella
        try {
            //Valmistellaan tulostus tiedostoon
            File tiedosto = new File(TIEDNIMI);
            FileOutputStream tulostusvirta = new FileOutputStream(tiedosto);
            PrintWriter kirjoittaja = new PrintWriter(tulostusvirta, true);
            
            //Varmistetaan ekan rivin oikea muotoilu
            String ekaRivi[] = ekarivi.split("["+EROTIN+"]");
            String ekaKentta = ekaRivi[0].trim();
            while (ekaKentta.length() < 4) {
              ekaKentta += " ";
            }
            String tokaKentta = ekaRivi[1].trim();
            while (tokaKentta.length() < 4) {
              tokaKentta += " ";
            }
            String kolmasKentta = ekaRivi[2].trim();
            while (kolmasKentta.length() < 4) {
              kolmasKentta += " ";
            }
            ekarivi = (ekaKentta +EROTIN+ tokaKentta +EROTIN+ kolmasKentta +EROTIN);
            
            //Tulostetaan ensimmäinen rivi, jolla siemen ja rivi- sekä sarakelkm
            kirjoittaja.println(ekarivi);
            
            /* Käydään läpi taulukko osat[][], jokaisen taulukon solun (olion)
             * kohdalla käsketään kyseistä oliota kertomaan tietonsa kutsumalla
             * kyseisen olion toString-metodia. Kutsusta palautuva merkkijono
             * lisätään aina aiemmin muodostettuun tekstipätkään. */
            for (Osa osa[] : osat)
                for (Osa otus : osa)
                    teksti += (otus.toString());
            //Tulostetaan teksti tiedostoon
            kirjoittaja.print(teksti);
            //Suljetaan kirjoittaja
            kirjoittaja.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Kerrotaan Käyttöliittymän pääsilmukalle, jatketaanko pelaamista.
     * 
     * @return True, jos käyttäjä antaa komennon "lopeta", kaikki esineet on
     * kerätty, tai mönkijä häviää taistelun robottia vastaan.
     */
    protected boolean onkoSokkeloLapaisty() {
        return sokkeloLapaisty;
    }

    /** Lopetetaan peli.
     * <p>
     * Tulostetaan kartta ja asetetaan lippumuuttuja true, jotta käyttöliittymä
     * saisi tiedon, että lopettaa pääsilmukan pyörityksen.
     */
    protected void lopeta() {
        tulostaKartta();
        sokkeloLapaisty = true;
    }
}