package de.lingen.hsosna.texview;

/**
 * Ein einzelner Artikel wird dargestellt und erhält Daten aus Artikelkombinationen
 */
public class Artikel {
    private String artikelID;
    private String artikel_Bezeichnung;
    private String farbe_ID;
    private String farbe_Bezeichnung;
    private String groessen_ID;
    private String fertigungszustand;
    private String menge;
    private String mengenEinheit;

    /**
     * @param artikelID           Artikel ID des Artikels
     * @param artikel_Bezeichnung Artikelkurzbezeichnung des Artikels
     * @param farbe_ID            Farb ID des Artikels
     * @param farbe_Bezeichnung   Farbbezeichnung des Artikels
     * @param groessen_ID         Größen ID des Artikels
     * @param fertigungszustand   Fertigungszustand des Artikels
     * @param menge               Menge des Artikels
     * @param mengenEinheit       zugehörige Einheit zu der Menge
     */
    public Artikel (String artikelID, String artikel_Bezeichnung, String farbe_ID,
                    String farbe_Bezeichnung, String groessen_ID, String fertigungszustand,
                    String menge, String mengenEinheit) {
        this.artikelID = artikelID;
        this.artikel_Bezeichnung = artikel_Bezeichnung;
        this.farbe_ID = farbe_ID;
        this.farbe_Bezeichnung = farbe_Bezeichnung;
        this.groessen_ID = groessen_ID;
        this.fertigungszustand = fertigungszustand;
        this.menge = menge;
        this.mengenEinheit = mengenEinheit;
    }

    /**
     * @return Artikel ID des Artikels
     */
    public String getArtikelID () {
        return artikelID;
    }

    /**
     * @return Artikelkurzbezeichnung (kann leer sein)
     */
    public String getArtikel_Bezeichnung () {
        return artikel_Bezeichnung;
    }

    /**
     * @return Farben ID des Artikels
     */
    public String getFarbe_ID () {
        return farbe_ID;
    }

    /**
     * @return Farbbezeichnung des Artikels
     */
    public String getFarbe_Bezeichnung () {
        return farbe_Bezeichnung;
    }

    /**
     * @return Größen ID des Artikels
     */
    public String getGroessen_ID () {
        return groessen_ID;
    }

    /**
     * @return Fertigungszustand des Artikels
     */
    public String getFertigungszustand () {
        return fertigungszustand;
    }

    /**
     * @return Menge des Artikels
     */
    public String getMenge () {
        return menge;
    }

    /**
     * @return Einheit der Menge
     */
    public String getMengenEinheit () {
        return mengenEinheit;
    }
}
