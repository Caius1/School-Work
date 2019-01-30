package luokat;

/** Sokkelon seinän osaa mallintava luokka.
 *
 * @author Kai Pirttiniemi, pirttiniemi.kai.a@student.uta.fi
 * @version 1.0, 6.4.2016.
 */
public class Seina extends Rakenne {
    
    /** Seinän oletusrakentaja. */
    public Seina(){}
    /** Seinän kuormitettu rakentaja.
     * <p>
     * @param rivind Seinän rivi-indeksi.
     * @param sarind Seinän sarakeindeksi.
     */
    public Seina(int rivind, int sarind) {
        super(rivind, sarind);
    }
    
    /** {@inheritDoc}
     *
     * @return False, koska seinän sisään ei voi sijoittaa mitään.
     */   
    @Override
    public boolean sallittu() {
        return false;
    }
    
    /** {@inheritDoc}
     * 
     * @return Seinän tiedot.
     */
    @Override
    public String toString() {
        String seinaTietue = ("Seina    " +EROTIN+ super.toString() +RIVIVAIHTO);
        return seinaTietue;
    }
}