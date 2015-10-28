package net.alhazmy13.mediapickerexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.alhazmy13.mediapicker.CameraPicker;

public class MainActivity extends AppCompatActivity implements CameraPicker.OnImagePicked {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.text);
        CameraPicker cameraPicker=new CameraPicker(this);
        cameraPicker.setOnImagePicked(this);
        cameraPicker.pick();
    }

    @Override
    public void OnImagePicked(String path) {
        textView.setText(path+"");
       // Toast.makeText(MainActivity.this,path+"",Toast.LENGTH_SHORT).show();
    }
}
