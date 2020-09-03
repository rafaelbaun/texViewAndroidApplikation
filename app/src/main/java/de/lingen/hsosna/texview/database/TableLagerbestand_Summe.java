package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Lagerbestand_Summe"
 */
public class TableLagerbestand_Summe {
    private TableLagerbestand_Summe () {
    }

    public static final class Lagerbestand_SummeEntry implements BaseColumns {
        public static final String TABLE_NAME = "lagerbestand_summe";
        public static final String COLUMN_LAGERPLATZ = "lagerplatz_lagerbestand_summe";
        public static final String COLUMN_ARTIKEL_ID = "artikel_id_lagerbestand_summe";
        public static final String COLUMN_GROESSEN_ID = "groessen_id_lagerbestand_summe";
        public static final String COLUMN_FARBE_ID = "farbe_id_lagerbestand_summe";
        public static final String COLUMN_VARIANTEN_ID = "varianten_id_lagerbestand_summe";
        public static final String COLUMN_FERTIGUNGSZUSTAND = "fertigungszustand_lagerbestand_summe";
        public static final String COLUMN_MENGE = "menge_lagerbestand_summe";
        public static final String COLUMN_MENGENEINHEIT = "mengeneinheit_lagerbestand_summe";
    }
}