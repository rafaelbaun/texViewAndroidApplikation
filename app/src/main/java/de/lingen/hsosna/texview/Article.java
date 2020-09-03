package de.lingen.hsosna.texview;

/**
 * Ein einzelner Artikel wird dargestellt und erhält Daten aus Artikelkombinationen
 */
public class Article {
    private int artikelID;
    private String artikel_Bezeichnung;
    private int farbe_ID;
    private String farbe_Bezeichnung;
    private int groessen_ID;
    private String fertigungszustand;
    private String menge;
    private String mengenEinheit;

    private int lagerort;
    private int lagerplatz;
    private int regalfach;

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
    public Article (int artikelID, String artikel_Bezeichnung, int farbe_ID,
                    String farbe_Bezeichnung, int groessen_ID, String fertigungszustand,
                    String menge, String mengenEinheit, int lagerplatz) {
        this.artikelID = artikelID;
        this.artikel_Bezeichnung = artikel_Bezeichnung;
        this.farbe_ID = farbe_ID;
        this.farbe_Bezeichnung = farbe_Bezeichnung;
        this.groessen_ID = groessen_ID;
        this.fertigungszustand = fertigungszustand;
        this.menge = menge;
        this.mengenEinheit = mengenEinheit;
        this.regalfach = getRegalfachFromLagerplatz(lagerplatz);
        this.lagerplatz = formatLagerplatz(lagerplatz);
        this.lagerort = 60;

        // TODO LAGERORT
    }



    /**
     * @return Artikel ID des Artikels
     */
    public int getArtikelID () {
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
    public int getFarbe_ID () {
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
    public int getGroessen_ID () {
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

    public void setLagerplatz(int lagerplatz){
        this.lagerplatz = lagerplatz;
    }

    public int getLagerplatz(){
        return lagerplatz;
    }

    public int getRegalfach() { return regalfach;}

    public int getLagerort () {
        return lagerort;
    }

    private int formatLagerplatz(int lagerplatz){
        String lagerplatzString = String.valueOf(lagerplatz);
        String finalString = lagerplatzString.substring(0,2) + "0" + lagerplatzString.charAt(2);

        return Integer.parseInt(finalString);
    }

    private int getRegalfachFromLagerplatz (int lagerplatz) {
        String lagerplatzString = String.valueOf(lagerplatz);
        String finalString = "" + lagerplatzString.charAt(3);
        return Integer.parseInt(finalString);
    }
}
