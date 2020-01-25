package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegisterActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        preferences = getSharedPreferences("app#users", Context.MODE_PRIVATE);

        findViewById(R.id.boton_registrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView campoUsuario = findViewById(R.id.campo_usuario_registro);
                TextView campoNombre = findViewById(R.id.campo_nombre_registro);
                TextView campoApellidos = findViewById(R.id.campo_apellidos_registro);
                TextView campoDni = findViewById(R.id.campo_dni_registro);
                TextView campoContrasena = findViewById(R.id.campo_contrasena_registro);
                if (!Utils.checkTextInput(campoUsuario, campoNombre, campoApellidos, campoDni, campoContrasena)) return;

                User user = Utils.findUserByUsername(preferences, campoUsuario.getText().toString());
                if (user == null) {
                    user = new User(
                            -1,
                            campoUsuario.getText().toString(),
                            campoNombre.getText().toString(),
                            campoApellidos.getText().toString(),
                            campoContrasena.getText().toString(),
                            campoDni.getText().toString(),
                            false
                    );

                    Utils.createUser(preferences, user);
                    UserRegisterActivity.this.finish();

                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_alta_correcta,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_usuario_existe,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}
