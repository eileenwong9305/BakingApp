package com.example.android.baking.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking.ExoPlayerViewManager;
import com.example.android.baking.FullscreenActivity;
import com.example.android.baking.R;
import com.google.android.exoplayer2.ui.PlayerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class StepDetailFragment extends Fragment {

    public static final String KEY_VIDEO_URL_FULLSCREEN = "video_url_fullscreen";

    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.detail_desc_tv)
    TextView descTextView;
    @BindView(R.id.exo_fullscreen_button)
    FrameLayout fullscreenLayout;
    @BindView(R.id.exo_fullscreen_icon)
    ImageView fullscreenIcon;
    private String videoUrl;
    private String description;

    public ExoPlayerViewManager exoPlayerViewManager;

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
        exoPlayerViewManager = ExoPlayerViewManager.getInstance(videoUrl, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        descTextView.setText(description);

        fullscreenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(KEY_VIDEO_URL_FULLSCREEN, videoUrl);
                startActivity(intent);
            }
        });
        Log.e(getClass().getSimpleName(), "onCreateView");
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (videoUrl != null) ExoPlayerViewManager.getInstance(videoUrl, getContext()).goToBackground();
        } else {
            if (videoUrl != null) ExoPlayerViewManager.getInstance(videoUrl, getContext()).goToForeground();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(), "onResume");
        if (videoUrl == null || videoUrl.equals("")) {
            playerView.setVisibility(View.GONE);
            descTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,28f);
            descTextView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            playerView.setVisibility(View.VISIBLE);
            exoPlayerViewManager.prepareExoPlayer(playerView);
            exoPlayerViewManager.goToForeground();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerViewManager.goToBackground();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(getClass().getSimpleName(), "onStop");
        exoPlayerViewManager.stopPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exoPlayerViewManager.releaseVideoPlayer();
    }
}
