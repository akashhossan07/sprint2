package com.example.sprint1;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UICaptionSearchTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestFilter() {
        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(clearText());
        onView(withId(R.id.etFromDateTime)).perform(typeText("2008:10:01 23:11"), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(clearText());
        onView(withId(R.id.etToDateTime)).perform(typeText("2021:10:01 23:45"), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("BCIT"), closeSoftKeyboard());

        onView(withId(R.id.fromLat)).perform(clearText());
        onView(withId(R.id.fromLat)).perform(typeText("46"),closeSoftKeyboard());
        onView(withId(R.id.toLat)).perform(clearText());
        onView(withId(R.id.toLat)).perform(typeText("50"),closeSoftKeyboard());
        onView(withId(R.id.fromLon)).perform(clearText());
        onView(withId(R.id.fromLon)).perform(typeText("-150.0"),closeSoftKeyboard());
        onView(withId(R.id.toLon)).perform(clearText());
       onView(withId(R.id.toLon)).perform(typeText("0"),closeSoftKeyboard());

        onView(withId(R.id.go)).perform(click());
        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }

        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnLeft)).perform(click());
        }
    }
}
