package demo.test.kdxfvoice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

String [] permission = {
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,

};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        Button yuyin = findViewById(R.id.yuyin);
        yuyin.setOnClickListener((v) -> {

            startActivity(new Intent(MainActivity.this, HuanxingActivity.class));

        });

        findViewById(R.id.lixianyuyin).setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, LixianTingxieActivity.class));
        });

        findViewById(R.id.lixianshibie).setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, LixianyuyinShiBieActivity.class));
        });


//设置系统亮度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                //有了权限，具体的动作
//                Settings.System.putInt(getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, progress);
//                data2 = intToString(progress, 255);
//                tvSunlightValue.setText(data2 + "%");
            }
        }

    }

    void checkPermission(){
       if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_VIDEO_OUTPUT)!= PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this,permission,0x123);
       }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
