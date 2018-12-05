package android.usuario.HomeCare.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.usuario.HomeCare.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumoAgua extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_agua);

        final double metaAgua = 4000; // QUATRO LITROS
        final double aguaRestante = metaAgua;

        final TextView faltam = (TextView) findViewById(R.id.faltam);
        faltam.setText(String.valueOf(aguaRestante/1000));

        final TextView alvo = (TextView) findViewById(R.id.alvo);
        alvo.setText(String.valueOf(metaAgua/1000));




        // SEEKBAR

        final TextView prog = (TextView) findViewById(R.id.txtProgresso);
        final SeekBar progresso = (SeekBar) findViewById(R.id.seekBar);
        progresso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog.setText("Progresso: " +  progress + "%");
             //   progresso.setEnabled(false); // DESABILITANDO O MOVER DA SEEKBAR PELO USUARIO
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // SPINNER

        final Spinner aguaQtd = (Spinner) findViewById(R.id.spinner_quantidade);
        ArrayAdapter adaptador = ArrayAdapter.createFromResource(this, R.array.Beber_agua, android.R.layout.simple_spinner_dropdown_item);

        aguaQtd.setAdapter(adaptador);

        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = aguaQtd.getSelectedItem().toString();
                Toast.makeText(getBaseContext(), "Parabéns! Você bebe " + item, Toast.LENGTH_SHORT).show(); // COMANDO PARA PRINTAR NA TELA

                int somar = 0;
                final int posicao = aguaQtd.getSelectedItemPosition();
                switch (posicao){
                    case 0: somar = 180; break;
                    case 1: somar = 350; break;
                    case 2: somar = 500; break;
                    case 3: somar = 1000; break;

                }
                final double porcentagem = (somar / aguaRestante) * 100;

                /*-----------------COLOCANDO O BOTAO AQUI---------------*/
                //  MUNDANDO O SEEKBAR AO CLICAR NO BOTAO

                Button btnBeber = (Button) findViewById(R.id.btnBeber);
                final int finalSomar = somar;
                btnBeber.setOnClickListener(new View.OnClickListener() {
                    double aguaRestanteAux = aguaRestante;

                    @Override
                    public void onClick(View v) {
                        progresso.setEnabled(true); // HABILITANDO O MOVER DA SEEKBAR PELO USUARIO AO CLICAR NO BOTAO
                      //  progresso.setProgress(progresso.getProgress() + finalSomar); // finalSomar FAZ REFERENCIA A VARIAVEL somar QUE TINHA QUE SER FINAL
                        progresso.setProgress(progresso.getProgress() + (int)porcentagem);
                        aguaRestanteAux = aguaRestanteAux - finalSomar;
                        if(aguaRestanteAux < 0)
                            aguaRestanteAux = 0;
                        faltam.setText(String.valueOf(aguaRestanteAux));
                    }
                });



                /*----------------- FINAL DO COLOCANDO O BOTAO AQUI---------------*/



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        aguaQtd.setOnItemSelectedListener(escolha);

    }
}
