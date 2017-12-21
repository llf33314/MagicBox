package com.gt.magicbox.utils.voice;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.DoubleCalcUtils;
import com.gt.magicbox.utils.WavMergeUtil;
import com.gt.magicbox.utils.commonutil.FileUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class VoiceUtils {
    private static volatile VoiceUtils singleton = null;
    public boolean IsPlaying;
    private int soundType = 2;
    MediaPlayer mediaPlayer = null;
    private boolean finishFlag = false;

    private Runnable runnable;
    private Context mContext;
    private Integer[] successVoice = {R.raw.wechat_pay, R.raw.ali_pay, R.raw.cash,
            R.raw.member_pay, R.raw.bank_card, R.raw.member_pay,
            R.raw.member_recharge, R.raw.balance};

    private AppendListener appendListener;
    private static final int MSG_PROGRESS = 0x01;

    public VoiceUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
        }
    };

    /**
     * 单例
     *
     * @param context
     * @return
     */
    public static VoiceUtils with(Context context) {
        if (singleton == null) {
            synchronized (VoiceUtils.class) {
                if (singleton == null) {
                    singleton = new VoiceUtils(context);
                }
            }
        }
        return singleton;
    }

    public void SetIsPlay(boolean IsPlaying) {

        this.IsPlaying = IsPlaying;
    }

    public boolean GetIsPlay() {
        return IsPlaying;
    }


    public List<File> getStreamList(String soundString) {
        List<File> streamList = new ArrayList<>();
        char[] chars = soundString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            InputStream wavStream = null;
            switch ("" + chars[i]) {
                case "零":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound0);
                    break;
                case "壹":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound1);

                    break;
                case "贰":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound2);

                    break;
                case "叁":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound3);

                    break;
                case "肆":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound4);

                    break;
                case "伍":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound5);

                    break;
                case "陆":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound6);

                    break;
                case "柒":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound7);

                    break;
                case "捌":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound8);

                    break;
                case "玖":
                    wavStream = mContext.getResources().openRawResource(R.raw.sound9);

                    break;
                case "拾":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundshi);

                    break;
                case "佰":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundbai);

                    break;
                case "仟":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundqian);

                    break;
                case "角":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundjiao);

                    break;
                case "分":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundfen);

                    break;
                case "元":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundyuan);

                    break;
                case "整":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundzheng);

                    break;
                case "万":
                    wavStream = mContext.getResources().openRawResource(R.raw.soundwan);

                    break;
                case "点":
                    wavStream = mContext.getResources().openRawResource(R.raw.sounddot);


                    break;
                case "$":
                    if (soundType >= 0 && soundType < successVoice.length) {
                        wavStream = mContext.getResources().openRawResource(successVoice[soundType]);
                    }
                    break;
            }
            if (wavStream != null) {
                LogUtils.d("wavStream--i=" + i);
                File file = FileUtils.createAppDataFile("" + i);
                inputstreamtofile(wavStream, file);
                if (file.exists()) {
                    streamList.add(file);
                }
            }
        }
        return streamList;
    }

    public VoiceUtils playMergeWavFile(String str, int soundType) {
        this.soundType = soundType;
        List<File> fileList = getStreamList(str);
        File mix = FileUtils.createAppDataFile("mix");
        WavMergeUtil.mergeWavResources(fileList, mix);
        if (mix.exists()) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mix.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (appendListener != null) {
                            appendListener.append();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            mix.delete();
        }
        return singleton;
    }

    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer createSound(int soundIndex, String soundString) {
        MediaPlayer mp = null;

        String soundChar = soundString.substring(soundIndex - 1, soundIndex);
        LogUtils.d("soundChar=" + soundChar + " payType=" + soundType);
        switch (soundChar) {
            case "零":
                mp = MediaPlayer.create(mContext, R.raw.sound0);
                break;
            case "壹":
                mp = MediaPlayer.create(mContext, R.raw.sound1);
                break;
            case "贰":
                mp = MediaPlayer.create(mContext, R.raw.sound2);
                break;
            case "叁":
                mp = MediaPlayer.create(mContext, R.raw.sound3);
                break;
            case "肆":
                mp = MediaPlayer.create(mContext, R.raw.sound4);
                break;
            case "伍":
                mp = MediaPlayer.create(mContext, R.raw.sound5);
                break;
            case "陆":
                mp = MediaPlayer.create(mContext, R.raw.sound6);
                break;
            case "柒":
                mp = MediaPlayer.create(mContext, R.raw.sound7);
                break;
            case "捌":
                mp = MediaPlayer.create(mContext, R.raw.sound8);
                break;
            case "玖":
                mp = MediaPlayer.create(mContext, R.raw.sound9);
                break;
            case "拾":
                mp = MediaPlayer.create(mContext, R.raw.soundshi);
                break;
            case "佰":
                mp = MediaPlayer.create(mContext, R.raw.soundbai);
                break;
            case "仟":
                mp = MediaPlayer.create(mContext, R.raw.soundqian);
                break;
            case "角":
                mp = MediaPlayer.create(mContext, R.raw.soundjiao);
                break;
            case "分":
                mp = MediaPlayer.create(mContext, R.raw.soundfen);
                break;
            case "元":
                mp = MediaPlayer.create(mContext, R.raw.soundyuan);
                break;
            case "整":
                mp = MediaPlayer.create(mContext, R.raw.soundzheng);
                break;
            case "万":
                mp = MediaPlayer.create(mContext, R.raw.soundwan);
                break;
            case "点":
                mp = MediaPlayer.create(mContext, R.raw.sounddot);

                break;
            case "$":
                if (soundType >= 0 && soundType < successVoice.length) {
                    mp = MediaPlayer.create(mContext, successVoice[soundType]);
                }
                break;

        }
        //下面这三句是控制语速，但是只适用于Android6.0 以上，以下的就会报错，所以这个功能下次更新时解决
//        PlaybackParams pbp = new PlaybackParams();
//        pbp.setSpeed(1.5F);
//        mp.setPlaybackParams(pbp);
        if (mp != null) {
            mp.stop();
        }
        return mp;
    }

    public VoiceUtils setAppendListener(AppendListener appendListener) {
        this.appendListener = appendListener;
        return singleton;
    }

    public interface AppendListener {
        public void append();
    }
}
