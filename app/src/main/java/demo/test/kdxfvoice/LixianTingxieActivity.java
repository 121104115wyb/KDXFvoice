package demo.test.kdxfvoice;

import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import demo.test.kdxfvoice.util.JsonParser;


public class LixianTingxieActivity extends AppCompatActivity {
    private static final String TAG = "LixianTingxieActivity";
    SpeechRecognizer mIat;
    private String engineMode = SpeechConstant.MODE_MSC;
    private String resultType = "json";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_LOCAL;
    RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixian_tingxie);
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        //设置语音识别弹出框
        mIatDialog = new RecognizerDialog(this, mInitListener);

        //以下为dialog设置听写参数
//        mIatDialog.setParams("xxx","xxx");
         //开始识别
        findViewById(R.id.start).setOnClickListener(v -> {
            //清空存储空间
            mIatResults.clear();
            //设置参数
            setParam();
            //开始识别并设置监听器
            mIatDialog.setListener(mRecognizerDialogListener);
            //显示听写对话框
            mIatDialog.show();
        });


    }

    public void setParam() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Toast.makeText(this, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        }
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
        mIat.getParameter(SpeechConstant.ENGINE_MODE);

        // 设置引擎模式
//        mIat.setParameter(SpeechConstant.ENGINE_MODE, engineMode);
//        if (SpeechConstant.MODE_MSC.equals(engineMode)) {
//            // 设置语法结果文件保存路径，以在本地识别时使用
//            mIat.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
//            //设置识别资源路径
//            mIat.setParameter(ResourceUtil.ASR_RES_PATH, asrResPath);
//        }


        //开始翻译设置的条件
//        mIat.setParameter(SpeechConstant.ASR_SCH, "1");
//        mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
//        mIat.setParameter(SpeechConstant.TRS_SRC, "its");
         //设置默认为汉语（离线模式测试翻译无效）
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//        mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
//        mIat.setParameter(SpeechConstant.TRANS_LANG, "en");

          //开始翻译设置的条件
//        mIat.setParameter(SpeechConstant.ASR_SCH, "1");
//        mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
//        mIat.setParameter(SpeechConstant.TRS_SRC, "its");

//        //设置默认为英语（离线模式测试翻译无效）
//        mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//        mIat.setParameter(SpeechConstant.ACCENT, null);
//        //设置原始余音为
//        mIat.setParameter(SpeechConstant.ORI_LANG, "en");
//        //设置翻译后的为汉语
//        mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");


        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        /* 其中 "bnf" 指定语法类型为 BNF,  grammarContent 为语法内容，grammarListener 为构建结果监听器*/
        int ret = 0;
        //ret = mIat.buildGrammar("bnf", grammarContent, grammarListener);
        if (ret != ErrorCode.SUCCESS) {
            Toast.makeText(this, "识别失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.d(TAG, "onInit: ----" + i);
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(LixianTingxieActivity.this, "监听失败,错误码：" + i, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {

            Log.d(TAG, "onResult: ---" + recognizerResult + "boolean:" + b);
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        Log.d(TAG, "printResult: -----:" + resultBuffer.toString());
//        mResultText.setText(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());
    }


    private GrammarListener grammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String s, SpeechError speechError) {
            Log.d(TAG, "onBuildFinish: -----s:" + s + "speechError:" + speechError);
        }
    };


}
