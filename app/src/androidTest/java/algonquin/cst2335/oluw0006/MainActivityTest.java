package algonquin.cst2335.oluw0006;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFindMissingUpperCase(){

        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.et));
        //type in password123#$*
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.btn));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.tv));
        //check the text
        textView.check(matches(withText("You Shall Not Pass!")));
    }

    @Test
    public void testFindMissingNumber(){

        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.et));
        //type in password#$*
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.btn));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.tv));
        //check the text
        textView.check(matches(withText("You Shall Not Pass!")));
    }

    @Test
    public void testFindMissingLowerCase(){

        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.et));
        //type in PASSWORD123#$*
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.btn));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.tv));
        //check the text
        textView.check(matches(withText("You Shall Not Pass!")));
    }

    @Test
    public void testFindMissingSpecialCharacter(){

        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.et));
        //type in pAssword123
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.btn));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.tv));
        //check the text
        textView.check(matches(withText("You Shall Not Pass!")));
    }

    @Test
    public void testSuccessful(){

        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.et));
        //type in pAssword123
        appCompatEditText.perform(replaceText("Password123#$*"));

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.btn));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.tv));
        //check the text
        textView.check(matches(withText("Your password is complex enough")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }



}
