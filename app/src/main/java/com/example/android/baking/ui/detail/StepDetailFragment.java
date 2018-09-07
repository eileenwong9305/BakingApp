package com.example.android.baking.ui.detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.detail_desc_tv)
    TextView descTextView;
    private SimpleExoPlayer exoPlayer;
    private String videoUrl;
    private String description;

    public StepDetailFragment () {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoUrl = bundle.getString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL);
            description = bundle.getString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        descTextView.setText(description);
        if (videoUrl == null || videoUrl.equals("")) {
            playerView.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }
        return rootView;
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            // Handler for the video player
            Handler mainHandler = new Handler();

	/* A TrackSelector that selects tracks provided by the MediaSource to be consumed by each of the available Renderers.
	  A TrackSelector is injected when the player is created. */
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player with previously created TrackSelector
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Load the SimpleExoPlayerView with the created player
            playerView.setPlayer(exoPlayer);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    getContext(),
                    Util.getUserAgent(getContext(), "BakingApp"),
                    defaultBandwidthMeter);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoUrl));

            // Prepare the player with the source.
            exoPlayer.prepare(videoSource);

            // Autoplay the video when the player is ready
            exoPlayer.setPlayWhenReady(true);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer.stop();
            exoPlayer = null;
        }
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
