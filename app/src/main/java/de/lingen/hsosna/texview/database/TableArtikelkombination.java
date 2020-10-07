package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Artikelkombinationen"
 */
public class TableArtikelkombination {

    private TableArtikelkombination () {
    }


    /**
     * Die Namem sowie die Deklarationen der Spaltennamen werden für die Tabelle
     * "Artikelkombinationen" in einem String abgebildet
     */
    public static final class ArtikelkombinationenEntry implements BaseColumns {

        public static final String TABLE_NAME =
                "artikelkombinationen";
        public static final String COLUMN_ARTIKEL_ID =
                "artikel_id_artikelkombinationen";
        public static final String COLUMN_ARTIKEL_BEZEICHNUNG =
                "artikel_bezeichnung_artikelkombinationen";
        public static final String COLUMN_GROESSEN_ID =
                "groessen_id_artikelkombinationen";
        public static final String COLUMN_GROESSEN_BEZEICHNUNG =
                "groessen_bezeichnung_artikelkombinationen";
        public static final String COLUMN_FARBE_ID =
                "farbe_id_artikelkombinationen";
        public static final String COLUMN_FARBE_BEZEICHNUNGEN =
                "farbe_bezeichnung_artikelkombinationen";
        public static final String COLUMN_VARIANTEN_ID =
                "varianten_id_artikelkombinationen";
        public static final String COLUMN_VARIANTEN_BEZEICHNUNG =
                "varianten_bezeichnung_artikelkombinationen";
    }

}