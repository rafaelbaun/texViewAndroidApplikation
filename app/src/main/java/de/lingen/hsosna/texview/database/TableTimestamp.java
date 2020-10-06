package de.lingen.hsosna.texview.database;

import android.provider.BaseColumns;

/**
 * Deklaration der Spaltennamen für die Tabelle "Kpi"
 */
public class TableTimestamp {
    private TableTimestamp () {
    }

    public static final class TimestampEntry implements BaseColumns {
        public static final String TABLE_NAME = "timestamp";
        public static final String COLUMN_TIMESTAMP = "timestamp_timestamp";
    }
}