package carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Viagem;

@Dao
public interface DaoViagem {
    @Insert
    long insert(Viagem viagem);

    @Delete
    void delete(Viagem viagem);

    @Update
    void update(Viagem viagem);

    @Query("SELECT * FROM viagem WHERE id = :id")
    Viagem getById(long id);

    @Query("SELECT * FROM viagem ORDER BY localizacao ASC")
    List<Viagem> getAll();

    @Query("SELECT * FROM viagem WHERE id = (SELECT MAX(id) from viagem)")
    Viagem getLast();
}
