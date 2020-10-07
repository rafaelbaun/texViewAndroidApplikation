package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Kpi"
 */
public class TableKpi {

    private TableKpi () {
    }


    /**
     * Die Namem sowie die Deklarationen der Spaltennamen werden für die Tabelle
     * "Kpi" in einem String abgebildet
     */
    public static final class KpiEntry implements BaseColumns {

        public static final String TABLE_NAME          = "kpi";
        public static final String COLUMN_NAME         = "name_kpi";
        public static final String COLUMN_CURRENTVALUE = "currentvalue_kpi";
        public static final String COLUMN_MAXVALUE     = "maxvalue_kpi";
        public static final String COLUMN_TIMESTAMP    = "timestamp_kpi";
    }
}