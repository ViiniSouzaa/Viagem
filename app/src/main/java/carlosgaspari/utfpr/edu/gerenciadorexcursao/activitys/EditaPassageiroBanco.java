package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class EditaPassageiroBanco extends AppCompatActivity {
    private static final String ARQUIVO = "carlosgaspari.utfpr.edu.gerenciadorexcursao.TEMA";
    private static final String OPCAO = "TEMA DARK";

    boolean tema;

    EditText editTextNome, editTextCPF;
    long id;
    Intent intent;
    Passageiro passageiro;
    long idViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_passageiro_activity);

        intent = getIntent();
        editTextNome = findViewById(R.id.editTextPassageiroNome2);
        editTextCPF = findViewById(R.id.editTextPassageiroCPF2);

        recuperaPassageiros();
        lerPreferencia();
    }

    private void recuperaPassageiros() {
        idViagem = intent.getLongExtra("idViagem", -1);
        id = intent.getLongExtra("id", -1);
        passageiro = ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().getById(id);

        editTextNome.setText(passageiro.getNome());
        editTextCPF.setText(passageiro.getCpf());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int add = item.getItemId();

        if(add == R.id.menuItemSalvar){
            passageiro.setNome(editTextNome.getText().toString());
            passageiro.setCpf(editTextCPF.getText().toString());
            ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().update(passageiro);

            Intent intent = new Intent(getApplicationContext(), EditaViagemActivity.class);
            intent.putExtra("idViagem", idViagem);
            startActivity(intent);
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
    }

    public void lerPreferencia(){
        SharedPreferences s = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        tema = s.getBoolean(OPCAO, false);

        muda();
    }

    private void muda() {
        if(tema){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(!tema){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
