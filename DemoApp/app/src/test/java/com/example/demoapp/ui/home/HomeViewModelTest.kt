package com.example.demoapp.ui.home

import com.example.demoapp.data.model.MovieList
import com.example.demoapp.domain.TopRatedMovieUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var useCase: TopRatedMovieUseCase
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk(relaxed = true)
        viewModel = HomeViewModel(useCase, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown(){
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getTopRatedMovies - api returns success`() = runTest {
        val movieListResponse = MovieList(emptyList())
        val response = flow<MovieList> { movieListResponse }
        coEvery { useCase.getTopRatedMovies() }  returns response

        viewModel.getTopRatedMovie()

        run {
            viewModel.homePageUiState.value.apply {
                HomePageUiState.Loading
                HomePageUiState.Success(movieListResponse)
            }
        }
    }

    @Test
    fun `getTopRatedMovies - api returns error`() = runTest {
        val errorResponse = Throwable()
        val response = flow<MovieList> { errorResponse }
        coEvery { useCase.getTopRatedMovies() }  returns response

        viewModel.getTopRatedMovie()

        run {
            viewModel.homePageUiState.value.apply {
                HomePageUiState.Loading
                HomePageUiState.Error(errorResponse)
            }
        }
    }
}