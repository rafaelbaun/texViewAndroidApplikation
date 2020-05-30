package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerplaetze;

public class GroceryDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "grocerylist.db";
    public static final int DATABASE_VERSION = 14;

    public GroceryDBHelper (@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        final String SQL_CREATE_GROCERYLIST_TABLE =
                "CREATE TABLE " + GroceryContract.GroceryEntry.TABLE_NAME + " ("
                + GroceryContract.GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroceryContract.GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + GroceryContract.GroceryEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, "
                + GroceryContract.GroceryEntry.COLUMN_TIMESTAMP
                + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        db.execSQL(SQL_CREATE_GROCERYLIST_TABLE);
        final String SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE =
                "CREATE TABLE " + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME
                + " (" + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG
                + " TEXT, " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSE_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + " INTEGER NOT NULL, "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_BEZEICHNUNGEN
                + " TEXT, "
                + "PRIMARY KEY("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSE_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID + ")" + ");";
        db.execSQL(SQL_CREATE_ARTIKELKOMBINATIONEN_TABLE);
        final String SQL_CREATE_LAGERBESTAND_TABLE =
                "CREATE TABLE " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + " ("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + " INTEGER NOT NULL, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGE + " TEXT, "
                + TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT + " TEXT, "
                + "PRIMARY KEY(" + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + "), "
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + ") REFERENCES "
                + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + "("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ
                + ") ON DELETE RESTRICT ON UPDATE CASCADE, "
                + "FOREIGN KEY("
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSE_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + ") REFERENCES "
                + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME + "("
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSE_ID + ", "
                + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID
                + ") ON DELETE RESTRICT ON UPDATE CASCADE" + ");";
        db.execSQL(SQL_CREATE_LAGERBESTAND_TABLE);
        final String SQL_CREATE_LAGERPLAETZE_TABLE =
                "CREATE TABLE " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + " ("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + " INTEGER NOT NULL, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_REGAL_NR + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_ZEILE + " INTEGER, "
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_SPALTE + " INTEGER, " + "PRIMARY KEY("
                + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + ")" + ");";
        db.execSQL(SQL_CREATE_LAGERPLAETZE_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryContract.GroceryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                   + TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerbestand.LagerbestandEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME);
        onCreate(db);
    }
}
