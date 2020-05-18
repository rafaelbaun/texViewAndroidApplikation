package de.lingen.hsosna.texview;

public class Artikel {
     //Artikelkombinationen
        //ArtikelID
    private String artikelID;
    private String artikel_Bezeichnung;
        //Farbe
    private String farbe_ID;
    private String farbe_Bezeichnung;
        //Groesse
    private String groessen_ID;
        //Fertigungszustand
    private String fertigungszustand;
        //Menge
    private String menge;
    private String mengenEinheit;


    //Lagerbestand
    private int Lagerplatz;
    private int Menge;
    private int Mengeneinheit;


    public Artikel(String artikelID, String artikel_Bezeichnung, String farbe_ID, String farbe_Bezeichnung, String groessen_ID, String fertigungszustand, String menge, String mengenEinheit) {
        this.artikelID = artikelID;
        this.artikel_Bezeichnung = artikel_Bezeichnung;
        this.farbe_ID = farbe_ID;
        this.farbe_Bezeichnung = farbe_Bezeichnung;
        this.groessen_ID = groessen_ID;
        this.fertigungszustand = fertigungszustand;
        this.menge = menge;
        this.mengenEinheit = mengenEinheit;
    }

    public String getArtikelID() {
        return artikelID;
    }

    public String getArtikel_Bezeichnung() {
        return artikel_Bezeichnung;
    }

    public String getFarbe_ID() {
        return farbe_ID;
    }

    public String getFarbe_Bezeichnung() {
        return farbe_Bezeichnung;
    }

    public String getGroessen_ID() {
        return groessen_ID;
    }

    public String getFertigungszustand() {
        return fertigungszustand;
    }

    public String getMenge() {
        return menge;
    }

    public String getMengenEinheit() {
        return mengenEinheit;
    }
}
