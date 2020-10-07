package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Kpi"
 */
public class TableTimestamp {

    private TableTimestamp () {
    }


    /**
     * Die Namem sowie die Deklarationen der Spaltennamen werden für die Tabelle
     * "Timestamp" in einem String abgebildet
     */
    public static final class TimestampEntry implements BaseColumns {

        public static final String TABLE_NAME       = "timestamp";
        public static final String COLUMN_TIMESTAMP = "timestamp_timestamp";
    }

}