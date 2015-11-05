package net.alhazmy13.mediapickerexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.alhazmy13.mediapicker.CameraPicker;

import java.io.File;

public class MainActivity extends AppCompatActivity implements CameraPicker.OnImagePicked {
    private TextView textView;
    private Button pickButton;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.tv_path);
        pickButton=(Button)findViewById(R.id.bt_pick);
        imageView=(ImageView)findViewById(R.id.iv_image);
        imageView.setRotation(90);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPicker cameraPicker = new CameraPicker(MainActivity.this);
                cameraPicker.setOnImagePicked(MainActivity.this);
                cameraPicker.setExtension(CameraPicker.JPG);
                cameraPicker.pick();
            }
        });

    }

    @Override
    public void OnImagePicked(String path) {
        textView.setText("PATH: "+path);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
       // Toast.makeText(MainActivity.this,path+"",Toast.LENGTH_SHORT).show();
    }
}
