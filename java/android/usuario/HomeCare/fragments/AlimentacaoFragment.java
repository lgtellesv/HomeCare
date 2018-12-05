package android.usuario.HomeCare.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.usuario.HomeCare.R;
import android.usuario.HomeCare.activities.ConsumoAgua;
import android.usuario.HomeCare.activities.MedicaoBatimentos;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AlimentacaoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alimentacao, container, false);


        final ImageView imageview = (ImageView) v.findViewById(R.id.bt_agua);

        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                Intent intent;
                intent = new Intent(getActivity(), ConsumoAgua.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anima_direita_in, R.anim.anima_esquerda_out);
            }
        });
        return v;
    }

}
