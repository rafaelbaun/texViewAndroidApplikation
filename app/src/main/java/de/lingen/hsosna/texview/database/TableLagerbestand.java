package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Lagerbestand"
 */
public class TableLagerbestand {
    private TableLagerbestand () {
    }

    public static final class LagerbestandEntry implements BaseColumns {
        public static final String TABLE_NAME = "lagerbestand";
        public static final String COLUMN_LAGERPLATZ = "lagerplatz_lagerbestand";
        public static final String COLUMN_STUECKNUMMER = "stuecknummer_lagerbestand";
        public static final String COLUMN_STUECKTEILUNG = "stueckteilung_lagerbestand";
        public static final String COLUMN_ARTIKEL_ID = "artikel_id_lagerbestand";
        public static final String COLUMN_GROESSEN_ID = "groessen_id_lagerbestand";
        public static final String COLUMN_FARBE_ID = "farbe_id_lagerbestand";
        public static final String COLUMN_VARIANTEN_ID = "varianten_id_lagerbestand";
        public static final String COLUMN_FERTIGUNGSZUSTAND = "fertigungszustand_lagerbestand";
        public static final String COLUMN_MENGE = "menge_lagerbestand";
        public static final String COLUMN_MENGENEINHEIT = "mengeneinheit_lagerbestand";
    }
}