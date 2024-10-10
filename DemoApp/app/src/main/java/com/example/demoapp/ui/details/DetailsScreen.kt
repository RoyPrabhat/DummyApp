package com.example.demoapp.ui.details

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.R
import com.example.demoapp.data.model.Movie
import com.example.demoapp.ui.common.ShowLoader
import com.example.demoapp.utils.StringUtil

@Composable
fun DetailsScreenComposable(navigateBack: () -> Unit) {
    val viewModel : MovieDetailsViewModel = viewModel()
    val detailsScreenState by viewModel.movieDetailsState.collectAsState(initial = MovieDetailsUiState.Loading)
    Row {
        MovieDetailsComposable(detailsScreenState, navigateBack)
    }
}

@Composable
fun MovieDetailsComposable(state: MovieDetailsUiState, navigateBack: () -> Unit) {
    when(state) {
        is MovieDetailsUiState.Loading -> ShowLoader()
        is MovieDetailsUiState.Error -> ShowErrorScreen()
        is MovieDetailsUiState.Success -> MovieDetails(state.movie, navigateBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetails(state: Movie, navigateBack: () -> Unit) {
    Scaffold(
        topBar ={
            TopAppBar(
                title = {Text(state.title!!)},
                navigationIcon = {
                IconButton(onClick = {navigateBack()}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "", tint = Color.Black)
                }
            })
        }) { paddingValues ->
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(StringUtil.buildImageUrl(state.backdropPath!!)).crossfade(true).build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .wrapContentHeight(),
            )
            Text(text = state.overview!!, modifier = Modifier.padding(16.dp, 16.dp), fontSize = 18.sp)

            Text(text = LocalContext.current.getString(R.string.genres_label), modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                fontWeight = FontWeight.Bold, fontSize = 24.sp)

            Text(text = StringUtil.getGenresList(state.genres!!), modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 16.sp)

            Text(text = LocalContext.current.getString(R.string.release_date), modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
                fontWeight = FontWeight.Bold, fontSize = 24.sp)

            Text(text = state.releaseDate!!, modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 16.sp)

            Text(text = LocalContext.current.getString(R.string.average_rating), modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 0.dp),
                fontWeight = FontWeight.Bold, fontSize = 24.sp)

            Text(text = state.voteAverage!!, modifier = Modifier.padding(16.dp, 0.dp),
                fontSize = 16.sp)

        }

    }
}

@Composable
fun ShowErrorScreen() {
    val context = LocalContext.current
    val viewModel : MovieDetailsViewModel = viewModel()
    Box(modifier = Modifier
        .fillMaxSize()) {
        Button(onClick = { viewModel.fetchMovieDetails() },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = ContextCompat.getString(context, R.string.retry_cta))
        }
        Toast.makeText(context, ContextCompat.getString(context, R.string.error_message), Toast.LENGTH_SHORT).show()
    }
}