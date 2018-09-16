package com.example.android.baking.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;
import java.util.Map;

public class ExoPlayerViewManager {

    private static Map<String, ExoPlayerViewManager> instances = new HashMap<>();
    private Uri videoUri;
    private Context context;

    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;
    private AudioManager.OnAudioFocusChangeListener focusChangeListener;
    private SimpleExoPlayer player;
    private boolean mAudioFocusGranted = false;
    private boolean mAudioIsPlaying = false;
    private ExoPlayerViewManager(String videoUri, Context context) {
        this.videoUri = Uri.parse(videoUri);
        this.context = context;
        focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        goToForeground();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        goToBackground();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        releaseVideoPlayer();
                        break;
                }
            }
        };
    }

    public static ExoPlayerViewManager getInstance(String videoUri, Context context) {
        ExoPlayerViewManager instance = instances.get(videoUri);
        if (instance == null) {
            instance = new ExoPlayerViewManager(videoUri, context);
            instances.put(videoUri, instance);
        }
        return instance;
    }

    public void prepareExoPlayer(PlayerView exoPlayerView) {
        if (context == null || exoPlayerView == null || videoUri == null) {
            return;
        }
        if (player == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, "BakingApp"),
                    defaultBandwidthMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);

            player.prepare(videoSource);
        }
        player.clearVideoSurface();
        player.setVideoSurfaceView((SurfaceView) exoPlayerView.getVideoSurfaceView());
        player.seekTo(player.getCurrentPosition() + 1);
        exoPlayerView.setPlayer(player);
    }

    private boolean requestAudioFocus() {
        if (!mAudioFocusGranted) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int result;
            if (audioManager != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                            .build();
                    focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(playbackAttributes)
                            .setOnAudioFocusChangeListener(focusChangeListener)
                            .build();
                    result = audioManager.requestAudioFocus(focusRequest);
                } else {
                    result = audioManager.requestAudioFocus(focusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
                }
                switch (result) {
                    case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                        mAudioFocusGranted = true;
                        break;
                    case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                        mAudioFocusGranted = false;
                }
            }
        }
        return mAudioFocusGranted;
    }

    public void releaseVideoPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            mAudioIsPlaying = false;
        }
        if (mAudioFocusGranted && audioManager != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(focusRequest);
            } else {
                audioManager.abandonAudioFocus(focusChangeListener);
            }
            mAudioFocusGranted = false;
        }
        player = null;
    }

    public void goToBackground() {
        if (mAudioIsPlaying && player != null && mAudioFocusGranted) {
            player.setPlayWhenReady(false);
            mAudioIsPlaying = false;
        }
    }

    public void goToForeground() {
        if (!mAudioIsPlaying && player != null) {
            if (mAudioFocusGranted || requestAudioFocus()) {
                if (player.getPlaybackState() == Player.STATE_ENDED) {
                    player.seekTo(0);
                }
                player.setPlayWhenReady(true);
                mAudioIsPlaying = true;
            }
        }
    }
}

