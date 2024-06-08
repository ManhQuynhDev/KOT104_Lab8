package com.quynhlm.dev.lab8

import MovieViewModel
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.quynhlm.dev.lab8.Model.Movie
import com.quynhlm.dev.lab8.ui.theme.Lab8Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieViewModel: MovieViewModel by viewModels()
        setContent {
            Lab8Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        "Movies",
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                },
                            )
                        }
                    ) { innerPadding ->
                        MovieListGrid(innerPadding , movieViewModel = movieViewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun MovieListGrid(innerPaddingValues: PaddingValues,movieViewModel: MovieViewModel){
    val moviesState by movieViewModel.productState
    var openAlertDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        movieViewModel.getAllProduct()
    }
    val gridState = rememberLazyStaggeredGridState()
    if (moviesState != null) {
        Column (modifier = Modifier.padding(start = 5.dp,innerPaddingValues.calculateTopPadding(),end = 5.dp)) {
            Button(onClick = {
                openAlertDialog = true
            }) {
                Text(text = "Add")
            }
            if(openAlertDialog == true){
                MovieForm(onDismissRequest = { openAlertDialog = false }, movie = null, type = 0 , movieViewModel = movieViewModel)
            }
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                contentPadding = PaddingValues(8.dp),
            ) {
                items(moviesState!!) { movie ->
                    MovieItem(movie = movie , movieViewModel = movieViewModel)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie : Movie? , movieViewModel: MovieViewModel) {
    var openAlertDialog by remember { mutableStateOf(false) }
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ){
        Column (modifier = Modifier
            .width(175.dp)
            .height(400.dp)){
                AsyncImage(
                    model = movie!!.image,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .height(255.dp)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topEnd = 8.dp, topStart =
                                8.dp
                            )
                        ),
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = movie!!.filmname, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                    BoldValueText(label = "Thời lượng: ", value = "${movie!!.duration}")
                    BoldValueText(label = "Khởi chiếu: ", value = movie!!.releaseDate)
                }
            Button(onClick = {
                openAlertDialog = true
            }) {
                Text(text = "Update")
            }

            if(openAlertDialog == true){
                MovieForm(onDismissRequest = { openAlertDialog = false }, movie = movie, type = 1 , movieViewModel = movieViewModel)
            }
        }
    }
}
@Composable
fun MovieForm(onDismissRequest: () -> Unit , movie: Movie? , type : Int , movieViewModel: MovieViewModel) {

    val registerState by movieViewModel.registerState

    val context = LocalContext.current

    var name by remember {
        mutableStateOf(movie?.filmname ?:"")
    }
    var duration by remember {
        mutableStateOf(movie?.duration ?:"")
    }
    var date by remember {
        mutableStateOf(movie?.releaseDate ?:"")
    }
    var descibe by remember {
        mutableStateOf(movie?.description ?:"")
    }
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = if (type == 0) "Add Movie" else "Edit Movie",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(value = name, onValueChange = {name = it} , placeholder = { Text(text = "Movie Name")})
                OutlinedTextField(value = duration, onValueChange = {duration = it} , placeholder = { Text(text = "Duration")})
                OutlinedTextField(value = date, onValueChange = {date = it} , placeholder = { Text(text = "ReleaseDate")})
                OutlinedTextField(value = descibe, onValueChange = {descibe = it} , placeholder = { Text(text = "Description")})

                Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (type == 0) {
                                val newMovie = Movie(
                                    name,
                                    duration,
                                    date,
                                    "Action, Crime, Drama",
                                    "VN",
                                    date,
                                    descibe,
                                    "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/hinh-dep-5.jpg"
                                )
                                movieViewModel.addMovie(newMovie)
                            }else{
                                movie!!.filmname = name
                                movie!!.duration = duration
                                movie!!.releaseDate = date
                                movie!!.description = descibe

                                movieViewModel.updateMovie(movie.filmid!! , movie = movie)
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }
                registerState?.let {
                    Log.e("TAG", "RegisterScreen:" + it )
                    if (it == true) {
                        showMessage(context, "Successfully")
                        onDismissRequest()
                    } else {
                        showMessage(context, "Not Successfully")
                    }
                }
            }
        }
    }
}

fun showMessage(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun BoldValueText(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)) {
                append(label)
            }
            withStyle(style = SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)) {
                append(value)
            }
        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab8Theme {
        Greeting("Android")
    }
}