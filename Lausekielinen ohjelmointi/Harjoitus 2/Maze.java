/* Pirttiniemi Kai Aulis, 415596
 * pirttiniemi.kai.a@student.uta.fi
 * 045-1304579
 * Viimeksi muokattu 13:55 17/01/2016
 */

public class Maze {

    /*
     * Vakioattribuutit
     */
    //Ilmansuunnat
    public static final char POHJOINEN = 'A';
    public static final char ITA = '>';
    public static final char ETELA = 'V';
    public static final char LANSI = '<';
    //Sokkelon merkkejä
    public static final char KULJETTU = 'o';
    public static final char SEINA = '.';
    //Käyttäjän komennot
    public static final char LIIKU = 'm';
    public static final char RATKAISE = 's';
    public static final char LOPETA = 'q';
    //Virhe komentoriviparametreissa
    public static final String KRPVIRHE = "Invalid command-line argument!";

    /*
     * main-operaatio tervehtii käyttäjää, sekä tarkistaa
     *  komentoriviparametrien säännönmukaisuuden.
     * Jos komentoriviparametrit eivät täytä sääntöjä tai niiden muunnoksessa
     *  (String -> int) ilmenee virhe, ohjelma siirtyy suoraan lopetus-operaatioon.
     */
    public static void main(String[] args) {
        
        int siemen = 0;
        int rivlkm = 0;
        int sarlkm = 0;
        
        //Lippumuuttuja, jolla määritetään ohitetaan komentoriviparametrien arvojen
        // tarkistuksen, jos parametrien muunnos epäonnistui tai parametreja
        // ei ole tasan 3
        boolean virhe = false;
        
        //Tervehdys
        System.out.println("-----------");
        System.out.println("| M A Z E |");
        System.out.println("-----------");
        
        //Komentoriviparametrejä oltava 3
        if (args.length == 3) {
            //Yritetään muuntaa komentoriviparametrit
            try {
                siemen = Integer.parseInt(args[0]);
                rivlkm = Integer.parseInt(args[1]);
                sarlkm = Integer.parseInt(args[2]);
            }
            //Muunnos ei onnistunut
            catch (NumberFormatException e) {
                virhe = true;
                System.out.println(KRPVIRHE);
                lopetus();
            }
        }
        //Komentoriviparametreja annettiin vähemmän tai enemmän kuin 4
        else {
            virhe = true;
            System.out.println(KRPVIRHE);
            lopetus();
        }
        
        //Annettu rivi- ja sarakelukumäärä oltava pariton
        // ja arvoltaan vähintään 3
        if (!virhe) {
            if (((rivlkm % 2 != 0) && (sarlkm % 2 != 0)) && ((rivlkm >= 3) && (sarlkm >= 3))) {
            
                //Siirrytään luomaan sokkelo
                char[][] sokkelo = luoSokkelo(siemen, rivlkm, sarlkm);
            
                //Muuttujilla rivind ja sarind seurataan suuntamerkin sijaintia ja
                // sokkelon edistymistä. Arvoiksi asetetaan (0,1) koska se on sokkelon
                // lähtöasetelmassa suuntamerkin sijainti.
                int rivind = 0;
                int sarind = 1;
            
                //Siirrytään lueKomento-operaatioon, missä ohjelman pääsilmukka
                // (varsinainen suorituspaikka) sijaitsee.
                lueKomento(sokkelo, rivind, sarind);
            }
            //Arvot parilliset tai alle 3
            else {
                System.out.println(KRPVIRHE);
                lopetus();
            }
        }
    }
    
    /*
     * luoSokkelo-operaatio luo sokkelon Generator-apuluokan avulla.
     * Generator vaatii siemenluvun itsensä alustamiseen, sekä rivien
     *  ja sarakkeiden lukumäärän varsinaisen sokkelon luomista varten.
     */
    public static char[][] luoSokkelo(int siemen, int rivlkm, int sarlkm) {
        
        //Sokkelon luova generaattori alustetaan siemenluvulla
        Generator.initialise(siemen);
        
        //Lopullinen sokkelo luodaan
        char[][] sokkelo = Generator.generate(rivlkm, sarlkm);
        
        //Sokkelon suulle sijoitetaan etelään osoittava suuntamerkki
        // osoittamaan sokkelon alkutilannetta
        sokkelo[Generator.ENTRYROWIND][Generator.ENTRYCOLIND] = ETELA;
        
        //Palautetaan valmis sokkelo
        return sokkelo;
    }
    
    /*
     * Tulostetaan sokkelo
     */
    public static void tulostaSokkelo(char[][] sokkelo) {
        
        //Määritellään sokkelon koko
        int rivlkm = sokkelo.length;
        int sarlkm = sokkelo[0].length;
        
        //Lasketaan rivejä
        for (int rivi = 0; rivi < rivlkm; rivi++) {
            //Lasketaan sarakkeita
            for (int sarake = 0; sarake < sarlkm; sarake++)
                //Tulostetaan merkki
                System.out.print(sokkelo[rivi][sarake]);
            //Vaihdetaan rivi
            System.out.println();
        }
    }
    
    /*
     * lueKomento esittää käyttäjälle vaihtoehdot mahdollisista toiminnoista,
     *  ja ohjaa ohjelman kulkua käyttäjän antaman komennon mukaisesti.
     * Parametrit rivind ja sarind tarkkailevat sokkelon etenemistä.
     */
    public static void lueKomento(char[][] sokkelo, int rivind, int sarind) {
        
        //Esitellään ja alustetaan paikanseurantataulukko
        //Taulukko alustetaan rivind ja sarind, koska jos ainoa komento on RATKAISE,
        // saadaan NullPointerException. Tämä taas johtuu siitä, että taulukkoa
        // päivitetään 1. kerran vasta LIIKU-komennon jälkeen.
        int[] paikka = {rivind, sarind};
        //Esitellään komento-muuttuja
        char komento;
        //Lippumuuttuja, jolla päästään ulos pääsilmukasta,
        //kun sokkelo on ratkaistu.
        boolean ulkona = false;
        
        do {
            
            //Päivitetään tilanne käyttäjälle tulostamalla sokkelo
            tulostaSokkelo(sokkelo);
            
            //Tutkitaan, mihin suuntaan ollaan menossa erillisellä metodilla etsiSuunta.
            char suunta = etsiSuunta(sokkelo);
            
            //Tarkistetaan, onko saavuttu uloskäynnille
            //Apumuuttujilla i ja j määtitetään sokkelon uloskäynnin sijainti
            int i = sokkelo.length - 1;
            int j = sokkelo[0].length - 2;
            
            if (sokkelo[i][j] == POHJOINEN || sokkelo[i][j] == ITA || sokkelo[i][j] == ETELA || sokkelo[i][j] == LANSI) 
                //Asetetaan lippumuttuja, ettei kysellä komentoja
                ulkona = true;
            
            //Kysellään komentoja, kunnes ollaan ulkona
            if (!ulkona) {
                
                //Esitetään komentovaihtoehdot
                System.out.println("m/move, s/solve, q/quit?");
                //Luetaan
                komento = In.readChar();
                
                //Toimitaan komennon mukaan
                if (komento == LOPETA)
                    //Poistutaan silmukasta
                    ulkona = true;
                else if (komento == RATKAISE)
                    ratkaiseSokkelo(sokkelo, rivind, sarind, suunta);
                else if (komento == LIIKU)
                    paikka = askellaSokkelossa(sokkelo, rivind, sarind, suunta);
            }
            
            //Päivitetään paikanseurantajärjestelmä
            rivind = paikka[0];
            sarind = paikka[1];
            
        }
        while (!ulkona);
        
        //Uloskäynnille on saavuttu, joten lopetetaan ohjelma
        lopetus();
    }
    
    /* 
     * askellaSokkelossa päättelee sen hetkisen suunnan, ja ohjaa ohjelman
     *  suoritusta suunnan mukaisesti sen mukaisesti suuntaa vastaavaan metodiin.
     * 
     */
    public static int[] askellaSokkelossa(char[][] sokkelo, int rivind, int sarind, char suunta) {
        
        int[] paikka = null;
        
        //Suunta on pohjoiseen
        if (suunta == POHJOINEN)
            paikka = suuntaPohjoiseen(sokkelo, rivind, sarind, suunta);
        
        //Suunta on itään
        if (suunta == ITA)
            paikka = suuntaItaan(sokkelo, rivind, sarind,suunta);
        
        //Suunta on etelään
        if (suunta == ETELA)
            paikka = suuntaEtelaan(sokkelo, rivind, sarind, suunta);
                    
        //Suunta on länteen
        if (suunta == LANSI)
            paikka = suuntaLanteen(sokkelo, rivind, sarind, suunta);
            
        return paikka;
    }
    
    public static char etsiSuunta(char[][] sokkelo) {
        
        //Mitä etsitään
        //Alustetaan, koska muuten return lauseesta tulee virheilmoitus:
        // "suunta might not have been initialized"
        char suunta = 'x';
        
        //Apumuuttujat
        int rivlkm = sokkelo.length;
        int sarlkm = sokkelo[0].length;
        
        //for-silmukat etsivät sokkelosta suuntamerkin
        for (int i = 0; i < rivlkm; i++){
            for (int j = 0; j < sarlkm; j++) {
                if (sokkelo[i][j] == POHJOINEN)
                    suunta = POHJOINEN;
                else if (sokkelo[i][j] == ITA)
                    suunta = ITA;
                else if (sokkelo[i][j] == ETELA)
                    suunta = ETELA;
                else if (sokkelo[i][j] == LANSI)
                    suunta = LANSI;
                else;
            }
        }
        
        //Palautetaan suuntamerkki
        return suunta;
    }
    
    /*
     * Tämä kommentti pätee kaikkiin neljään suuntaX-metodiin.
     * etsiSuunta-metodin palauttaman suuntamerkin mukaan siirrytään merkin mukaiseen
     *  metodiin, joissa kussakin päätellään seuraava mahdollinen suunta.
     * Esimerkiksi suuntaPohjoiseen: if-lauseet tarkistavat mahdolliset suunnat 
     *  oikean käden säännön mukaisessa järjestyksessä (oikea, eteen, vasen), jos
     *  mikään ei käy, käännytään ympäri.
     * Kun mahdollinen suunta löytyy, siihen paikalle asetetaan KULJETTU-merkki ja
     *  uuteen sijaintiin vastaan liikesuunnan mukainen merkki. Esim. kun mennään
     *  pohjoiseen ja käännytään oikealle, uusi suunta on itään.
     */
    
    public static int[] suuntaPohjoiseen(char[][] sokkelo, int rivind, int sarind, char suunta) {
    
        //Päivitetään paikka
        int[] paikka = {rivind, sarind};
        
        //Päästäänkö...
        //Oikealle?
        if (sokkelo[rivind][sarind+1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind+1] = ITA;
            sarind++;
            paikka[1] = sarind;
        }
        //Eteenpäin?
        else if (sokkelo[rivind-1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind-1][sarind] = POHJOINEN;
            rivind--;
            paikka[0] = rivind;
        }
        //Vasemmalle?
        else if (sokkelo[rivind][sarind-1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind-1] = LANSI;
            sarind--;
            paikka[1] = sarind;
        }
        //Umpikuja -> käännytään ympäri paikallaan
        else 
            sokkelo[rivind][sarind] = ETELA;
        
        return paikka;
    }
    
    public static int[] suuntaItaan(char[][] sokkelo, int rivind, int sarind, char suunta) {
        
        //Päivitetään paikka
        int[] paikka = {rivind, sarind};
        
        //Päästäänkö...
        //Oikealle?
        if (sokkelo[rivind+1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind+1][sarind] = ETELA;
            rivind++;
            paikka[0] = rivind;
        }
        //Eteenpäin?
        else if (sokkelo[rivind][sarind+1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind+1] = ITA;
            sarind++;
            paikka[1] = sarind;
        }
        //Vasemmalle?
        else if (sokkelo[rivind-1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind-1][sarind] = POHJOINEN;
            rivind--;
            paikka[0] = rivind;
        }
        //Umpikuja -> ympäri paikallaan
        else 
            sokkelo[rivind][sarind] = LANSI;
        
        return paikka;
    }
    
    public static int[] suuntaEtelaan(char[][] sokkelo, int rivind, int sarind, char suunta) {
    
        //Päivitetään paikka
        int[] paikka = {rivind, sarind};
    
        //Päästäänkö...
        //Oikealle?
        if (sokkelo[rivind][sarind-1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind-1] = LANSI;
            sarind--;
            paikka[1] = sarind;
        }
        //Eteenpäin?
        else if (sokkelo[rivind+1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind+1][sarind] = ETELA;
            rivind++;
            paikka[0] = rivind;
        }
        //Vasemmalle?
        else if (sokkelo[rivind][sarind+1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind+1] = ITA;
            sarind++;
            paikka[1] = sarind;
        }
        //Umpikuja -> ympäri paikallaan
        else 
            sokkelo[rivind][sarind] = POHJOINEN;

        return paikka;
    }
    
    public static int[] suuntaLanteen(char[][] sokkelo, int rivind, int sarind, char suunta) {
    
        //Päivitetään paikka
        int[] paikka = {rivind, sarind};
    
        //Päästäänkö...
        //Oikealle?
        if  (sokkelo[rivind-1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind-1][sarind] = POHJOINEN;
            rivind--;
            paikka[0] = rivind;
        }
        //Eteenpäin?
        else if (sokkelo[rivind][sarind-1] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind][sarind-1] = LANSI;
            sarind--;
            paikka[1] = sarind;
        }
        //Vasemmalle?
        else if (sokkelo[rivind+1][sarind] != SEINA) {
            sokkelo[rivind][sarind] = KULJETTU;
            sokkelo[rivind+1][sarind] = ETELA;
            rivind++;
            paikka[0] = rivind;
        }
        //Umpikuja -> ympäri paikallaan
        else
            sokkelo[rivind][sarind] = ITA;
        
        return paikka;
    }
    
    /* 
     * ratkaiseSokkelo
     * Sokkelon ratkaisu siirtyy do-while-silmukkaan, joka pyörii kunnes lippumuutujalla
     *  havaitaan, että on saavuttu uloskäynnille.
     * Silmukka on rakenteeltaan samanlainen kuin pääsilmukka lueKomento-metodissa,
     *  mutta ilman komennon lukemista ja sokkelo tulostetaan kun on saavuttu maaliin.
     */
    public static void ratkaiseSokkelo(char[][] sokkelo, int rivind, int sarind, char suunta) {
        
        boolean ulkona = false;
        int[] paikka;

        //Tarkistetaan, onko saavuttu uloskäynnille
        //Apumuuttujilla i ja j määtitetään sokkelon uloskäynnin sijainti
        int i = sokkelo.length - 1;
        int j = sokkelo[0].length - 2;
        
        do {
           suunta = etsiSuunta(sokkelo);
           paikka = askellaSokkelossa(sokkelo, rivind, sarind, suunta);
           
           //Tarkistetaan onko saavuttu uloskäynnille
           if (sokkelo[i][j] == POHJOINEN || sokkelo[i][j] == ITA || sokkelo[i][j] == ETELA || sokkelo[i][j] == LANSI)
               ulkona = true;

            rivind = paikka[0];
            sarind = paikka[1];
        }
        while (!ulkona);

        //Kun sokkelo on ratkaistu, ohjelma palaa operaation kutsupaikkaan lueKomento,
        // mistä poistutaan lopetus-operaatioon.
    }
    
    /*
     * lopetus-operaatio tervehtii käyttäjää hyvästiksi,
     * minkä jälkeen ohjelma päättyy.
     */
    public static void lopetus() {
        System.out.println("Bye, see you soon.");
    }
}