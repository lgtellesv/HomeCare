package android.usuario.HomeCare.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.usuario.HomeCare.model.Usuario;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USER = "user";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NOME = "user_nome";
    private static final String COLUMN_USER_NASC = "user_nasc";
    private static final String COLUMN_USER_SEXO = "user_sexo";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_SENHA = "user_senha";
    private static final String COLUMN_USER_PESO = "user_peso";
    private static final String COLUMN_USER_ALTURA = "user_altura";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NOME + " TEXT," + COLUMN_USER_NASC + " TEXT," + COLUMN_USER_SEXO + " TEXT," + COLUMN_USER_PESO + " TEXT,"
            + COLUMN_USER_ALTURA + " TEXT," + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_SENHA + " TEXT" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void adicionarUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NOME, usuario.getNome());
      //  values.put(COLUMN_USER_NASC, usuario.getDataNasc());
        values.put(COLUMN_USER_SEXO, usuario.getSexo());
        values.put(COLUMN_USER_SEXO, usuario.getPeso());
        values.put(COLUMN_USER_SEXO, usuario.getAltura());
        values.put(COLUMN_USER_EMAIL, usuario.getEmail());
        values.put(COLUMN_USER_SENHA, usuario.getSenha());

        db.insert(TABLE_USER,null, values);
        db.close();
    }

    public boolean buscarUsuario(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean buscarUsuario(String email, String senha){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_SENHA + " =?";
        String[] selectionArgs = { email, senha };

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
}
