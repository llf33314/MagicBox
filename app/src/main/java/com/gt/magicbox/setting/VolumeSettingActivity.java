package com.gt.magicbox.setting;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/7/19 0019.
 */

public class VolumeSettingActivity extends BaseActivity {
    @BindView(R.id.sb_volume_size)
    SeekBar sbVolumeSize;
    @BindView(R.id.sb_lock_size)
    SeekBar sbLockSize;
    @BindView(R.id.sb_media_size)
    SeekBar sbMediaSize;

    private AudioManager mAudioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_setting);
        mAudioManager= (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        sbVolumeSize.setOnSeekBarChangeListener(new SeekBarListener());
        sbLockSize.setOnSeekBarChangeListener(new SeekBarListener());
        sbMediaSize.setOnSeekBarChangeListener(new SeekBarListener());

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        int currentVolume=mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
        int currentLock=mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        int currentMedia= mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        sbVolumeSize.setProgress(currentVolume);
        sbLockSize.setProgress(currentLock);
        sbMediaSize.setProgress(currentMedia);




    }
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()){
                case R.id.sb_volume_size:
                    mAudioManager.setStreamVolume(AudioManager.STREAM_RING,seekBar.getProgress(),AudioManager.FLAG_PLAY_SOUND);
                    break;
                case R.id.sb_lock_size:
                    mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,seekBar.getProgress(),AudioManager.FLAG_PLAY_SOUND);
                    break;
                case R.id.sb_media_size:
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,seekBar.getProgress(),AudioManager.FLAG_PLAY_SOUND);
                    break;
            }

        }
    }
}
