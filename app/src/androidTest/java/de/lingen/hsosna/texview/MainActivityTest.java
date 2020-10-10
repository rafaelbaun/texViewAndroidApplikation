package de.lingen.hsosna.texview;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import de.lingen.hsosna.texview.Login.Login;
import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Für die MainActivity werden UI-Tests geschrieben.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public MainActivity mainActivity = null;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);



    /**
     * Es wird ein Intent initialisiert, um den Test vorzubereiten.
     */
    @Before
    public void setUp() throws Exception {
        Intents.init();
    }


    /**
     * Überprüft, ob der richtige Context der App ausgewählt worden ist
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Das Menü wird geöffnet.
     */
    @Test
    public void open_menu () throws  Exception{
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
    }


    /**
     * Das Menü wird geöffnet und es wird der Hauptbildschirm durch Klicken auf die Titelfläche
     * aufgerufen.
     */
    @Test
    public void open_menu_show_home () throws  Exception{
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Nach Aufruf der Menüleiste werden die Menü-Items ausgewählt, um zwischen den
     * Fragmenten zu wechseln.
     */
    @Test
    public void navigation () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);

        onView(withText("Lageransicht"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Filter"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Suche"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("KPI's"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Info"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Einstellungen"))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Nach Aufruf der Menüleiste werden die MenuItems ausgewählt, um zwischen den
     * Fragmenten zu wechseln. Zwischendurch wird der Hauptbildschirm durch Klicken auf die
     * Titelfläche aufgerufen.
     */
    @Test
    public void navigation_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);

        onView(withText("Lageransicht"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Filter"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Suche"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("KPI's"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Info"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Einstellungen"))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Die Menüleiste wird geöffnet und auf "Ausloggen" geklickt. Es wird überprüft, ob die neue
     * Aktivität geladen wird und man zum Login gelangt.
     */
    @Test
    public void logout_should_close () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);

        onView(withText("Ausloggen"))
                .perform(click());
        Thread.sleep(1000);

        // Überprüfung
        Intent resultData = new Intent();
        resultData.putExtra("Login", "MainActivity");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(toPackage(Login.class.getName())).respondWith(result);
        intending(hasComponent(Login.class.getName()))
                .respondWith(new Instrumentation.ActivityResult(0, null));
    }


    /**
     * Es wird auf ein Regal geklickt, um in die Regalfrontansicht zu wechseln.
     */
    @Test
    public void select_shelf () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101)).perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Danach wird die
     * Menüleiste geöffnet und zur Hauptansicht gewechselt.
     */
    @Test
    public void select_shelf_show_menu () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withText("Lageransicht"))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Es wird in der Toolbar auf die Titelfläche geglickt, um auf die Hauptansicht zu wechseln.
     */
    @Test
    public void show_home () throws Exception {
        Thread.sleep(1000);
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Danach wird auf ein
     * Regalfach geglickt, um den Inhalt einzusehen.
     */
    @Test
    public void select_shelf_select_compartment () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02_focused))
                .perform(click());
        Thread.sleep(1000);
    }

    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Danach wird auf ein
     * Regalfach geglickt. Es wird kein Inhalt dargestellt.
     */
    @Test
    public void select_shelf_select_compartment_empty () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Danach wird auf ein
     * Regalfach geglickt, um den Inhalt einzusehen. Zum Schluss wird durch Klicken auf die
     * Titelfläche der Hauptbildschirm aufgerufen.
     */
    @Test
    public void select_shelf_select_compartment_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02_focused))
                .perform(click());
        Thread.sleep(1000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geklickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geklickt. Danach wird auf ein freies Regalfach
     * geklickt. Es wird kein Fachinhalt dargestellt.
     */
    @Test
    public void select_shelf_switch_color_select_compartment () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        // select compartment
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geklickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geklickt. Danach wird auf ein freies Regalfach
     * geklickt. Es wird kein Fachinhalt dargestellt. Zum Schluss wird die farbliche Markierung der
     * freien Fächer rückgängig gemacht.
     */
    @Test
    public void select_shelf_switch_color_select_compartment_switch_back () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        // select compartment
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geglickt. Danach wird auf ein freies Regalfach
     * geglickt. Es wird kein Fachinhalt dargestellt. Zum Schluss wird durch Klicken auf die
     * Titelfläche der Hauptbildschirm aufgerufen.
     */
    @Test
    public void select_shelf_switch_color_select_compartment_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);
        // switch color
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
        // select compartment
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal geklickt, um in die Regalfrontansicht zu wechseln. Danach werden
     * mehrere Regalfächer  ausgewählt, um den Inhalt einizusehen. Teilweise wird kein Fachinhalt
     * dargestellt.
     */
    @Test
    public void select_shelf_pass_compartments () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(2000);

        // pass compartments
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf ein Regal gegklickt, um in die Regalfrontansicht zu wechseln. Danach werden
     * mehrere Regalfächer  ausgewählt, um den Inhalt einizusehen. Teilweise wird kein Fachinhalt
     * dargestellt. Zum Schluss wird durch Klicken auf die Titelfläche der Hauptbildschirm
     * aufgerufen.
     */
    @Test
    public void select_shelf_pass_compartments_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(2000);

        // pass compartments
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07))
                .perform(click());
        Thread.sleep(2000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }



    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geglickt. Danach werden mehrere Regalfächer
     * ausgewählt, um den Inhalt einzusehen. Teilweise wird kein Fachinhalt dargestellt.
     */
    @Test
    public void select_shelf_switch_color_pass_compartments () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));

        // pass compartments
        Thread.sleep(2000);
        // pass compartments
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geglickt. Danach werden mehrere Regalfächer
     * ausgewählt, um den Inhalt einzusehen. Teilweise wird kein Fachinhalt dargestellt. Zum
     * Schluss wird die farbliche Markierung der freien Fächer rückgägngig gemacht.
     */
    @Test
    public void select_shelf_switch_color_pass_compartments_switch_back () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));

        // pass compartments
        Thread.sleep(2000);
        // pass compartments
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07))
                .perform(click());

        // switch color
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
    }


    /**
     * Es wird auf ein Regal geglickt, um in die Regalfrontansicht zu wechseln. Um freie Fächer
     * einsehen zu können, wird das Switch-Color-Item geglickt. Danach werden
     * mehrere Regalfächer  ausgewählt, um den Inhalt einizusehen. Teilweise wird kein Fachinhalt
     * dargestellt. Zum Schluss wird durch Klicken auf die Titelfläche der Hauptbildschirm
     * aufgerufen.
     */
    @Test
    public void select_shelf_switch_color_pass_compartments_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_0101))
                .perform(click());
        Thread.sleep(1000);

        // switch color
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        // pass compartments
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07))
                .perform(click());
        Thread.sleep(2000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }



    /**
     * In der Toolbar wird das Switch-Color-Item angeklickt. Dadurch werden die Regale farblich nach
     * Füllungsgrad markiert.
     */
    @Test
    public void switch_color () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }


    /**
     * In der Toolbar wird das Switch-Color-Item angewählt. Dadurch werden die Regale farblich nach
     * Füllungsgrad markiert. Im Anschluss wird der Hauptbildschirm durch Klicken auf die Titelfläche
     * geladen.
     */
    @Test
    public void switch_color_show_Home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(1000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * In der Toolbar wird das Switch-Color-Item angewählt. Dadurch werden die Regale farblich nach
     * Füllungsgrad markiert. Im Anschluss wird der Switch rückgängig gemacht, sodass alle Regale
     * wieder die normale Farbe bekommen.
     */
    @Test
    public void switch_color_switch_back () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }


    /**
     * In der Toolbar wird das Switch-Color-Item angewählt. Dadurch werden die Regale farblich nach
     * Füllungsgrad markiert. Im Anschluss wird der Hauptbildschirm durch Klicken auf die Titelfläche
     * geladen. Zum Schluss wird der Switch rückgängig gemacht, sodass alle Regale wieder die
     * normale Farbe bekommen.
     */
    @Test
    public void switch_color_show_home_switch_back () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }


    /**
     * Es wird eine Filterung vorgenommen. Danach wird in der Toolbar das Switch-Color-Item
     * ausgewählt.
     */
    @Test
    public void filter_switch_color () throws Exception {
        Thread.sleep(500);
        // Filter
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("gaby"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());

        // Switch Color
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
    }


    /**
     * Es wird eine Filterung vorgenommen. Danach wird in der Toolbar das Switch-Color-Item
     * ausgewählt und zum Schluss kehrt man durch Klicken auf die Titelfläche zum Hauptbildschirm
     * zurück.
     */
    @Test
    public void filter_switch_color_show_home () throws Exception {
        Thread.sleep(500);
        // Filter
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("gaby"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());

        // Switch Color
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Es wird eine Suche vorgenommen. Danach wird in der Toolbar das Switch-Color-Item
     * ausgewählt.
     */
    @Test
    public void search_switch_color () throws Exception {
        Thread.sleep(500);
        // Suche
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());

        // Switch Color
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);
    }


    /**
     * Es wird eine Suche vorgenommen. Danach wird in der Toolbar das Switch-Color-Item
     * ausgewählt und zum Schluss kehrt man durch Klicken auf die Titelfläche zum Hauptbildschirm
     * zurück.
     */
    @Test
    public void search_switch_color_show_home () throws Exception {
        Thread.sleep(500);
        // Suche
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());

        // switch Color
        Thread.sleep(2000);
        onView(withId(R.id.switchColor))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(2000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Es wird auf das temporäre Lager geklickt.
     */
    @Test
    public void select_temporary_shelf () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_01x3)) // regalTemp
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird auf das temporäre Lager geklickt. Danach wird auf die Titelfläche geklickt, um auf
     * den Hauptbildschirm zu gelangen.
     */
    @Test
    public void select_temporary_shelf_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.regal_01x3)) // regalTemp
                .perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.mainActivity = null;
        Intents.release();
    }

}