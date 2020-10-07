package de.lingen.hsosna.texview;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableKpi;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerbestand_Summe;
import de.lingen.hsosna.texview.database.TableLagerplaetze;
import de.lingen.hsosna.texview.database.TableTimestamp;

/**
 * DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lagerverwaltung.db";
    public static final int DATABASE_VERSION = 3;

    public DatabaseHelper (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Mittels eine SQLiteDatabse werden die Tabellen Artikelkombinationen, Lagerplaetze, Lagerbestand
     * und Lagerbestand_Summe via SQL-Befehlen erstellt.
     *
     * @param db
     */
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        // ARTIKELKOMBINATIONEN
        final String SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE =
                "CREATE TABLE " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + " ("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG
                + " TEXT, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID
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
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID          + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID         + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID + ")" + ");";
        db.execSQL(SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE);

        // LAGERPLAETZE
        final String SQL_CREATE_LAGERPLAETZE_TABLE =
                "CREATE TABLE " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + " ("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT     + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ   + " INTEGER NOT NULL, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR     + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE        + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE       + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_BESCHREIBUNG + " TEXT, "
                + "PRIMARY KEY("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + ")" + ");";
        db.execSQL(SQL_CREATE_LAGERPLAETZE_TABLE);

        // LAGERBESTAND
        final String SQL_CREATE_LAGERBESTAND_TABLE =
                "CREATE TABLE " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + " ("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ        + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER      + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG     + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID        + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID       + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID          + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_VARIANTEN_ID      + " INTEGER, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGE             + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT     + " TEXT, "
                + "PRIMARY KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ    + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER  + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID    + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID   + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID      + "), "
                //TODO MIT HENNING FREMDSCHLUESSEL
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ") REFERENCES "
                + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + "("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ
                + ") ON DELETE RESTRICT ON UPDATE CASCADE, "
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID  + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID    + ") REFERENCES "
                + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + "("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID  + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + ") ON DELETE RESTRICT ON UPDATE CASCADE" + ");";
        db.execSQL(SQL_CREATE_LAGERBESTAND_TABLE);

        // LAGERBESTAND_SUMME
        final String SQL_CREATE_LAGERBESTAND_SUMME_TABLE =
                "CREATE TABLE " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME + " ("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ        + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID        + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID       + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID          + " INTEGER NOT NULL, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_VARIANTEN_ID      + " INTEGER, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FERTIGUNGSZUSTAND + " TEXT, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGE             + " TEXT, "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGENEINHEIT     + " TEXT, "
                + "PRIMARY KEY("
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ  + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID  + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID + ", "
                + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID    + "), "
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

        // KPI
        final String SQL_CREATE_KPI_TABLE =
            "CREATE TABLE " + TableKpi.KpiEntry.TABLE_NAME + " ("
            + TableKpi.KpiEntry.COLUMN_NAME         + " TEXT NOT NULL, "
            + TableKpi.KpiEntry.COLUMN_CURRENTVALUE + " INTEGER NOT NULL, "
            + TableKpi.KpiEntry.COLUMN_MAXVALUE + " INTEGER NOT NULL, "
            + TableKpi.KpiEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL, "
            + "PRIMARY KEY(" + TableKpi.KpiEntry.COLUMN_NAME + ", "
            + TableKpi.KpiEntry.COLUMN_CURRENTVALUE + ", "
            + TableKpi.KpiEntry.COLUMN_MAXVALUE     + ", "
            + TableKpi.KpiEntry.COLUMN_TIMESTAMP + "));";
        db.execSQL(SQL_CREATE_KPI_TABLE);

        // TIMESTAMP
        final String SQL_CREATE_TIMESTAMP_TABLE =
                "CREATE TABLE " + TableTimestamp.TimestampEntry.TABLE_NAME + " ("
                + TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, "
                + "PRIMARY KEY(" + TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP + "));";
        db.execSQL(SQL_CREATE_TIMESTAMP_TABLE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP, 0);
        db.insert(TableTimestamp.TimestampEntry.TABLE_NAME, null, contentValues);

    }


    /**
     * Alle Relationen der Datenbank werden gelöscht und im Anschluss wieder neu erstellt.
     *
     * @param db lagerverwaltung.db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerbestand.LagerbestandEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableKpi.KpiEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableTimestamp.TimestampEntry.TABLE_NAME);

        onCreate(db);
    }
}
