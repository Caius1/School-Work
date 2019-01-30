package luokat;

import apulaiset.*;

/** Käyttäjän ohjaamaa mönkijä-oliota mallintava luokka.
 * <p>
 * Monkija-luokka sisältää kaikki mönkijän toimintoihin ja tietoihin liittyvät
 * metodit, kuten mönkijän käytäväpaikan päivitys liikkeen jälkeen ja varaston
 * tulostus.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.1, 8.4.2016.
 * <p>8.4.2016: Liiku-metodi palauttaa boolean arvon.
 */
public class Monkija extends Sisalto implements Suunnallinen {

    //Mönkijän varasto, jonne säilötään mönkijän käytäviltä keräämät esineet.
    private OmaLista monkijaLista;
    
    //Ilmansuunta, johon mönkijä liikkui viimeksi.
    private char suunta;
    
    // Onomatopoeettinen ilmoitus kolarista, kun mönkijä ajaa seinään.
    private final String KOPS = "Kops!";
    
    /* Mönkijän oletusrakentaja. */
    public Monkija(){}
    /** Mönkijän kuormitettu rakentaja.
     * 
     * @param riviind Mönkijän rivi-indeksi.
     * @param saraind Mönkijän sarakeindeksi.
     * @param energ Parametriarvo energia-attribuutille.
     * @param ilmansuunta Parametriarvo suunnalle, johon mönkijä on tällä
     * hetkellä menossa.
     */
    public Monkija(int riviind, int saraind, int energ, char ilmansuunta) {
        super(riviind, saraind, energ);
        suunta(ilmansuunta);
        monkijaLista = new OmaLista();
    }

    /** Mönkijän suunnan lukumetodi.
     *
     * @return jokin yllä määritellyistä neljästä pääilmansuunnan symbolista.
     */
    @Override
    public char suunta() {
       return suunta;
    }

    /** Mönkijän suunnan asetusmetodi.
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

    /** Lisää esineen mönkijän varastoon.
     * 
     * @param esine Varastoon lisättävä esine.
     */
    public void lisaaVarastoon(Object esine) {
        monkijaLista.lisaaListalle(esine);
    }

    /**
     * Muunna-metodi muuntaa mönkijän OmaLista-varastosta käyttäjän haluaman
     * määrän Esine-olioita energiaksi.
     * <p>
     * Muunnoksesta saatu energia lisätään energiaan, joka mönkijällä oli ennen
     * muunnosta, ja näiden energioiden summa asetetaan mönkijän uudeksi
     * energiaksi.
     * 
     * @param luku Muunnettavien esineiden lukumäärä.
     */
    public void muunna(String luku) {
        
        boolean voiMuuntaa = true;
        int maara = 0;
        
        //Muunnetaan String luku int-tyyppiseksi määräksi
        try {
            maara = Integer.parseInt(luku);
        }
        //Jos parseInt ei onnistunut
        catch(NumberFormatException e) {
            voiMuuntaa = false;
        }
        
        /* Jos parseInt onnistui, tarkistetaan muunnettava määrä. Määrän on
         *  oltava suurempi kuin 0, mutta pienempi kuin varaston koko. */
        if (0 > maara || maara > monkijaLista.koko())
            voiMuuntaa = false;
        
        //Muunnetaan annettu määrä esineitä energiaksi, jos varasto ei ole tyhjä
        if (voiMuuntaa && (monkijaLista.koko() > 0)) {
            //Käydään muuntamassa esineet energiaksi apumetodissa
            int saatuEnergia = (energia() + muunnaEnergiaksi(maara));
            energia(saatuEnergia); //Asetetaan mönkijälle uusi energia
        }
        else //Virheilmoitus, jos parametri oli virheellinen
            System.out.println(VIRHE);
    }

    /** Poistaa annetun määrän esineitä mönkijän varastosta listan alusta lähtien
     * ja esineistä kerätyn, yhteenlasketun energiamäärän.
     *
     * @param maara Listalta poistettavien esineiden lukumäärä.
     * @return saatuEnergia Int-muotoinen määrä energiaa, joka saatiin listalta
     * poistetuista esineistä.
     */
    private int muunnaEnergiaksi(int maara) {
        
        //Apumuuttuja kerätylle energialle
        int saatuEnergia = 0;
        
        //Aloitetaan listalta poistaminen listan alusta
        for (int i = 0; i < maara; i++) {
            //Poistetaan ensimmäinen/seuraava, palauttaa Object-tyypin
            Object poistettu = monkijaLista.poistaAlusta();
            //Muutetaan poistetun tyyppi Esineeksi, jotta saadaan energia irti
            Esine esine = (Esine)poistettu;
            //Otetaan poistetulta esineeltä energiapitoisuus talteen
            saatuEnergia += esine.energia();
        }
        
        //Palautetaan esineistä saatu energia
        return saatuEnergia;
   }

    /** Tulostetaan näytölle mönkijän tiedot sekä varaston sisältö kutsumalla
     * mönkijän toString()-metodia. */
    public void inventoi() {
        System.out.print(toString());
    }

    /**
     * Tulostaa komentoikkunaan halutussa ilmansuunnassa sijaitsevan sokkelon
     * rakenneosan tiedot.
     * <p>
     * Jos katsotaan seinää, tulostuu vain seinä. Jos katsotaan käytävää,
     * tulostetaan itse käytävä sekä käytävän sisältö kuten robotit ja esineet.
     * 
     * @param ilmansuunta Ilmansuunta, johon katsotaan.
     * @param osat Taulukko, jossa sokkelon Rakenne-osat, joissa Sisalto-osat
     */
    public void katso(String ilmansuunta, Osa[][] osat) {

        //Katsottavan koordinaatit, alustetaan mönkijän sijainnilla, josta katsotaan
        int r = rivi();
        int s = sarake();

        //Päätellään katsottavan osan koordinaatit
        switch(ilmansuunta.charAt(0)) {
            case POHJOINEN:
                r--; break;
            case ITA:
                s++; break;
            case ETELA:
                r++; break;
            case LANSI:
                s--; break;
            default:
                System.out.println(VIRHE); break;
        }

        //Käsketään halutussa sijainnissa olevaa osaa kertomaan olemuksestaan
        //Tiedot tulostuvat vain, jos ei katsota mönkijän sijaintia
        if (r != rivi() || s != sarake())
            System.out.print(osat[r][s].toString());
    }

    /**
     * Liikuttaa mönkijää käyttäjän haluamaan suuntaan.
     * <p>
     * Jos liikuttavassa suunnassa on seinä, mönkijä ajaa seinään, jolloin mönkijän
     * sijainti ei muutu ja näytölle tulostuu ilmoitus törmäyksestä seinän kanssa.
     * Mönkijän suunta muuttuu onnistui liikkuminen tai ei.
     * 
     * @param suunta Ilmansuunta, johon liikutaan.
     * @param osat Taulukko, jossa sokkelon Rakenne-osat, joissa Sisalto-osat.
     * @return True, jos mönkijän liike onnistui eli ei ajettu seinään.
     */
    public boolean liiku(char suunta, Osa[][] osat) {
        //Liikkuiko mönkijä
        boolean monLiikkui = false;
        //Tavoitellussa suunnassa sijaitseva sokkelon osa
        Osa kohde;
        //Mönkijän sijainti
        int r = rivi();
        int s = sarake();
        //Liikutaan haluttuun suuntaan. Ajetaan seinään, jos suunnassa on seinä
        switch(suunta) {
            case POHJOINEN:
                kohde = osat[r-1][s];
                if (kohde.sallittu()) {
                    rivi(--r);
                    monLiikkui = true;
                }
                else
                    System.out.println(KOPS);
                suunta(POHJOINEN);
                break;
            case ITA:
                kohde = osat[r][s+1];
                if (kohde.sallittu()) {
                    sarake(++s);
                    monLiikkui = true;
                }
                else
                    System.out.println(KOPS);
                suunta(ITA);
                break;
            case ETELA:
                kohde = osat[r+1][s];
                if (kohde.sallittu()) {
                    rivi(++r);
                    monLiikkui = true;
                }
                else
                    System.out.println(KOPS);
                suunta(ETELA);
                break;
            case LANSI:
                kohde = osat[r][s-1];
                if (kohde.sallittu()) {
                    sarake(--s);
                    monLiikkui = true;
                }
                else
                    System.out.println(KOPS);
                suunta(LANSI);
                break;
            default:
                System.out.println(VIRHE); break;
        }
        //Siirretään mönkijä uuden sijainnin käytävän listalle, jos liike onnistui
        if (monLiikkui)
            siirraMonkija(osat);
        //Palautetaan tieto liikkestä
        return monLiikkui;
    }

    /**
     * Siirretään mönkijä edellisen käytäväsijainnin listalta
     * uuden käytävän listalle.
     * <p>
     * Siirto tapahtuu päättelemällä mönkijän edellinen sijainti, hakemalla ja
     * poistamalla mönkijä edellisen sijaintinsa listalta ja lisäämällä mönkijä
     * uuden sijaintisa listalle.
     * 
     * @param osat Taulukko, jonka sisällä mönkijää siirretään.
     */
    private void siirraMonkija(Osa[][] osat) {
        
        /* Kaytava-tyyppiset muuttujat edelliselle ja uudelle paikalle,
         * jotta päästään kiinni kyseisten käytäväpaikkojen lista-operaatioihin */
        Kaytava edelPaikka = null;        
        Kaytava uusiPaikka;
        
        //Uusi paikka on mönkijän nykyinen sijainti
        uusiPaikka = (Kaytava)osat[rivi()][sarake()];
        //Edellinen paikka on käytävä, josta mönkijä tuli
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
            default: System.out.println("Virhe siirraMonkija!"); break;
        }
        
        /* Jos edellinen paikka saatiin haettua.
         * Poistetaan mönkijä edelliseltä paikaltaan ja lisätään mönkijä uuden
         * paikan listalle */
        if (edelPaikka != null) {
            Object monkija = edelPaikka.poistaMonkija();
            uusiPaikka.lisaaKaytavalle(monkija);
        }
        
        /* Päivitetään mönkijän varaston esineiden sijainti-indeksit vastaamaan
         * mönkijän uutta sijaintia, koska mönkijän liikkuessa myös esineet
         * liikkuvat */
        int monRivi = rivi();
        int monSarake = sarake();
        //Käydään varasti läpi ja päivitetään indeksit
        for (int i = 0; i < monkijaLista.koko(); i++){
            Esine esine = (Esine)monkijaLista.alkio(i);
            esine.rivi(monRivi);
            esine.sarake(monSarake);
        }
    }

    /**{@inheritDoc}
     * 
     * @return Mönkijän tietue, joka sisältää mlnkijän tietojen lisäksi mönkijän
     * varastossa olevien esineiden tietueet.
     */
    @Override
    public String toString() {
        
        //Mönkijän tietue
        String monkijaTietue = ("Monkija  " +EROTIN+ super.toString() +suunta+"   "+EROTIN+RIVIVAIHTO);
        
        //Mönkijän varaston tietue
        String varasto = "";
        //Käydään varasto läpi ja lisätään varaston sisältö varaston tietueeseen
        for (int i = 0; i < monkijaLista.koko(); i++) {
            varasto += (monkijaLista.alkio(i).toString());
        }
        return (monkijaTietue + varasto);
    }

    /**{@inheritDoc}
     * 
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /** {@inheritDoc}
     * @param o {@inheritDoc}
     * @return {@inheritDoc} */
    @Override
    public int compareTo(Sisalto o) {
        return super.compareTo(o);
    }
}