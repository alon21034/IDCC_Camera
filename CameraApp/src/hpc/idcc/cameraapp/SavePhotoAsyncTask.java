package hpc.idcc.cameraapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

public class SavePhotoAsyncTask extends AsyncTask<byte[], Void, Throwable> {

    private ProgressDialog mDialog;
    private Context ctx;
    private SavePhotoListener mListener;
    private Uri mUri;
    
    public SavePhotoAsyncTask(CameraPreviewActivity activity, Uri uri) {
        mListener = activity;
        ctx = activity;
        mUri = uri;
        mDialog = ProgressDialog.show(activity, null, "saving...");
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Exception doInBackground(byte[]... params) {
        if (mUri == null) {
            mUri = Uri.parse(PictureFiles.getOutputMediaFile(PictureFiles.MEDIA_TYPE_IMAGE).getPath());
            return new IOException("file is null");
        }
        
        if (params == null || params[0] == null) {
            return new NullPointerException("data is null");
        }
        
        try {
            OutputStream fos = ctx.getContentResolver().openOutputStream(mUri);
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
        if (result == null)
            mListener.onSaveFinish(result, mUri);
        else 
            mListener.onSaveFinish(result, null);
    }
    
    public interface SavePhotoListener {
        public void onSaveFinish(Throwable e, Uri uri);
    }
}
