package hpc.idcc.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private final static int AR_IMAGE_FROM_CAMERA = 1;
    private final static int AR_IMAGE_FROM_CAMERA_PREVIEW = 2;
    private final static int AR_IMAGE_FROM_DEFAULT_FOLDER = 3;
    
    private Button mButtonViaIntent;
    private Button mButtonCameraFeature;
    private Button mButtonDefaultFolder;
    private Uri cameraOutputUri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonViaIntent = (Button) findViewById(R.id.main_button_intent);
        mButtonViaIntent.setOnClickListener(this);

        mButtonCameraFeature = (Button) findViewById(R.id.main_button_camera_feature);
        mButtonCameraFeature.setOnClickListener(this);
        
        mButtonDefaultFolder = (Button) findViewById(R.id.main_button_default_folder);
        mButtonDefaultFolder.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case AR_IMAGE_FROM_CAMERA:
            if (resultCode == RESULT_OK) {
                if (cameraOutputUri != null)
                    Toast.makeText(this, "saved photo in: " + cameraOutputUri.getPath(), 
                            Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops! Some errors occur!! (camera via intent)", 
                        Toast.LENGTH_LONG).show();
            }
            break;
        case AR_IMAGE_FROM_DEFAULT_FOLDER:
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "saved photo in: " + 
                        getRealPathFromURI(data.getData()), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops! Some errors occur!! (camera via intent)", 
                        Toast.LENGTH_LONG).show();
            }
            break;
        case AR_IMAGE_FROM_CAMERA_PREVIEW:
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "saved photo from camera preview", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops! Some errors occur!! (camera preview)", Toast.LENGTH_LONG).show();
            }
            break;
        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = null;
        int requestCode = -1;
        switch (v.getId()) {
        case R.id.main_button_intent:
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraOutputUri = PictureFiles.getOutputMediaFileUri(
                    PictureFiles.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputUri);
            requestCode = AR_IMAGE_FROM_CAMERA;
            break;
        case R.id.main_button_default_folder:
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            requestCode = AR_IMAGE_FROM_DEFAULT_FOLDER;
            break;
        case R.id.main_button_camera_feature:
            intent = new Intent(this, CameraPreviewActivity.class);
            requestCode = AR_IMAGE_FROM_CAMERA_PREVIEW;
            break;
        default:
            break;
        }

        if (intent != null) {
            startActivityForResult(intent, requestCode);
        }
    }
    
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
