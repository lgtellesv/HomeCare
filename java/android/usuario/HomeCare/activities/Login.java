package android.usuario.HomeCare.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.usuario.HomeCare.R;
import android.usuario.HomeCare.helper.Validacao;
import android.usuario.HomeCare.sql.DatabaseHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Login.this;
    private ConstraintLayout constraintLayout;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutSenha;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextSenha;
    private Button btn_login;
    private TextView link_registrar;
    private Validacao validacao;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarViews();
        iniciarListeners();
        iniciarObjects();
    }

    private void iniciarViews(){
        constraintLayout = (ConstraintLayout) findViewById(R.id.tela1);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail2);
        textInputLayoutSenha = (TextInputLayout) findViewById(R.id.textInputLayoutSenha2);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail2);
        textInputEditTextSenha = (TextInputEditText) findViewById(R.id.textInputEditTextSenha2);
        btn_login = (Button) findViewById(R.id.btn_login);
        link_registrar = (TextView) findViewById(R.id.link_registrar);
    }

    private void iniciarListeners(){
        btn_login.setOnClickListener(this);
        link_registrar.setOnClickListener(this);
    }

    private void iniciarObjects(){
        databaseHelper = new DatabaseHelper(activity);
        validacao = new Validacao(activity);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                verificarNoSQLite();
                break;
            case R.id.link_registrar:
                Intent intentRegister = new Intent(getApplicationContext(), Cadastro.class);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.anima_direita_in, R.anim.anima_esquerda_out);
                break;
        }
    }

    private void verificarNoSQLite(){
        if (!validacao.verificaPreenchimento(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_vazio))) {
            return;
        }
        if (!validacao.verificaPadraoEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_invalido))){
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextSenha, textInputLayoutSenha, getString(R.string.senha_vazio))){
            return;
        }
        if (databaseHelper.buscarUsuario(textInputEditTextEmail.getText().toString().trim(), textInputEditTextSenha.getText().toString().trim())){
            Intent contaUsuario = new Intent(activity, MainActivity.class);
            contaUsuario.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(contaUsuario);
            overridePendingTransition(R.anim.anima_direita_in, R.anim.anima_esquerda_out);
            finish();
        } else {
            Snackbar.make(constraintLayout, getString(R.string.nao_encontrado), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextSenha.setText(null);
    }

}
