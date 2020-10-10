package de.lingen.hsosna.texview.fragments;

import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;
import de.lingen.hsosna.texview.database.TableLagerbestand;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SettingsFragmentTest {

    private SQLiteDatabase mDatabase;
    //DatabaseHelper dbHelper = new DatabaseHelper();

    public int getArticleCount () {
        String countQuery = "SELECT * FROM " + TableLagerbestand.LagerbestandEntry.TABLE_NAME;
        SQLiteOpenHelper dbHelper;
        //mDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = mDatabase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    @Test
    public void insert_rawQuery () throws Exception {
        String articleId = "79961";
        String colorId = "8506";
        ContentValues cv = new ContentValues();
        cv.put(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID, articleId);
        cv.put(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID, colorId);
        mDatabase.insert(TableLagerbestand.LagerbestandEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDatabase.rawQuery("SELECT "
                + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID + ", "
                + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID + " FROM "
                + TableLagerbestand.LagerbestandEntry.TABLE_NAME,
                null);
        //assertThat(cursor.moveToFirst()).isTrue();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity = null;

    Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
            .addMonitor(SettingsFragment.class.getName(), null, false);


    /**
     * Es wird ein neues SettingsFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment()).commit();
    }



    /**
     * Überprüft, ob der richtige Context der App ausgewählt worden ist
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Das SettingsFragment wird aufgerufen.
     */
    @Test
    public void open_settings () throws Exception {
        onView(withContentDescription(R.string.app_description))
                .perform(DrawerActions.open());
        onView(allOf(withId(R.id.nav_settings)))
                .perform(click());
        assertTrue(mActivityTestRule.getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) instanceof SettingsFragment);
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}