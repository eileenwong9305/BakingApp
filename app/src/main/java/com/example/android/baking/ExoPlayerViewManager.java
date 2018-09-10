package com.example.android.baking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
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

    public static ExoPlayerViewManager getInstance(String videoUri) {
        ExoPlayerViewManager instance = instances.get(videoUri);
        if (instance == null) {
            instance = new ExoPlayerViewManager(videoUri);
            instances.put(videoUri, instance);
        }
        return instance;
    }

    private SimpleExoPlayer player;

    private ExoPlayerViewManager(String videoUri) {
        this.videoUri = Uri.parse(videoUri);
    }

    public void prepareExoPlayer(Context context, PlayerView exoPlayerView) {
        if (context == null || exoPlayerView == null) {
            return;
        }
        if (player == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player with previously created TrackSelector
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            // Load the SimpleExoPlayerView with the created player
//            exoPlayerView.setPlayer(player);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            // Produces DataSource instances through which media data is loaded.
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

    public void releaseVideoPlayer() {
        if (player != null) {
            player.release();
        }
        player = null;
    }

    public void goToBackground() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    public void goToForeground() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }
}

