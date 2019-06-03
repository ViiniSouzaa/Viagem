package carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Passageiro implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String nome;

    private String cpf;

    @NonNull
    private long idViagem;

    public Passageiro() {
    }

    protected Passageiro(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        cpf = in.readString();
        idViagem = in.readLong();
    }

    public static final Creator<Passageiro> CREATOR = new Creator<Passageiro>() {
        @Override
        public Passageiro createFromParcel(Parcel in) {
            return new Passageiro(in);
        }

        @Override
        public Passageiro[] newArray(int size) {
            return new Passageiro[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public long getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(long idViagem) {
        this.idViagem = idViagem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(cpf);
        dest.writeLong(idViagem);
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
