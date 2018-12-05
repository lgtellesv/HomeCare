package android.usuario.HomeCare.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.usuario.HomeCare.R;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//ATIVANDO TELA CHEIA
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = (VideoView) findViewById(R.id.videoView);
//BUSCANDO CAMINHO DO VIDEO
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        videoView.setVideoURI(video);
//VERIFICA TERMINO DO VIDEO PARA ABRIR OUTRA ACTIVITY
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isFinishing())
                    return;
                startActivity(new Intent(SplashScreen.this, Login.class));
                finish();

            }
        });

        videoView.start();

    }
}
