package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects
    private lateinit var fakeReminderDataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel


    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setupViewModel() {
        fakeReminderDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeReminderDataSource)
    }

    @Test
    fun shouldReturnError () = runBlockingTest  {
        val result = saveReminderViewModel.validateEnteredData(createIncompleteReminderDataItem())
        MatcherAssert.assertThat(result, CoreMatchers.`is`(false))
    }

    private fun createIncompleteReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "",
            "description abc",
            "location abc",
            77.00,
            77.00)
    }

    @Test
    fun check_loading() = runBlockingTest {

        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(createFakeReminderDataItem())

        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value, CoreMatchers.`is`(true))

        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(saveReminderViewModel.showLoading.value, CoreMatchers.`is`(false))
    }

    private fun createFakeReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "title abc",
            "description abc",
            "location abc",
            77.00,
            77.00)
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}