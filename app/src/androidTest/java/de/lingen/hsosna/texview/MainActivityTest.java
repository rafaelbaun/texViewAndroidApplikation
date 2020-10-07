package de.lingen.hsosna.texview;

import android.content.Context;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

public class MainActivityTest {


    /*Order of Execution
     * 1 BeforeClass
     * 2 Rule
     * 3 Before
     * 4 Tests
     * 5 After
     * 6 AfterClass
     */

    public MainActivity mainActivity = null;
    //Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SearchFragment.class.getName(), null, false);


    @BeforeClass
    public static void createMainActivity () {
    }


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule = new IntentsTestRule<>(MainActivity.class);



    /**
     * Es wird ein neues SearchFragment erstellt, um darauf zu testen.
     * Initialisierung
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
                //.replace(R.id.fragment_container, new HomeFragment()).commit();
    }


    /**
     *
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Wechselt zwischen den Fragmenten, welche durch die Menübar aufgerufen werden
     */
    @Test
    public void basicNavigation_withContentDescr () throws Exception {
        Thread.sleep(500);
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("Lageransicht"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("Filter"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("Suche"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("KPI's"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("Info"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withContentDescription("Einstellungen"))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }



    @Test
    public void basicNavigation_withText () throws Exception {
        Thread.sleep(500);
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("Lageransicht"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("Filter"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("Suche"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("KPI's"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("Info"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(withText("Einstellungen"))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }


    @Test
    public void basicNavigation_withIcons () throws Exception {
        Thread.sleep(500);
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // HOME
        onView(withId(R.drawable.ic_home))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Filter
        onView(withId(R.drawable.ic_filter))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Search
        onView(withId(R.drawable.ic_search))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // KPI's
        onView(withId(R.drawable.ic_kpi))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Info
        onView(withId(R.drawable.ic_info))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Settings
        onView(withId(R.drawable.ic_settings))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.nav_view)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }

    @Test
    public void open_close_drawerLayout () {
        onView(withId(R.id.mainActivity)).perform(actionOpenDrawer());
        onView(withId(R.id.mainActivity)).perform(actionCloseDrawer());
    }

    public static ViewAction actionOpenDrawer () {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }
            @Override
            public String getDescription() {
                return "open drawer";
            }
            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };
    }

    public static ViewAction actionCloseDrawer () {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }
            @Override
            public String getDescription() {
                return "close drawer";
            }
            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(GravityCompat.START);
            }
        };
    }






    @Test
    public void test_navgiation_view () throws Exception {
        Thread.sleep(500);

        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_dbcon));
        Thread.sleep(500);

        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_filter));
        Thread.sleep(500);

        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_search));
        Thread.sleep(500);

        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_kpi));
        Thread.sleep(500);
    }




    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.mainActivity = null;
    }

}