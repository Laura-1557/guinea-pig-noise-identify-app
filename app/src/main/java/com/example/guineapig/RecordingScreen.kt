package com.example.guineapig

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    var isRecording by remember {
        mutableStateOf(false)
    }

    var mediaRecorder by remember {
        mutableStateOf<MediaRecorder?>(null)
    }

    var outputFilePath by remember {
        mutableStateOf<String?>(null)
    }

    var mediaPlayer by remember {
        mutableStateOf<MediaPlayer?>(null)
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        // Permission result is handled when Record is pressed again.
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaRecorder?.release()
            mediaPlayer?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopAppBar(
            title = {
                Text("Record a Noise")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (isRecording) {
                    "Recording..."
                } else {
                    "Record a Noise"
                }
            )

            Button(
                onClick = {

                    val permissionAlreadyGranted =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                    if (!permissionAlreadyGranted) {

                        permissionLauncher.launch(
                            Manifest.permission.RECORD_AUDIO
                        )

                    } else if (!isRecording) {

                        val outputFile =
                            context.getExternalFilesDir(null)
                                ?.resolve("guinea_pig_recording.m4a")

                        if (outputFile != null) {

                            outputFilePath = outputFile.absolutePath

                            mediaRecorder = MediaRecorder().apply {

                                setAudioSource(
                                    MediaRecorder.AudioSource.MIC
                                )

                                setOutputFormat(
                                    MediaRecorder.OutputFormat.MPEG_4
                                )

                                setAudioEncoder(
                                    MediaRecorder.AudioEncoder.AAC
                                )

                                setOutputFile(
                                    outputFile.absolutePath
                                )

                                prepare()
                                start()
                            }

                            isRecording = true
                        }

                    } else {

                        mediaRecorder?.stop()
                        mediaRecorder?.release()
                        mediaRecorder = null

                        isRecording = false
                    }
                }
            ) {

                Text(
                    if (isRecording) {
                        "⏹ Stop"
                    } else {
                        "🎙 Record"
                    }
                )
            }

            if (outputFilePath != null && !isRecording) {

                Button(
                    onClick = {

                        if (isPlaying) {

                            mediaPlayer?.pause()
                            isPlaying = false

                        } else {

                            if (mediaPlayer == null) {

                                mediaPlayer = MediaPlayer().apply {

                                    setDataSource(outputFilePath)

                                    prepare()

                                    setOnCompletionListener {
                                        isPlaying = false
                                        seekTo(0)
                                    }
                                }
                            }

                            mediaPlayer?.start()
                            isPlaying = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {

                    Text(
                        if (isPlaying) {
                            "⏸ Pause Recording"
                        } else {
                            "▶ Play Recording"
                        }
                    )
                }

                Text(
                    text = "Recording saved.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}