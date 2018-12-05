package android.usuario.HomeCare.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.usuario.HomeCare.fragments.AlimentacaoFragment;
import android.usuario.HomeCare.fragments.EstatisticasFragment;
import android.usuario.HomeCare.fragments.ExerciciosFragment;
import android.usuario.HomeCare.fragments.MedicamentosFragment;
import android.usuario.HomeCare.R;
import android.usuario.HomeCare.fragments.SaudeFragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private AlimentacaoFragment alimentacaoFragment;
    private EstatisticasFragment estatisticasFragment;
    private ExerciciosFragment exerciciosFragment;
    private MedicamentosFragment medicamentosFragment;
    private SaudeFragment saudeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
        }

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        alimentacaoFragment = new AlimentacaoFragment();
        estatisticasFragment = new EstatisticasFragment();
        exerciciosFragment = new ExerciciosFragment();
        medicamentosFragment = new MedicamentosFragment();
        saudeFragment = new SaudeFragment();


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_exercicios:
                        mMainNav.setItemBackgroundResource(R.color.menu1);
                        setFragment(exerciciosFragment);
                        return true;
                    case R.id.nav_alimentacao:
                        mMainNav.setItemBackgroundResource(R.color.menu2);
                        setFragment(alimentacaoFragment);
                        return true;
                    case R.id.nav_saude:
                        mMainNav.setItemBackgroundResource(R.color.menu3);
                        setFragment(saudeFragment);
                        return true;
                    case R.id.nav_medicamentos:
                        mMainNav.setItemBackgroundResource(R.color.menu4);
                        setFragment(medicamentosFragment);
                        return true;
                    case R.id.nav_estatisticas:
                        mMainNav.setItemBackgroundResource(R.color.menu5);
                        setFragment(estatisticasFragment);
                        return true;

                    default:
                        return false;

                }
            }


        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }


}

