package com.example.dancestudioapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContract;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropActivityResultContract extends ActivityResultContract<Object, Uri> {

    @Override
    public Intent createIntent(Context context, Object input) {

        return CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(context);
    }

    @Override
    public Uri parseResult(int resultCode, Intent intent) {
        return CropImage.getActivityResult(intent).getUri();
    }
}
