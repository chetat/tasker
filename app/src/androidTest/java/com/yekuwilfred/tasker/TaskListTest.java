package com.yekuwilfred.tasker;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class TaskListTest {
    @Rule
    public ActivityTestRule<TasksListActivity> mTasksListTestRule =
            new ActivityTestRule<TasksListActivity>(TasksListActivity.class);

    @Test
    public void clickAddTaskButton() throws Exception{
        onView(withId(R.id.add_task_fab)).perform(click());
        onView(withId(R.id.task_title_field))
            .check(matches(isDisplayed()));
    }
}
