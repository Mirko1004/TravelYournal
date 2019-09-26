package zkezic.fesb.mimosproject.presentation.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import zkezic.fesb.mimosproject.R;

public class GalleryActivity extends AppCompatActivity {

    @BindView(R.id.photo_view)
    PhotoView photoView;

    @BindView(R.id.video_view)
    VideoView videoView;

    String filename;
    boolean isVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLayout();
    }

    public void setupLayout() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isVideo = extras.getBoolean("IS_VIDEO");
            if(extras.get("FILENAME") != null) filename = extras.get("FILENAME").toString();

            if(!TextUtils.isEmpty(filename)) {
                if(isVideo) {
                    loadVideo();
                } else {
                    loadImage();
                }
            }
        }
    }

    public void loadVideo() {
        File videoFileUploaded = new File(Environment.getExternalStorageDirectory(), filename);

        if (videoFileUploaded.exists()) {
            videoView.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.fromFile(videoFileUploaded));
            videoView.start();
        } else {
            Toast.makeText(getApplicationContext(), "Video not found", Toast.LENGTH_LONG).show();
        }
    }

    public void loadImage() {
        File imageFileUploaded = new File(Environment.getExternalStorageDirectory(), filename);

        if (imageFileUploaded.exists()) {
            photoView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(this)
                    .load(imageFileUploaded)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .into(photoView);
        } else {
            Toast.makeText(getApplicationContext(), "Image not found", Toast.LENGTH_LONG).show();
        }
    }
}
