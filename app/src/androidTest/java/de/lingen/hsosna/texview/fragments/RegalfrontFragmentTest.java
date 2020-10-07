package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Checks;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.common.truth.Truth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import de.lingen.hsosna.texview.ArticleAdapter;
import de.lingen.hsosna.texview.Lagerplatz;
import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Checks.checkNotNull;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
// TODO umbenennen: ShelfFrontFragmentTest
public class RegalfrontFragmentTest {

    /*https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/TestUtils.java*/

    public static <VH extends RecyclerView.ViewHolder> ViewAction
    actionOnItemViewAtPosition(int position, @IdRes int viewId, ViewAction viewAction) {
        return new ActionOnItemViewAtPositionViewAction(position, viewId, viewAction);
    }

    private static final class ActionOnItemViewAtPositionViewAction<VH extends RecyclerView.ViewHolder>
            implements ViewAction {
        private final int position;
        private final ViewAction viewAction;
        private final int viewId;

        private ActionOnItemViewAtPositionViewAction(int position, @IdRes int viewId, ViewAction viewAction) {
            this.position = position;
            this.viewAction = viewAction;
            this.viewId = viewId;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "actionOnItemAtPosition performing ViewAction: "
                    + this.viewAction.getDescription()
                    + " on item at position: "
                    + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            (new ScrollToPositionViewAction(this.position)).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();

            View targetView = recyclerView.getChildAt(this.position).findViewById(this.viewId);

            if (targetView == null) {
                throw (new PerformException.Builder()).withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException("No view with id "
                                        + this.viewId
                                        + " found at position: "
                                        + this.position))
                        .build();
            } else {
                this.viewAction.perform(uiController, targetView);
            }
        }
    }

    private static final class ScrollToPositionViewAction
            implements ViewAction {
        private final int position;

        private ScrollToPositionViewAction(int position) {
            this.position = position;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "scroll RecyclerView to position: " + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.scrollToPosition(this.position);
        }
    }



    /*https://gist.github.com/chemouna/00b10369eb1d5b00401b*/
    public static ViewAssertion hasItemsCount (final int count) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException e) {
                if (!(view instanceof RecyclerView)) {
                    throw e;
                }
                RecyclerView rv = (RecyclerView) view;
                Truth.assertThat(rv.getAdapter().getItemCount()).isEqualTo(count);
            }
        };
    }


    public  MainActivity mainActivity = null;

    private ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();
    private CharSequence clickedShelf;
    private ConstraintLayout constraintLayout;

    private final int recyclerViewId;

    public RegalfrontFragmentTest(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }


    @Rule
    public  ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    /**
     * Es wird ein neues RegalfrontFragment erstellt, um darauf zu testen.
     */
    @BeforeClass
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RegalfrontFragment()).commit();
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


    @Test
    public void click_Shelf_should_create_ShelfFrontView () throws Exception {
        onView(withId(R.id.regal_0324)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01))
                .perform(click())
                .check(matches(isDisplayed()));

    }



    @Test
    public void testSelectShelfAndCompartment() {
        onView(withId(R.id.regal_0101)).perform(click());
        onView(withId(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static Matcher<View> atPosition (final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }
            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    /*
    public Matcher<View> atPositionOnView (final int position, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;
            @Override
            public boolean matchesSafely(View view) {
                this.resources = view.getResources();
                if (childView == null) {
                    RecyclerView recyclerView =
                            (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                        childView =  recyclerView.findViewHolderForAdapterPosition(position).itemView;
                    }
                } else {
                    return false;
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view = targetView;
                }
            }
            @Override
            public void describeTo(Description description) {
                String idDescription  = Integer.toString(recyclerViewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException e) {
                        idDescription = String.format("%s (resource name not found)",
                                new Object[]{Integer.valueOf(recyclerViewId)});
                    }
                }
                description.appendText("with id: " + idDescription);
            }
        };
    }

     */

    /*
    @Test
    public void testItemClick () {
        onView(withRecyclerView(R.id.slideUp_recyclerView_compartment01)).atPosition(1)).perform(click());
    }

     */


    @Test
    public void listtItem_isVisible () {
        onView(withId(R.id.regalfrontFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.regalfrontFragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    private void assertChildren (ViewGroup container, Fragment... fragments) {
        final int numFragments = fragments == null ? 0: fragments.length;
        assertEquals("Bla Bla", numFragments, container.getChildCount());
        for (int i = 0; i < numFragments; i++) {
            assertEquals("Wrong for [" + i + "]", container.getChildAt(i), fragments[i].getView());
        }
    }


    @Test(expected = PerformException.class)
    public void itemwithText_doesNotExist () {
        onView(withId(R.id.slideUpPane_shelf_compartment01))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText("not in the list"))));
    }


    @Test
    public void scrollToItemBelowFold_checkItsText () {
        onView(withId(R.id.slideUpPane_shelf_compartment01))
                .perform(RecyclerViewActions.actionOnItemAtPosition(40, click()));
        String itemElementText = getApplicationContext().getResources().getString(
                R.string.itemArticle_placeholder_articleId) + String.valueOf(40);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }


    @Test
    public void itemInMiddleOfList_hasSpecialText () {
        onView(withId(R.id.slideUpPane_shelf_compartment01))
                .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()));
        String middleElementText = getApplicationContext().getResources().getString(
                R.string.itemArticle_placeholder_pieceId);
        onView(withText(middleElementText)).check(matches(isDisplayed()));
    }



    private static Matcher<ArticleAdapter.ExampleViewHolder> isInTheMiddle () {
        return new TypeSafeMatcher<ArticleAdapter.ExampleViewHolder>() {
            @Override
            protected boolean matchesSafely(ArticleAdapter.ExampleViewHolder holder) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item in the middle!");
            }
        };
    }




    /**
     * onCreateView
     * Checkt, ob der Alert Dialog ausgeführt wird
     */
    // TODO alert Dialog
    @Test
    public void alertDialogAfterQuery () {
        // Title
        ViewInteraction dialogLoadingTitle = onView(withId(R.string.fragment_shelf_frontal_loadingTitle))
                .inRoot(isDialog())
                .check(matches(allOf(withText("Regaldaten werden geladen"), isDisplayed())));
        // Message
        ViewInteraction dialogLoadingDescription = onView(withId(R.string.fragment_shelf_frontal_loadingDescription))
                .inRoot(isDialog())
                .check((ViewAssertion) isDialog())
                .check(matches(allOf(withText("Bitte warten..."), isDisplayed())));
    }


    /**
     * Checkt, ob die Regal, die rot markiert werden sollen, auch wirklich rot markiert werden
     */
    @Test
    public void markCompartmentsRed () {
    }


    @Test
    public void markCompartmentsToRed () {
        final ImageView shelve = (ImageView) constraintLayout.findViewById(R.drawable.ic_shelf_marked_red);
        shelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = shelve;
                assert (R.drawable.ic_shelf_marked_red == imageView.getId());
            }
        });
    }


    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

}