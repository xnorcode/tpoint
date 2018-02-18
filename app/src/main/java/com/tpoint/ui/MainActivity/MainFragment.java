package com.tpoint.ui.MainActivity;

import android.content.DialogInterface;
import android.content.IntentSender;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.tpoint.R;
import com.tpoint.data.TPoint;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tpoint.ui.MainActivity.Constants.TAG_MODE_NORMAL;
import static com.tpoint.ui.MainActivity.Constants.TAG_MODE_ORANGE;
import static com.tpoint.ui.MainActivity.Constants.TAG_MODE_RED;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class MainFragment extends Fragment implements MainActivityView {

    // View Binding related
    @BindView(R.id.tag_btn)
    public Button mTagButton;
    @BindView(R.id.numberPicker)
    public NumberPicker mChannelPicker;
    // Presenter
    private MainActivityPresenterMgmt mPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init presenter
        mPresenter = new MainActivityPresenterMgmt(getContext(), this);
        // init location tracking
        mPresenter.startLocationTracking();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // init UI references
        ButterKnife.bind(this, rootView);
        // configure channel picker
        mChannelPicker.setMinValue(1);
        mChannelPicker.setMaxValue(999);
        mChannelPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                // set new channel when scrolling is finished
                if (scrollState == SCROLL_STATE_IDLE) {
                    mPresenter.channelChange(view.getValue());
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }


    /*
    View Callback Methods
     */
    @Override
    public void onPointTagged() {
        // notify user point was tagged
        Toast.makeText(getContext(), getString(R.string.toast_point_tagged), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointUntagged() {
        // notify user point was untagged
        Toast.makeText(getContext(), getString(R.string.toast_point_untagged), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChannelChangedAlert(int channel) {
        // show changed channel alert
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("New Channel")
                .setMessage("Welcome to channel " + channel + "!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing happens
                    }
                }).show();
    }

    @Override
    public void onPointNearbyAlert(TPoint tp) {
        // alert user that there is a tagged point in a range of 250 meters nearby
        // play alert sound and change button colors
        if (mTagButton != null) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    final Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
                    r.play();
                    if (tp.getDistance() < 250) {
                        setButtonMode(TAG_MODE_RED);
                    } else {
                        setButtonMode(TAG_MODE_ORANGE);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setButtonMode(TAG_MODE_NORMAL);
                            r.stop();
                        }
                    }, TimeUnit.SECONDS.toMillis(30));
                }
            };
            getActivity().runOnUiThread(task);
        }
    }

    @Override
    public void onTerminateActivity() {
        getActivity().finish();
    }

    @Override
    public void onLocationResolutionRequest(Status status) {
        // request resolution for when location status not ok
        try {
            status.startResolutionForResult(getActivity(), Constants.REQUEST_CODE_CHECK_LOCATION_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    /*
    View Binding: Button onClick() Method
     */
    @OnClick(R.id.tag_btn)
    public void tagButtonClick() {
        if (mTagButton != null && mTagButton.getTag() != null) {// && mEngine != null
            if (mTagButton.getTag().equals(getString(R.string.tag_btn_tag))) {
                mPresenter.tagPoint();
            } else if (mTagButton.getTag().equals(getString(R.string.tag_btn_remove_tag))) {// && mEngine.hasPointLessThan500()
                mPresenter.unTagPoint();
                setButtonMode(TAG_MODE_NORMAL);
            }
        }
    }


    /*
    Change button background colours helper method
     */
    private void setButtonMode(@Constants.SELECT_TAG_MODE int mode) {
        if (mTagButton != null) {
            switch (mode) {
                case TAG_MODE_NORMAL: {
                    mTagButton.setBackgroundResource(R.drawable.shape_circle);
                    mTagButton.setText(getString(R.string.tag_btn_tag));
                    mTagButton.setTag(getString(R.string.tag_btn_tag));
                    return;
                }
                case TAG_MODE_ORANGE: {
                    mTagButton.setBackgroundResource(R.drawable.shape_circle_orange);
                    break;
                }
                case TAG_MODE_RED: {
                    mTagButton.setBackgroundResource(R.drawable.shape_circle_red);
                    break;
                }
            }
            mTagButton.setText(getString(R.string.tag_btn_remove_tag));
            mTagButton.setTag(getString(R.string.tag_btn_remove_tag));
        }
    }
}