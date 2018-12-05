package android.usuario.HomeCare.activities;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.usuario.HomeCare.R;
import android.usuario.HomeCare.helper.Validacao;
import android.usuario.HomeCare.model.Usuario;
import android.usuario.HomeCare.sql.DatabaseHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = Cadastro.this;

    private ConstraintLayout constraintLayout;
    private TextInputLayout textInputLayoutNome;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutSenha;
    private TextInputLayout textInputLayoutConfirmaSenha;
    private TextInputLayout textInputLayoutPeso;
    private TextInputLayout textInputLayoutAltura;
    private TextInputLayout textInputLayoutSexo;

    private TextInputEditText textInputEditTextNome;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextSenha;
    private TextInputEditText textInputEditTextConfirmaSenha;
    private TextInputEditText textInputEditTextPeso;
    private TextInputEditText textInputEditTextAltura;
    private RadioGroup radioGroupSexo;
    private String sexo;

    private Button btn_registrar;
    private TextView link_login;

    private Validacao validacao;
    private DatabaseHelper databaseHelper;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarViews();
        iniciarListeners();
        iniciarObjects();
    }



    private void iniciarViews(){
        constraintLayout = (ConstraintLayout) findViewById(R.id.tela2);
        textInputLayoutNome = (TextInputLayout) findViewById(R.id.textInputLayoutNome);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutSenha = (TextInputLayout) findViewById(R.id.textInputLayoutSenha);
        textInputLayoutConfirmaSenha = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmaSenha);
        textInputLayoutPeso = (TextInputLayout) findViewById(R.id.textInputLayoutPeso);
        textInputLayoutAltura = (TextInputLayout) findViewById(R.id.textInputLayoutAltura);
        textInputLayoutSexo = (TextInputLayout) findViewById(R.id.textInputLayoutRadioGroup);

        textInputEditTextNome = (TextInputEditText) findViewById(R.id.textInputEditTextNome);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextSenha = (TextInputEditText) findViewById(R.id.textInputEditTextSenha);
        textInputEditTextConfirmaSenha = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmaSenha);
        textInputEditTextPeso = (TextInputEditText) findViewById(R.id.textInputEditTextPeso);
        textInputEditTextAltura = (TextInputEditText) findViewById(R.id.textInputEditTextAltura);
        radioGroupSexo = (RadioGroup) findViewById(R.id.sexo);

        btn_registrar = (Button) findViewById(R.id.btn_registrar);
        link_login = (TextView) findViewById(R.id.link_login);
    }

    private void iniciarListeners(){

        btn_registrar.setOnClickListener(this);
        link_login.setOnClickListener(this);
    }

    private void iniciarObjects(){
        validacao = new Validacao(activity);
        databaseHelper = new DatabaseHelper(activity);
        usuario = new Usuario();
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_registrar:
                InserirDadosNoSQLite();
                break;
            case R.id.link_login:
                finish();
                break;
        }
    }

    private void InserirDadosNoSQLite(){

        if (!validacao.verificaSelecaoSexo(radioGroupSexo, textInputLayoutSexo, getString(R.string.sexo_vazio))) {
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextNome, textInputLayoutNome, getString(R.string.nome_vazio))) {
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextPeso, textInputLayoutPeso, getString(R.string.peso_vazio))) {
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextAltura, textInputLayoutAltura, getString(R.string.altura_vazio))) {
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_vazio))) {
            return;
        }
        if (!validacao.verificaPadraoEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_invalido))) {
            return;
        }
        if (!validacao.verificaPreenchimento(textInputEditTextSenha, textInputLayoutSenha, getString(R.string.senha_vazio))) {
            return;
        }
        if (!validacao.confirmaSenha(textInputEditTextSenha, textInputEditTextConfirmaSenha, textInputLayoutConfirmaSenha,
                getString(R.string.senhaConfirma))) {
            return;
        }

        if (!databaseHelper.buscarUsuario(textInputEditTextEmail.getText().toString().trim())){
            usuario.setNome(textInputEditTextNome.getText().toString().trim());
            usuario.setEmail(textInputEditTextEmail.getText().toString().trim());
            usuario.setSenha(textInputEditTextSenha.getText().toString().trim());
            usuario.setPeso(textInputEditTextPeso.getText().toString().trim());
            usuario.setAltura(textInputEditTextAltura.getText().toString().trim());
            usuario.setSexo(getSexo(sexo).toString().trim());

            databaseHelper.adicionarUsuario(usuario);

            emptyInputEditText();
            Snackbar.make(constraintLayout, getString(R.string.registro_efetuado), Snackbar.LENGTH_LONG).show();

        } else {
            Snackbar.make(constraintLayout, getString(R.string.emailJaExiste), Snackbar.LENGTH_LONG).show();
        }

    }

    public String getSexo(String sexo) {

        if (radioGroupSexo.getCheckedRadioButtonId() == R.id.masculino) {
            return "Masculino";
        }else
            return "Feminino";
        }


    private void emptyInputEditText(){
        textInputEditTextNome.setText(null);
        textInputEditTextPeso.setText(null);
        textInputEditTextAltura.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextSenha.setText(null);
        textInputEditTextConfirmaSenha.setText(null);

    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.anima_esquerda_in, R.anim.anima_direita_out);
    }
}
