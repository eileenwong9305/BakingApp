package com.example.android.baking.ui.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.ui.full.FullscreenActivity;
import com.example.android.baking.util.ExoPlayerViewManager;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    public static final String KEY_VIDEO_URL_FULLSCREEN = "video_url_fullscreen";

    @BindView(R.id.player_view)
    public PlayerView mPlayerView;
    @BindView(R.id.detail_desc_tv)
    public TextView mDescTextView;
    @BindView(R.id.exo_fullscreen_button)
    public FrameLayout mFullscreenLayout;
    @BindView(R.id.exo_fullscreen_icon)
    public ImageView mFullscreenIcon;
    public ExoPlayerViewManager mExoPlayerViewManager;
    private String mVideoUrl;
    private String mDescription;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mVideoUrl = bundle.getString(StepCollectionPagerAdapter.BUNDLE_KEY_VIDEO_URL);
            mDescription = bundle.getString(StepCollectionPagerAdapter.BUNDLE_KEY_DESC);
        }
        mExoPlayerViewManager = ExoPlayerViewManager.getInstance(mVideoUrl, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        mDescTextView.setText(mDescription);
        mFullscreenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(KEY_VIDEO_URL_FULLSCREEN, mVideoUrl);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mExoPlayerViewManager != null && !(mVideoUrl == null || mVideoUrl.equals(""))) {
            if (!isVisibleToUser) {
                mExoPlayerViewManager.goToBackground();
            } else {
                mExoPlayerViewManager.prepareExoPlayer(mPlayerView);
                mExoPlayerViewManager.goToForeground();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoUrl == null || mVideoUrl.equals("")) {
            mPlayerView.setVisibility(View.GONE);
            mDescTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
            mDescTextView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            mExoPlayerViewManager.prepareExoPlayer(mPlayerView);
            if (getUserVisibleHint()) mExoPlayerViewManager.goToForeground();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mExoPlayerViewManager.goToBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!getActivity().isChangingConfigurations()) {
            mExoPlayerViewManager.releaseVideoPlayer();
        }
    }
}
