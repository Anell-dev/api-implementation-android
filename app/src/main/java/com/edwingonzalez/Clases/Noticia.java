package com.edwingonzalez.Clases;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edwingonzalez.HelperDb.MyHelperSqLite;

import java.util.ArrayList;

public class Noticia {
    private String id;
    private String titulo;
    private String autor;
    private String detalles;
    private String fecha;
    private String imagen;
    private String icono;
    MyHelperSqLite admin;
    SQLiteDatabase db;
    String BaseDatos= "basededatos.db";

    public Noticia() {
    }

    public Noticia(Activity activity2) {
        admin  = new MyHelperSqLite(activity2, BaseDatos, null, 1);
        db =  admin.getWritableDatabase();
    }

    public Noticia(String id, String titulo, String autor, String detalles, String fecha, String imagen, String icono) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.detalles = detalles;
        this.fecha = fecha;
        this.imagen = imagen;
        this.icono = icono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(int megusta) {
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public boolean insertar(){
        ContentValues registro = new ContentValues();
        registro.put("id_news", this.id);
        db.insert("news", null, registro);
        db.close();
        return true;
    }

    public boolean eliminar(){
        db.delete("news","id_news="+this.id, null);
        db.close();
        return true;
    }

    public boolean existe(String id) {
        Cursor cursor = db.rawQuery("SELECT id_news FROM news WHERE id_news = ?", new String[]{id});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }


    public ArrayList<Noticia> ListarTodos(){
        ArrayList<Noticia> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select id_news from news", null);

        while (cursor.moveToNext()){
            Noticia n = new Noticia();
            n.id = cursor.getString(0);
            lista.add(n);
        }

//       cursor.close();
        db.close();
        return lista;
    }

}
