package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Lagerplaetze"
 */
public class TableLagerplaetze {

    private TableLagerplaetze () {
    }


    /**
     * Die Namem sowie die Deklarationen der Spaltennamen werden für die Tabelle
     * "Artikelkombinationen" in einem String abgebildet
     */
    public static final class LagerplaetzeEntry implements BaseColumns {

        public static final String TABLE_NAME          = "lagerplaetze";
        public static final String COLUMN_LAGERORT     = "lagerort_lagerplaetze";
        public static final String COLUMN_LAGERPLATZ   = "lagerplatz_lagerplaetze";
        public static final String COLUMN_REGAL_NR     = "regal_nr_lagerplaetze";
        public static final String COLUMN_ZEILE        = "zeile_lagerplaetze";
        public static final String COLUMN_SPALTE       = "spalte_lagerplaetze";
        public static final String COLUMN_BESCHREIBUNG = "beschreibung_lagerplaetze";
    }

}