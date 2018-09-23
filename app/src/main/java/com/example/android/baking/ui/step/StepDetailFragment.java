package com.example.android.baking.ui.step;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.example.android.baking.databinding.FragmentStepDetailBinding;
import com.example.android.baking.ui.full.FullscreenActivity;
import com.example.android.baking.util.ExoPlayerViewManager;

import org.parceler.Parcels;

public class StepDetailFragment extends Fragment {

    public static final String KEY_VIDEO_URL_FULLSCREEN = "video_url_fullscreen";
    public ExoPlayerViewManager mExoPlayerViewManager;
    private Step mStep;

    private FragmentStepDetailBinding mViewBinding;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mStep = Parcels.unwrap(bundle.getParcelable(StepCollectionPagerAdapter.BUNDLE_KEY_STEP));
        }
        mExoPlayerViewManager = ExoPlayerViewManager.getInstance(mStep.getVideoURL(), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        mViewBinding.setStep(mStep);
        FrameLayout fullscreenLayout = mViewBinding.getRoot().findViewById(R.id.exo_fullscreen_button);
        fullscreenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(KEY_VIDEO_URL_FULLSCREEN, mStep.getVideoURL());
                startActivity(intent);
            }
        });
        return mViewBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mExoPlayerViewManager != null && !(mStep == null || mStep.getVideoURL().equals(""))) {
            if (!isVisibleToUser) {
                mExoPlayerViewManager.goToBackground();
            } else {
                mExoPlayerViewManager.prepareExoPlayer(mViewBinding.playerView);
                mExoPlayerViewManager.goToForeground();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStep != null && !mStep.getVideoURL().equals("")) {
            mExoPlayerViewManager.prepareExoPlayer(mViewBinding.playerView);
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
