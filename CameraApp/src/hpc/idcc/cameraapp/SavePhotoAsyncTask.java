package hpc.idcc.cameraapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SavePhotoAsyncTask extends AsyncTask<byte[], Void, Throwable> {

    private ProgressDialog mDialog;
    private SavePhotoListener mListener;
    
    public SavePhotoAsyncTask(CameraPreviewActivity activity) {
        mListener = activity;
        mDialog = ProgressDialog.show(activity, null, "saving...");
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Exception doInBackground(byte[]... params) {
        File pictureFile = PictureFiles.getOutputMediaFile(PictureFiles.MEDIA_TYPE_IMAGE);
        if (pictureFile == null){
            return new NullPointerException("pictureFile is null");
        }
        
        if (params == null || params[0] == null) {
            return new NullPointerException("data is null");
        }
        
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(params[0]);
            fos.close();
        } catch (FileNotFoundException e) {
            // (TODO) handle file not found error.
        } catch (IOException e) {
            // (TODO) handle accessing file error.
        }
        return null;
    }

    @Override
    protected void onPostExecute(Throwable result) {
        super.onPostExecute(result);
        mDialog.cancel();
        mListener.onSaveFinish(result);
    }
    
    public interface SavePhotoListener {
        public void onSaveFinish(Throwable e);
    }
}
