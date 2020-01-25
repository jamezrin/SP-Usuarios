package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        findViewById(R.id.boton_registrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView campoUsuario = findViewById(R.id.campo_usuario_registro);
                TextView campoNombre = findViewById(R.id.campo_nombre_registro);
                TextView campoApellidos = findViewById(R.id.campo_apellidos_registro);
                TextView campoDni = findViewById(R.id.campo_dni_registro);
                TextView campoContrasena = findViewById(R.id.campo_contrasena_registro);
                if (!Utils.checkTextInput(campoUsuario, campoNombre, campoApellidos, campoDni, campoContrasena)) return;
                UsuariosStore usuariosStore = (UsuariosStore) getIntent().getSerializableExtra("usuarios_store");

                for (User otro : usuariosStore.getUsuarios()) {
                    if (otro.getNick().equals(campoUsuario.getText().toString())
                        || otro.getDni().equals(campoDni.getText().toString())) {
                        Toast.makeText(
                                v.getContext(),
                                "Ya existe un user con ese DNI o nick",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }
                }

                User user = new User(
                        campoUsuario.getText().toString(),
                        campoNombre.getText().toString(),
                        campoApellidos.getText().toString(),
                        campoContrasena.getText().toString(),
                        campoDni.getText().toString(),
                        TipoUsuario.REGULAR
                );

                usuariosStore.getUsuarios().add(user);

                UserRegisterActivity.this.finish();
                Toast.makeText(v.getContext(), "Se te ha dado de alta correctamente", Toast.LENGTH_LONG).show();
            }
        });
    }
}
