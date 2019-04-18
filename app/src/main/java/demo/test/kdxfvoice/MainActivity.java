package demo.test.kdxfvoice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        Button yuyin = findViewById(R.id.yuyin);
        checkPermission();
        yuyin.setOnClickListener((v)->{

            startActivity(new Intent(MainActivity.this,HuanxingActivity.class));


        });
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
