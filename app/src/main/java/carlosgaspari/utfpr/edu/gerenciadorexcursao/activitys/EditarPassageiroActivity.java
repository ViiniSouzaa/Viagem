package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class EditarPassageiroActivity extends AppCompatActivity {

    EditText editTextNome, editTextCPF;
    Intent intent;
    Passageiro passageiro;
    ArrayList<Passageiro> passageiros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_passageiro_activity);

        intent = getIntent();
        editTextNome = findViewById(R.id.editTextPassageiroNome2);
        editTextCPF = findViewById(R.id.editTextPassageiroCPF2);

        recuperaPassageiros();
    }

    private void recuperaPassageiros() {
        passageiros = intent.getParcelableArrayListExtra("passageiros");
        passageiro = intent.getParcelableExtra("passageiro");

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
            passageiros.remove(intent.getIntExtra("posicao", -1));
            passageiro.setNome(editTextNome.getText().toString());
            passageiro.setCpf(editTextCPF.getText().toString());
            passageiros.add(passageiro);

            Intent intent = new Intent(getApplicationContext(), ViagensActivity.class);
            intent.putParcelableArrayListExtra("passageiros", passageiros);
            startActivity(intent);
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
    }

}
