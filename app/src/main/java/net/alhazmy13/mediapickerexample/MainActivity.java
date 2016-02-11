package net.alhazmy13.mediapickerexample;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;

import net.alhazmy13.mediapicker.Image.ImagePicker;

public class MainActivity extends AppCompatActivity implements ImagePicker.OnImageSetListener {
    private static final String TAG = "MainActivity";
    private TextView textView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // RefWatcher refWatcher = App.getRefWatcher(this);

        textView=(TextView)findViewById(R.id.tv_path);
        Button pickButton = (Button) findViewById(R.id.bt_pick);
        imageView=(ImageView)findViewById(R.id.iv_image);
        imageView.setRotation(90);


        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();

            }
        });

    }


    private void pickImage(){
        ImagePicker imagePicker = new ImagePicker(this);
        imagePicker.setOnImageSetListener(this);
        imagePicker.setDirectory(ImagePicker.Directory.DEFAULT);
        imagePicker.setMode(ImagePicker.Mode.CAMERA);
        imagePicker.setExtension(ImagePicker.Extension.PNG);
        imagePicker.setCompressLevel(ImagePicker.ComperesLevel.MEDIUM);
        imagePicker.pick();
    }



    @Override
    public void OnImageSet(String path) {
        textView.setText("PATH: "+path);
        Log.d(TAG, "OnImageSet() called with: " + "path = [" + path + "]");
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        // Toast.makeText(MainActivity.this,path+"",Toast.LENGTH_SHORT).show();
    }


}