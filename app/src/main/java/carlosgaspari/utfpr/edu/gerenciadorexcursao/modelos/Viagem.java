package carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Viagem {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String localizacao;

    public Viagem() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public String toString() {
        return this.getLocalizacao();
    }
}
