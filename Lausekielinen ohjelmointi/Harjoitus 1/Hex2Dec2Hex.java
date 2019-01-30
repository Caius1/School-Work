/* Tämä on Lausekielinen Ohjlemointi II -kurssin (TIEP5) 1. harjoitustyö.
 * Tässä harjoitustyössä tehdään ohjelma, joka muuntaa heksadesimaalilukuja (16-luku) desimaaliksi (10-luku) ja päinvastoin.
 * Ohjelmassa tulostukset ovat englanniksi, koska tehtävänantokin oli esimerkkien osalta.
 * Ohjelman suorituksen osat on jaettu 4:än operaatioon: main, kohdeKanta, muunnaDesi ja muunnaHeksa.
 * Operaatioiden välillä ei välitetä tietoja eivätkä operaatiot palauta mitään.
 *
 * Ohjelmassa voi olla tehty samoja asioita eri tavoilla, mutta se johtuu vain siitä, että työtä on tehty useina päivinä.
 * Huomasin vasta lopputarkistuksia tehdessäni, että virheilmoitus ja ohjelman ymmärtämät valinnat piti vakioida.
 * Ei siinä muuta ongelmaa kuin, että oma vakioton ratkaisuni oli huomattavasti siistimpi ja helpompi lukea kuin tämä vakioitu.
 * Laitetaan sitten vakiot kun täytyy, mutta en alkanut enää enempää muutoksia tekemään.
 *
 * Kirjoittanut Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi, 415596
 * Aloitettu 30/10/2015, viimeksi muokattu 14:09 23/12/2015.
 */

public class Hex2Dec2Hex {

   //Virheilmotus attribuuttina, jotta käyttö useassa oliossa helpottuisi
   public static final String ERROR = "Error!";

   //main-operaatiossa tervehditään käyttäjää, mutta siirrytään heti kohdekannan valintaan.
   public static void main (String [] args) {

      System.out.println("Hello! I am your friendly number converter.");

      kohdeKanta();

   }
   
   //kohdeKanta-operaatiossa käyttäjä valitsee lukujärjestelmän, johon muunnetaan.
   //Käyttäjän syötteen tulee olla toinen kahdesta vaihtoehdosta. Tarkistus ja uudelleensyöttö tapahtuu do-while -silmukassa
   // lippumuuttujan avulla.
   public static void kohdeKanta() {
      
      final int KOHDEHEXA = 16;
      final int KOHDEDESI = 10;
      boolean syoteOK = false;
      int syote;

      do {
         System.out.println("Enter the target base (16/10):");

         syote = In.readInt();

         if (syote == KOHDEHEXA) {
            syoteOK = true;
            syote = KOHDEDESI;
         }
         else if (syote == KOHDEDESI) {
            syoteOK = true;
            syote = KOHDEHEXA;
         }
         else
            System.out.println(ERROR);

      }
      while (!syoteOK);

      //Siirrytään valintaa vastaavaan operaatioon, joka muuntaa syötetyn luvun ja tulostaa muunnoksen tuloksen.
      if (syote == KOHDEDESI)
         muunnaDesi();
      else
         muunnaHeksa();
   }
   
   //muunnaDesi-operaatiossa muunnetaan desimaaliluku heksadesimaaliluvuksi, 10 -> 16.
   //Syöte tarkistetaan desimaaliksi lippumuuttujalla ohjatussa do-while -silmukassa.
   public static void muunnaDesi() {

      boolean syoteOK = false;
      boolean laskuValmis = false;
      int syote, syoteJaannos;
      String valiTulos = "";
      String tulos = "";

      //Luetaan ja tarkistetaan syöte
      do {
         System.out.println("Enter a decimal number:");

         syote = In.readInt();

         if (syote > 0)
            syoteOK = true;
         else
            System.out.println(ERROR);

      }
      while (!syoteOK);

      //Lasketaan hexa-arvo yhdessä silmukassa kahden laskun avulla.
      do {
         
         //1. lasku laskee jakojäännökset ja lisää ne apumuuttujaan (valiTulos).
         syoteJaannos = (char)(syote % 16);
         if (syoteJaannos == 10)
            valiTulos = valiTulos + 'A';
         else if (syoteJaannos == 11)
            valiTulos += 'B';
         else if (syoteJaannos == 12)
            valiTulos += 'C';
         else if (syoteJaannos == 13)
            valiTulos += 'D';
         else if (syoteJaannos == 14)
            valiTulos += 'E';
         else if (syoteJaannos == 15)
            valiTulos += 'F';
         else
            valiTulos += syoteJaannos;

         //2. lasku jakaa syötettä luvulla 16 ja ohjaa silmukan suorituksen lippumuuttujalla päätökseen kun syötettä ei voi enää jakaa.
         //Kun syötettä ei voida enää jakaa (eikä siitä saada enää jakojäännöksiä irti), käännetään laskuValmis -lippumuuttuja, jolloin
         // voidaan poistua silmukasta.
         syote = (int)(syote / 16);
         if (syote <= 0)
            laskuValmis = true;
      }
      while (!laskuValmis);

      //Hexadesimaali on oikein, mutta takaperin apumuuttujassa. Seuraavaksi käännetään se oikein päin lopulliseen muotoonsa.
      for (int i = valiTulos.length() - 1; i >= 0 ; i--)
         tulos += valiTulos.charAt(i);
         
      //Tulostetaan lopullinen oikeinpäin oleva tulos.
      System.out.println("As hexadecimal: " + tulos);
      
      //Siirrytään kysymymään käyttäjältä, haluaako hän jatkaa lukujen muuntamista
      jatketaanko();
   }

   //muunnaHeksa-operaatiossa muunnetaan heksadesimaaliluku desimaaliluvuksi, 16 -> 10.
   //Syötettä kysellään lippumuuttujalla ohjatussa do-while -silmukassa ja syöte tarkistetaan for-silmukassa String-operaatioilla.
   public static void muunnaHeksa() {

      boolean syoteOK = false;
      String syote;
      int tulos = 0;
      int pituus;

      do {

         //"merkitOK" on ehkä hieman tarpeeton lippumuuttuja, mutta sillä tarkistetaan
         // syötteen tarkistuksen lopputulos ja toimitaan sen mukaisesti.
         boolean merkitOK = true;
      
         System.out.println("Enter a hexadecimal number:");

         syote = In.readString();

         pituus = syote.length();

         //Syötteen tarkistus, käydään läpi kaikki annetut merkit. Käytetään "merkki"-apumuuttujaa.
         for (int i = 0; i < pituus; i++) {
            char merkki = syote.charAt(i);
            if ((merkki < '0' || merkki > '9') && (merkki < 'A' || merkki > 'F'))
               merkitOK = false;
         }

         if (merkitOK == true)
            syoteOK = true;
         else
            System.out.println(ERROR);

      }
      while (!syoteOK);

      //j-apumuuttujalla määritetään tarvittava 16:sta potenssi, 16^j.
      int j = syote.length();

      //Karun näköisessä silmukassa tarkkaillaan String syötteen merkkejä yksi kerrallaan.
      //Merkin mukaan päätellään tarvittava laskutoimitus, jolla lisätään tulokseen
      // heksadesimaalin merkkiä vastaava desimaaliluku.
      for (int i = 0; i < pituus; i++) {
         j--;
         if (syote.charAt(i) == '0')
            tulos = tulos +  (int)(0 * Math.pow(16, j));
         else if (syote.charAt(i) == '1')
            tulos += (int)(1 * Math.pow(16, j));
         else if (syote.charAt(i) == '2')
            tulos += (int)(2 * Math.pow(16, j));
         else if (syote.charAt(i) == '3')
            tulos += (int)(3 * Math.pow(16, j));
         else if (syote.charAt(i) == '4')
            tulos += (int)(4 * Math.pow(16, j));
         else if (syote.charAt(i) == '5')
            tulos += (int)(5 * Math.pow(16, j));
         else if (syote.charAt(i) == '6')
            tulos += (int)(6 * Math.pow(16, j));
         else if (syote.charAt(i) == '7')
            tulos += (int)(7 * Math.pow(16, j));
         else if (syote.charAt(i) == '8')
            tulos += (int)(8 * Math.pow(16, j));
         else if (syote.charAt(i) == '9')
            tulos += (int)(9 * Math.pow(16, j));
         else if (syote.charAt(i) == 'A')
            tulos += (int)(10 * Math.pow(16, j));
         else if (syote.charAt(i) == 'B')
            tulos += (int)(11 * Math.pow(16, j));
         else if (syote.charAt(i) == 'C')
            tulos += (int)(12 * Math.pow(16, j));
         else if (syote.charAt(i) == 'D')
            tulos += (int)(13 * Math.pow(16, j));
         else if (syote.charAt(i) == 'E')
            tulos += (int)(14 * Math.pow(16, j));
         else if (syote.charAt(i) == 'F')
            tulos += (int)(15 * Math.pow(16, j));
         else;
      }

      //Tulostetaan edellisen päättelyn lopputulos
      System.out.println("As decimal: " + tulos);
      
      //Siirrytään kysymymään käyttäjältä, haluaako hän jatkaa lukujen muuntamista
      jatketaanko();
   }
   
   //jatketaanko-operaatiossa kysytään käyttäjän halua jatkaa numeromuunnoksia.
   //Jos jatketaan, palataan kohdeKanta-operaatioon.
   //Jos ei jatketa, ohjelma päättyy tervehdykseen.
   public static void jatketaanko () {
      
      final char KYLLA = 'y';
      final char EI = 'n';
      boolean syoteOK = false;
      char valinta;

      //Kysytään haluaako käyttäjä jatkaa. Siirrytään takaisin kohdeKanta-operaation jos kyllä. Jos ei, ohjelma päättyy.
      do {
      System.out.println("Continue (y/n)?");

      valinta = In.readChar();

      //Käyttäjän valinnan tarkistus
      if (valinta == KYLLA) {
         syoteOK = true;
         valinta = KYLLA;
      }
      else if (valinta == EI) {
         syoteOK = true;
         valinta = EI;
      }
      else
         System.out.println(ERROR);
      }
      while (!syoteOK);

      //Toimitaan valinnan mukaan.
      if (valinta == KYLLA)
         kohdeKanta();
      else
         System.out.println("See you soon.");
   }

}