package de.lingen.hsosna.texview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lingen.hsosna.texview.fragments.FilterFragment;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

public class DatabaseHelperTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity = null;

    public static final String DATABASE_NAME = "lagerverwaltung.db";
    public static final int DATABASE_VERSION = 2;

    private Context mContext;
    private DatabaseHelper mDbHelper;
    SQLiteDatabase mDatabase;

    /**
     * Es wird ein neues SearchFragment erstellt, um darauf zu testen.
     * Initialisierung
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(DATABASE_NAME);
    }


    /**
     * Die Aktivität wird terminiert.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
        mDatabase.close();
    }


    /**
     *
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    @Test
    public void lagerbestandTableHasAllColumns () {
        //assertThat(listColumnsNames(DATABASE_NAME, "lagerbestand"))
    }

    List<String> listColumnsNames (SQLiteDatabase db, String table) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA lagerbestand(" + table + ");", null);
            List<String> columns = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
                columns.add(cursor.getString(cursor.getColumnIndex("name")));
            }
            cursor.close();
            return columns;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }




    @Test
    public void onCreate() {
    }

    @Test
    public void onUpgrade() {
    }
}