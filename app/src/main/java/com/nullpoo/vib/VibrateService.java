package com.nullpoo.vib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

/**
 * バイブレートを行うサービス
 */
public class VibrateService extends Service {

    public static final String PARAM_PATTERN = "ParamPattern";

    public static final long[] mPattern1 = {0, 10000};

    public static final long[] mPattern2 = {100, 1000};

    public static final long[] mPattern3 = {100, 100};

    /** バイブレータ */
    private Vibrator mVibrator;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 振動パターンを取得
        long[] pattern = intent.getLongArrayExtra(PARAM_PATTERN);

        // 振動を開始
        mVibrator.vibrate(pattern, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 振動を停止
        mVibrator.cancel();
    }

}
