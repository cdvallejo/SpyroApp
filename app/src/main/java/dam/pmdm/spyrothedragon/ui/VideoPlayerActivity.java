package dam.pmdm.spyrothedragon.ui;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.databinding.ActivityVideoPlayerBinding;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.easter_egg_video);
        binding.videoView.setVideoURI(videoUri);

        binding.videoView.setOnCompletionListener(mp -> finish());

        binding.videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Liberamos el binding para evitar fugas de memoria
    }
}
