package com.example.productcatalog.ui.home

import app.cash.turbine.test
import com.example.productcatalog.data.model.ProductDto
import com.example.productcatalog.data.model.RatingDto
import com.example.productcatalog.domain.repository.ProductRepository
import com.example.productcatalog.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val repository: ProductRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyProduct = ProductDto(
        id = 1,
        title = "Test Product",
        price = 10.0,
        description = "Test Description",
        category = "Test Category",
        image = "https://test.com/image.png",
        rating = RatingDto(rate = 4.5, count = 10)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls getProducts and updates uiState to Success`() = runTest {
        coEvery { repository.getProducts() } returns flowOf(
            Resource.Loading(),
            Resource.Success(listOf(dummyProduct))
        )

        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem() as HomeUiState.Success
            assertEquals(1, state.products.size)
            assertEquals("Test Product", state.products[0].title)
        }
    }

    @Test
    fun `init updates uiState to Empty when repository returns empty list`() = runTest {
        coEvery { repository.getProducts() } returns flowOf(
            Resource.Loading(),
            Resource.Success(emptyList())
        )

        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Empty)
        }
    }

    @Test
    fun `init updates uiState to Error when repository returns error`() = runTest {
        coEvery { repository.getProducts() } returns flowOf(
            Resource.Loading(),
            Resource.Error("Error Message")
        )

        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem() as HomeUiState.Error
            assertEquals("Error Message", state.message)
        }
    }

    @Test
    fun `retry calls getProducts again`() = runTest {
        coEvery { repository.getProducts() } returns flowOf(Resource.Loading())

        viewModel = HomeViewModel(repository)
        
        coEvery { repository.getProducts() } returns flowOf(Resource.Success(listOf(dummyProduct)))
        
        viewModel.retry()

        viewModel.uiState.test {
            val state = awaitItem() as HomeUiState.Success
            assertEquals(1, state.products.size)
        }
    }
}
