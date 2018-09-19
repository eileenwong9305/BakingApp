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

    private static Map<String, ExoPlayerViewManager> sInstances = new HashMap<>();
    private Uri mVideoUri;
    private Context mContext;

    private AudioManager mAudioManager;
    private AudioFocusRequest mFocusRequest;
    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener;
    private SimpleExoPlayer mPlayer;
    private boolean mAudioFocusGranted = false;
    private boolean mAudioIsPlaying = false;
    private ExoPlayerViewManager(String videoUri, Context context) {
        this.mVideoUri = Uri.parse(videoUri);
        this.mContext = context;
        mFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
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
        ExoPlayerViewManager instance = sInstances.get(videoUri);
        if (instance == null) {
            instance = new ExoPlayerViewManager(videoUri, context);
            sInstances.put(videoUri, instance);
        }
        return instance;
    }

    public void prepareExoPlayer(PlayerView exoPlayerView) {
        if (mContext == null || exoPlayerView == null || mVideoUri == null) {
            return;
        }
        if (mPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    mContext,
                    Util.getUserAgent(mContext, "BakingApp"),
                    defaultBandwidthMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mVideoUri);

            mPlayer.prepare(videoSource);
        }
        mPlayer.clearVideoSurface();
        mPlayer.setVideoSurfaceView((SurfaceView) exoPlayerView.getVideoSurfaceView());
        mPlayer.seekTo(mPlayer.getCurrentPosition() + 1);
        exoPlayerView.setPlayer(mPlayer);
    }

    private boolean requestAudioFocus() {
        if (!mAudioFocusGranted) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int result;
            if (mAudioManager != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                            .build();
                    mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(playbackAttributes)
                            .setOnAudioFocusChangeListener(mFocusChangeListener)
                            .build();
                    result = mAudioManager.requestAudioFocus(mFocusRequest);
                } else {
                    result = mAudioManager.requestAudioFocus(mFocusChangeListener,
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
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mAudioIsPlaying = false;
        }
        if (mAudioFocusGranted && mAudioManager != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                mAudioManager.abandonAudioFocusRequest(mFocusRequest);
            } else {
                mAudioManager.abandonAudioFocus(mFocusChangeListener);
            }
            mAudioFocusGranted = false;
        }
        mPlayer = null;
    }

    public void goToBackground() {
        if (mAudioIsPlaying && mPlayer != null && mAudioFocusGranted) {
            mPlayer.setPlayWhenReady(false);
            mAudioIsPlaying = false;
        }
    }

    public void goToForeground() {
        if (!mAudioIsPlaying && mPlayer != null) {
            if (mAudioFocusGranted || requestAudioFocus()) {
                if (mPlayer.getPlaybackState() == Player.STATE_ENDED) {
                    mPlayer.seekTo(0);
                }
                mPlayer.setPlayWhenReady(true);
                mAudioIsPlaying = true;
            }
        }
    }
}

