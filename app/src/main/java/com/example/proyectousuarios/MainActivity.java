package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    private void inicializarSistemaUsuarios() {
        preferences = getSharedPreferences("shpr_app", Context.MODE_PRIVATE);

        User userDefault1 = new User();
        userDefault1.setNick("admin");
        userDefault1.setContrasena("superpass");
        userDefault1.setDni("00000000F");
        userDefault1.setNombre("Administrador");
        userDefault1.setApellidos("bueno");
        usuariosDefault.getUsuarios().add(userDefault1);

        Toast.makeText(this,
                getString(R.string.usuario_defecto,
                        userDefault1.getNick(),
                        userDefault1.getContrasena()),
                Toast.LENGTH_LONG
        ).show();

        usuariosStore = gson.fromJson(
                preferences.getString(
                        "usuarios_json",
                        gson.toJson(usuariosDefault)),
                UsuariosStore.class
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuarios_json", gson.toJson(usuariosStore));
        if (!editor.commit()) {
            Toast.makeText(
                    this,
                    "No se han podido guardar los usuarios",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarSistemaUsuarios();

        findViewById(R.id.boton_iniciar_sesion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView campoUsuario = findViewById(R.id.campo_usuario_login);
                TextView campoContrasena = findViewById(R.id.campo_contrasena_login);
                if (!Utils.checkTextInput(campoUsuario, campoContrasena)) return;

                for (User user : usuariosStore.getUsuarios()) {
                    if (user.getNick().equals(campoUsuario.getText().toString()) &&
                            user.getContrasena().equals(campoContrasena.getText().toString())) {
                        Intent intent = new Intent(
                                v.getContext(),
                                user.getTipo() == TipoUsuario.ADMIN ?
                                        UserAdminDashActivity.class :
                                        UserDashActivity.class
                        );

                        startActivity(intent);
                        MainActivity.this.finish();

                        return;
                    }
                }

                Toast.makeText(v.getContext(), R.string.error_no_usuario, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.boton_actividad_registro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
