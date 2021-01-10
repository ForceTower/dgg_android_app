package dev.forcetower.destiny.view.stream

import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.AppOpsManagerCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.destiny.R
import dev.forcetower.destiny.core.service.TwitchService
import dev.forcetower.destiny.core.task.GetLiveStreamTask
import dev.forcetower.destiny.databinding.ActivityStreamBinding
import dev.forcetower.toolkit.components.BaseActivity
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@AndroidEntryPoint
class StreamActivity : BaseActivity() {
    private lateinit var binding: ActivityStreamBinding

    private var isInPipMode = false
    private lateinit var player: SimpleExoPlayer
    private var isPIPModeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stream)

        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        binding.playerView.controllerAutoShow = false
        binding.playerView.controllerHideOnTouch = false
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            val client = OkHttpClient.Builder()
                .build()

            val service = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.twitch.tv/api/")
                .build()
                .create(TwitchService::class.java)

            val task = GetLiveStreamTask(service, "destiny")
            val result = task.execute()
            Timber.d("Result from task $result")

            val dataSourceFactory = DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    this@StreamActivity,
                    "Twitch"
                )
            )
            val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(result.toUri())
            player.playWhenReady = true
            player.prepare(mediaSource)
        }

        val mediaSession = MediaSessionCompat(this, packageName)
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
        mediaSession.isActive = true
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player = null
        player.release()
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            finishAndRemoveTask()
        }
    }

    override fun onBackPressed(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            && isPIPModeEnabled) {
            enterPIPMode()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        if (newConfig != null) {
            isInPipMode = !isInPictureInPictureMode
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    @Suppress("DEPRECATION")
    fun enterPIPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            binding.playerView.useController = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val params = PictureInPictureParams.Builder()
                this.enterPictureInPictureMode(params.build())
            } else {
                this.enterPictureInPictureMode()
            }

            val res = AppOpsManagerCompat.noteOpNoThrow(
                this,
                "android:picture_in_picture",
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid,
                packageName
            )

            if (res == AppOpsManager.MODE_ALLOWED) {
                checkPIPPermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkPIPPermission(){
        isPIPModeEnabled = isInPictureInPictureMode
        if (!isInPictureInPictureMode){
            onBackPressed()
        }
    }

}