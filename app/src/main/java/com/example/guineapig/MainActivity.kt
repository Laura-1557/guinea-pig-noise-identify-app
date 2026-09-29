package com.example.guineapig

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.compose.material3.Button
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

data class GuineaPigNoise(
    val name: String,
    val description: String,
    val example: String
)

val guineaPigNoises = listOf(
    GuineaPigNoise(
        name = "Wheeking",
        description = "A loud, repeated squeaking sound. Guinea pigs often make this noise when they are excited or expecting something enjoyable.",
        example = "Your guinea pig might wheek when it hears a food bag being opened."
    ),
    GuineaPigNoise(
        name = "Purring",
        description = "A low vibrating sound. Depending on the situation, this can indicate relaxation, but some types of purring can also indicate discomfort or irritation.",
        example = "A relaxed guinea pig may make a soft purring sound while being gently stroked."
    ),
    GuineaPigNoise(
        name = "Chutting",
        description = "A series of quiet, short sounds that can occur when a guinea pig is exploring or moving around.",
        example = "You may hear chutting while your guinea pig explores its enclosure."
    ),
    GuineaPigNoise(
        name = "Chirping",
        description = "A relatively unusual bird-like sound. The reason guinea pigs chirp is not fully understood.",
        example = "A guinea pig may suddenly produce a quiet, bird-like chirping sound."
    ),
    GuineaPigNoise(
        name = "Teeth Chattering",
        description = "A rapid clicking or chattering sound made with the teeth. It can be a warning that the guinea pig is annoyed, frightened or becoming aggressive.",
        example = "Two guinea pigs may chatter their teeth when they are unhappy with each other."
    ),
    GuineaPigNoise(
        name = "Squeaking",
        description = "A short, high-pitched squeak. The meaning can depend heavily on what is happening around the guinea pig.",
        example = "A guinea pig might squeak when startled or when it wants attention."
    )
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                GuineaPigApp()
            }
        }
    }
}

@Composable
fun GuineaPigApp() {

    var selectedNoise by remember {
        mutableStateOf<GuineaPigNoise?>(null)
    }

    if (selectedNoise == null) {

        HomeScreen(
            noises = guineaPigNoises,
            onNoiseSelected = { noise ->
                selectedNoise = noise
            }
        )

    } else {

        InformationScreen(
            noise = selectedNoise!!,
            onBack = {
                selectedNoise = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    noises: List<GuineaPigNoise>,
    onNoiseSelected: (GuineaPigNoise) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("GuineaPig1.0")
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(noises) { noise ->

                NoiseListItem(
                    noise = noise,
                    onClick = {
                        onNoiseSelected(noise)
                    }
                )
            }
        }
    }
}

@Composable
fun NoiseListItem(
    noise: GuineaPigNoise,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = noise.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Tap to learn more",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    noise: GuineaPigNoise,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {

            TopAppBar(

                title = {
                    Text(noise.name)
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {

            Text(
                text = noise.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "What might it mean?",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = noise.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Example",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = noise.example,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Example sound",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            val context = LocalContext.current
            var mediaPlayer by remember {
                mutableStateOf<MediaPlayer?>(null)
            }

            var isPlaying by remember {
                mutableStateOf(false)
            }

            Button(
                onClick = {

                    if (isPlaying) {

                        mediaPlayer?.pause()
                        isPlaying = false

                    } else {

                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(
                                context,
                                R.raw.wheeking
                            )
                        }

                        mediaPlayer?.start()
                        isPlaying = true
                    }
                }
            ) {

                Text(
                    if (isPlaying) {
                        "⏸ Pause wheeking"
                    } else {
                        "▶ Play wheeking"
                    }
                )
            }

            Text(
                text = "🔊 No recording available yet.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
