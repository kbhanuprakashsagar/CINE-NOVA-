package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cine Nova", appName)
  }

  @Test
  fun `verify film detail essentials for discover movies`() {
    val movies = com.example.data.repository.MovieRepository.movies.value
    assert(movies.isNotEmpty()) { "Movies catalog must not be empty" }
    movies.forEach { movie ->
      assert(movie.synopsis.isNotBlank()) { "Every film must have a description/synopsis: ${movie.title}" }
      assert(movie.director.isNotBlank()) { "Every film must have a director: ${movie.title}" }
      assert(movie.year > 1900) { "Every film must have a valid release year: ${movie.title}" }
    }
  }
}
