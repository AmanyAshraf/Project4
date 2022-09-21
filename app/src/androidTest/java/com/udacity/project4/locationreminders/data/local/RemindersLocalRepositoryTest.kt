package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
@get:Rule
var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository



    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }

    @After
    fun cleanUp() = database.close()

    @Test
    fun testInsertRetrieveData() = runBlocking {



        val data = ReminderDTO(
            "title abc",
            "description abc",
            "location abc",
            77.00,
            77.00)

        repository.saveReminder(data)

        val result = repository.getReminder(data.id)

        result as Result.Success
        assertThat(result.data != null, CoreMatchers.`is`(true))

        val loadedData = result.data
        assertThat(loadedData.id, CoreMatchers.`is`(data.id))
        assertThat(loadedData.title, CoreMatchers.`is`(data.title))
        assertThat(loadedData.description, CoreMatchers.`is`(data.description))
        assertThat(loadedData.location, CoreMatchers.`is`(data.location))
        assertThat(loadedData.latitude, CoreMatchers.`is`(data.latitude))
        assertThat(loadedData.longitude, CoreMatchers.`is`(data.longitude))
    }


    @Test
    fun testDataNotFound_returnError() = runBlocking {
        var reminder = ReminderDTO("title", "description", "street_10", 31.039908275708083, 31.390473840481295)
        repository.saveReminder(reminder)
        repository.deleteAllReminders()

        val result = repository.getReminder(reminder.id)
        assertThat(result is Result.Error, `is`(true))
        result as Result.Error
        assertThat(result.message, `is`("Reminder not found!"))
    }

    @Test
    fun deleteReminders_EmptyList()= runBlocking {
        val reminder = ReminderDTO("My Shop", "Get to the Shop", "Abuja", 6.54545, 7.54545)
        repository.saveReminder(reminder)

        repository.deleteAllReminders()

        val result = repository.getReminders()

        assertThat(result is Result.Success, `is`(true))
        result as Result.Success

        assertThat(result.data, `is` (emptyList()))
    }
}