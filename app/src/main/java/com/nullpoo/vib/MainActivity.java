package com.nullpoo.vib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {

    /** 開始フラグ */
    private boolean mIsStart;

    /** 開始ボタン */
    private Button mStartButton;

    /** 停止ボタン */
    private Button mStopButton;

    /** パターン選択スピナー */
    private Spinner mPatternSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタンの初期化
        mStartButton = (Button) findViewById(R.id.start);
        mStopButton = (Button) findViewById(R.id.stop);
        mPatternSpinner = (Spinner) findViewById(R.id.pattern_spinner);
        mStartButton.setOnClickListener(mStartClickListener);
        mStopButton.setOnClickListener(mStopClickListener);
        String[] pattens = getResources().getStringArray(R.array.vibrate_pattern_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.pattern_spinner_item, pattens);
        adapter.setDropDownViewResource(R.layout.pattern_spinner_dropdown_item);
        mPatternSpinner.setAdapter(adapter);

        mPatternSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsStart) {
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), VibrateService.class);
                changePattern(intent);
                startService(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * スピナーからパターンを切り替える
     *
     * @param intent
     */
    private void changePattern(Intent intent) {
        switch (mPatternSpinner.getSelectedItemPosition()) {
            case 0:
                intent.putExtra(VibrateService.PARAM_PATTERN, VibrateService.mPattern1);
                break;
            case 1:
                intent.putExtra(VibrateService.PARAM_PATTERN, VibrateService.mPattern2);
                break;
            case 2:
                intent.putExtra(VibrateService.PARAM_PATTERN, VibrateService.mPattern3);
                break;
            default:
                break;
        }
    }

    /** スリープ時に継続するためのレシーバー */
    public BroadcastReceiver mVibrateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && mIsStart) {
                Intent serviceIntent = new Intent(getApplicationContext(), VibrateService.class);
                changePattern(serviceIntent);
                startService(serviceIntent);
                mIsStart = true;
            }
        }
    };

    /** 開始ボタンのリスナー */
    private View.OnClickListener mStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), VibrateService.class);
            changePattern(intent);
            startService(intent);

            // スリープ時にも継続するように設定
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mVibrateReceiver, filter);

            mIsStart = true;
        }
    };

    /** 停止ボタンのリスナー */
    private View.OnClickListener mStopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopService(new Intent(getApplicationContext(), VibrateService.class));
            mIsStart = false;
        }
    };

}
