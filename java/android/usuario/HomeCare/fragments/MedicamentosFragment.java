package android.usuario.HomeCare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.usuario.HomeCare.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MedicamentosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medicamentos, container, false);
    }

}