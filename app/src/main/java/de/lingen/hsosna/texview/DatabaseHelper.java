package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerbestand_Summe;
import de.lingen.hsosna.texview.database.TableLagerplaetze;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "lagerverwaltung.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        final String SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE =
                "CREATE TABLE " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME
                + " (" + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG
                + " TEXT, " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_BEZEICHNUNG
                + " TEXT, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN
                + " TEXT, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_VARIANTEN_ID
                + " INTEGER, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_VARIANTEN_BEZEICHNUNG
                + " TEXT, "
                + "PRIMARY KEY("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID + ")" + ");";
        db.execSQL(SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE);

        final String SQL_CREATE_LAGERPLAETZE_TABLE =
                "CREATE TABLE " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + " ("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + " INTEGER NOT NULL, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_BESCHREIBUNG + " TEXT, "
                + "PRIMARY KEY("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + ")" + ");";
        db.execSQL(SQL_CREATE_LAGERPLAETZE_TABLE);


        final String SQL_CREATE_LAGERBESTAND_TABLE =
                "CREATE TABLE " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + " ("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_VARIANTEN_ID + " INTEGER, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGE + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT + " TEXT, "
                + "PRIMARY KEY(" + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + "), "
                //TODO MIT HENNING FREMDSCHLUESSEL
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ") REFERENCES "
                + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + "("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ
                + ") ON DELETE RESTRICT ON UPDATE CASCADE, "
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + ") REFERENCES "
                + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + "("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + ") ON DELETE RESTRICT ON UPDATE CASCADE" + ");";
        db.execSQL(SQL_CREATE_LAGERBESTAND_TABLE);



        final String SQL_CREATE_LAGERBESTAND_SUMME_TABLE =
                "CREATE TABLE " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME + " ("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_VARIANTEN_ID + " INTEGER, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FERTIGUNGSZUSTAND + " TEXT, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGE + " TEXT, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGENEINHEIT + " TEXT, "
                + "PRIMARY KEY(" + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID + "), "
                + "FOREIGN KEY("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ + ") REFERENCES "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME + "("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ
                + ") ON DELETE RESTRICT ON UPDATE CASCADE, "
                + "FOREIGN KEY("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID + ") REFERENCES "
                + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + "("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + ") ON DELETE RESTRICT ON UPDATE CASCADE" + ");";
        db.execSQL(SQL_CREATE_LAGERBESTAND_SUMME_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerbestand.LagerbestandEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME);
        onCreate(db);
    }
}
