package devops.colby.cheqit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ZoomedImageActivity extends AppCompatActivity {
    ImageCapture imageCapture;
    Activity activity = this;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomed_image);
        final String photoUrl = this.getIntent().getExtras().getString("photo");

        final ImageView zoomedPhoto = findViewById(R.id.zoomed_image);
        imageCapture = new ImageCapture(activity, context);
        imageCapture.setmCurrentPhotoPath(photoUrl);
        zoomedPhoto.setImageBitmap(imageCapture.getPhoto());


    }
}
