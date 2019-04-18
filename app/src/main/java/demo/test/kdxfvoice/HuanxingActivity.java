package demo.test.kdxfvoice;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

public class HuanxingActivity extends AppCompatActivity {
//    private SpeechRecognizer mAsr;

    private VoiceWakeuper mIvw;
    private static final String TAG = "HuanxingActivity";
    private Button button, stop, cancle;
    TextView huanxingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huanxing);
        huanxingText = findViewById(R.id.huanxingText);
        button = findViewById(R.id.huanxing);
        button.setOnClickListener((v) -> {
            startHuanxing();
        });

        stop = findViewById(R.id.stop);
        stop.setOnClickListener((v) -> {
            stopHuanxing();
        });
        cancle = findViewById(R.id.cancle);
        cancle.setOnClickListener((v) -> {
            cancleHuanxing();
        });


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mIvw = VoiceWakeuper.createWakeuper(this, mInitListener);
//        // 唤醒类型
//        mIvw.setParameter(SpeechConstant.IVW_SST, ivwSst);
//
//        // 唤醒门限
//        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, threshold);
//        // 持续唤醒
//        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keepAlive);

    }

    int ret = -1;

    void startHuanxing() {
        if (null == mIvw) {
            Toast.makeText(this, "始化失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 清空参数
        mIvw.setParameter(SpeechConstant.PARAMS, null);
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        //mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
        // 设置唤醒模式
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
        // 设置持续进行唤醒(0,1)1,持续唤醒
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "0");
        // 设置闭环优化网络模式
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, "1");
        // 设置唤醒资源路径
        mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
        // 设置唤醒录音保存路径，保存最近一分钟的音频
        mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
        mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
//        mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

        ret = mIvw.startListening(MyHuxnxingListener);
        if (ret != ErrorCode.SUCCESS) {
            Toast.makeText(this, "唤醒失败", Toast.LENGTH_SHORT).show();

        }
    }


    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + getString(R.string.IflytekAPP_id) + ".jet");
        return resPath;
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(HuanxingActivity.this, "初始化失败,错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private WakeuperListener MyHuxnxingListener = new WakeuperListener() {


        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "onBeginOfSpeech: -------");

        }

        @Override
        public void onResult(WakeuperResult wakeuperResult) {
            Log.d(TAG, "onResult: -------" + wakeuperResult);
            Toast.makeText(HuanxingActivity.this, wakeuperResult.getResultString(), Toast.LENGTH_LONG).show();
            huanxingText.setText(wakeuperResult.getResultString());
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: ---" + speechError.getErrorCode());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            Log.d(TAG, "onEvent: ----");
        }

        @Override
        public void onVolumeChanged(int i) {
            Log.d(TAG, "onVolumeChanged: ---" + i);
        }
    };

    void stopHuanxing() {
        if (null != mIvw) {
            mIvw.stopListening();
        }
    }

    void cancleHuanxing() {
        if (null != mIvw) {
            mIvw.cancel();
        }
    }

    void destoryHuanxing() {
        if (null != mIvw) {
            mIvw.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancleHuanxing();
        destoryHuanxing();
    }
}
