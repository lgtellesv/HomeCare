package android.usuario.HomeCare.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.usuario.HomeCare.R;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

public class Validacao {

    private Context context;

    public Validacao(Context context){
        this.context = context;
    }

    public boolean verificaPreenchimento(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String mensagem){
        String preenchimento = textInputEditText.getText().toString().trim();
        if (preenchimento.isEmpty()){
            textInputLayout.setError(mensagem);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean verificaSelecaoSexo(RadioGroup radioGroup, TextInputLayout textInputLayout, String mensagem) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.masculino:
                textInputLayout.setErrorEnabled(false);
                return true;
            case R.id.feminino:
                textInputLayout.setErrorEnabled(false);
                return true;
            default:
                textInputLayout.setError(mensagem);
        }
        return false;
    }


    public boolean verificaPadraoEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String mensagem){
        String email = textInputEditText.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textInputLayout.setError(mensagem);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean confirmaSenha(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String mensagem){
        String senha = textInputEditText1.getText().toString().trim();
        String confirmacaoSenha = textInputEditText2.getText().toString().trim();
        if (!senha.contentEquals(confirmacaoSenha)){
            textInputLayout.setError(mensagem);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}


