package com.nullpoo.vib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

/**
 * バイブレートを行うサービス
 */
public class VibrateService extends Service {

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
        // 振動パターンを作成
        long[] pattern = {0, 10000};
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
