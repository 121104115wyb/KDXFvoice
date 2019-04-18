package demo.test.kdxfvoice;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by wyb on 2019-04-18.
 */


public class AndroidApp extends Application{

    static AndroidApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initKdxf();
    }

    public static AndroidApp getInstance() {
        return instance;
    }

    void initKdxf(){
        SpeechUtility.createUtility(instance, SpeechConstant.APPID +"=5cb7f256");
    }

}
