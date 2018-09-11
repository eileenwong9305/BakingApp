package com.example.android.baking;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.AudioAttributesCompat;
import android.util.Log;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
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

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExoPlayerVideoHandler {

    private SimpleExoPlayer player;
    private static String playerUri;

    private static ExoPlayerVideoHandler instance;
    public static ExoPlayerVideoHandler getInstance(String uri){
        if (instance == null || !playerUri.equals(uri)) {
            instance = new ExoPlayerVideoHandler(uri);
        }
        return instance;
    }

    private ExoPlayerVideoHandler(String uri) {
        playerUri = uri;
    }

    @Inject
    public ExoPlayerVideoHandler(){}

    public void initializeExoPlayer(Context context, String uri, PlayerView playerView) {
        if (context == null || uri == null || playerView == null) {
            return;
        }
        if (player == null) {
//            playerUri = uri;

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player with previously created TrackSelector
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            // Load the SimpleExoPlayerView with the created player
//            playerView.setPlayer(player);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, "BakingApp"),
                    defaultBandwidthMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(playerUri));

            // Prepare the player with the source.
            player.prepare(videoSource);
        }
        player.clearVideoSurface();
        player.setVideoSurfaceView((SurfaceView) playerView.getVideoSurfaceView());
        player.seekTo(player.getCurrentPosition() + 1);
        playerView.setPlayer(player);
        Log.e("ExoPlayerVideoHandler", "initlialize player" + player.toString());
        player.setPlayWhenReady(true);
    }

    public void releasePlayer() {
        if (player != null) {
            Log.e("ExoPlayerVideoHandler", "release player" + player.toString());
            player.release();
            player = null;
        }
    }

    public void stopPlayer() {
        if (player != null) {
            Log.e("ExoPlayerVideoHandler", "stop player" + player.toString());
            player.stop();
        }
    }

    public void startPlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    public void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }
}
