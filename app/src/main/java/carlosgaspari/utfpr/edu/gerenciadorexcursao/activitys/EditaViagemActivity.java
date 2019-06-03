package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Viagem;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class EditaViagemActivity extends AppCompatActivity {
    EditText editTextLocalizacao;
    ListView listViewPassageiros;
    List<Passageiro> passageiros;
    long id;
    Viagem viagem;

    private ActionMode actionMode;
    private int positionSelected = -1;
    private View viewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_viagem);

        editTextLocalizacao = findViewById(R.id.editTextLocalizacao);
        listViewPassageiros = findViewById(R.id.listViewPassageiros);

        listViewPassageiros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                editaSelecionado();
            }
        });

        listViewPassageiros.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewPassageiros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(actionMode != null){
                    return false;
                }
                positionSelected = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelected = view;
                listViewPassageiros.setEnabled(true);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });
        populaLista();
    }

    private void recuperaAnterior() {
        id = getIntent().getLongExtra("idViagem", -1);
        viagem = ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().getById(id);
        passageiros = ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().getByViagem(id);
        editTextLocalizacao.setText(viagem.getLocalizacao());
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contexto, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuItemAlterar :
                    editaSelecionado();
                    mode.finish();
                    return true;

                case R.id.menuItemExcluir :
                    deletaSelecionado();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelected != null){
                viewSelected.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelected = null;

            listViewPassageiros.setEnabled(true);
        }
    };

    private void deletaSelecionado() {
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.confirmar));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().delete(passageiros.get(positionSelected));
                populaLista();
            }
        });
        confirma.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirma.show();
    }

    private void editaSelecionado() {
        Intent intent = new Intent(this, EditaPassageiroBanco.class);
        intent.putExtra("id", passageiros.get(positionSelected).getId());
        intent.putExtra("idViagem", id);
        startActivity(intent);
    }

    private void populaLista() {
        recuperaAnterior();
        ArrayAdapter<Passageiro> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, passageiros);
        listViewPassageiros.setAdapter(adapter);
        registerForContextMenu(listViewPassageiros);
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
            viagem.setLocalizacao(editTextLocalizacao.getText().toString());
            ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().update(viagem);
            Intent intent = new Intent(this, PrincipalActivity.class);
            startActivity(intent);
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
    }

    public void adicionaPassageiro(View view){
        Intent intent = new Intent(this, PassageirosActivity.class);
        intent.putExtra("idViagem", viagem.getId());
        startActivity(intent);
        populaLista();
    }
}
