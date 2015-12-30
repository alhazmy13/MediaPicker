package net.alhazmy13.mediapickerexample;

import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Sound.SoundPicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;

public class MainActivity extends AppCompatActivity implements ImagePicker.OnImageSetListener {
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
                pickImage();

            }
        });

    }


    private void pickImage(){
        File saveFolder = null;
            saveFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/myFolder/images");
             saveFolder.mkdirs();
             if (!saveFolder.isDirectory())
              throw new RuntimeException("");

        ImagePicker imagePicker = new ImagePicker(this);
        imagePicker.setOnImageSetListener(this);
        imagePicker.setDirectory(saveFolder != null ? saveFolder.getAbsolutePath() : ImagePicker.DEFAULT_DIR);
        imagePicker.setCompressLevel(ImagePicker.MEDIUM);
        imagePicker.pick();
    }



    @Override
    public void OnImageSet(String path) {
        textView.setText("PATH: "+path);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        // Toast.makeText(MainActivity.this,path+"",Toast.LENGTH_SHORT).show();
    }


}