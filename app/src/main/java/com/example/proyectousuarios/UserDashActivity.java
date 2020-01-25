package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserDashActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash);

        preferences = getSharedPreferences("app#users", Context.MODE_PRIVATE);

        String usernameProp = getIntent().getStringExtra("username");
        user = Utils.findUserByUsername(preferences, usernameProp);

        if (user == null) {
            Toast.makeText(
                    this,
                    R.string.resultado_no_usuario,
                    Toast.LENGTH_SHORT
            ).show();
            UserDashActivity.this.finish();
            return;
        }

        final EditText campoNick = findViewById(R.id.campo_usuario_dash);
        final EditText campoNombre = findViewById(R.id.campo_nombre_dash);
        final EditText campoApellidos = findViewById(R.id.campo_apellidos_dash);
        final EditText campoDni = findViewById(R.id.campo_dni_dash);
        final EditText campoContrasena = findViewById(R.id.campo_contrasena_dash);

        campoNick.setText(user.getNick());
        campoNombre.setText(user.getNombre());
        campoApellidos.setText(user.getApellidos());
        campoDni.setText(user.getDni());
        campoContrasena.setText(user.getContrasena());

        findViewById(R.id.boton_guardar_cambios_dash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setNick(campoNick.getText().toString());
                user.setNombre(campoNombre.getText().toString());
                user.setApellidos(campoApellidos.getText().toString());
                user.setDni(campoDni.getText().toString());
                user.setContrasena(campoContrasena.getText().toString());

                User usuarioExistente = Utils.findUserByUsername(preferences, user.getNick());
                if (usuarioExistente == null || usuarioExistente.getId() == user.getId()) {
                    Utils.storeUser(preferences, user);
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_guardar_cuenta,
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

        findViewById(R.id.boton_borrar_cuenta_dash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.deleteUser(preferences, user);
                Toast.makeText(
                        v.getContext(),
                        R.string.resultado_borrar_cuenta,
                        Toast.LENGTH_SHORT
                ).show();
                UserDashActivity.this.finish();
            }
        });
    }
}
