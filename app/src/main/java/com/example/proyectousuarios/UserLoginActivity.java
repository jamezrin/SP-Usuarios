package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    private void initializeSystemUsers() {
        preferences = getSharedPreferences("app#users", Context.MODE_PRIVATE);

        User user = Utils.findUserByUsername(preferences, "admin");

        if (user == null || !user.isAdmin()) {
            user = new User();
            user.setNick("admin");
            user.setContrasena("superpass");
            user.setDni("00000000F");
            user.setNombre("Administrador");
            user.setApellidos("bueno");
            user.setAdmin(true);

            Utils.createUser(preferences, user);

            Toast.makeText(
                    this,
                    getString(R.string.usuario_defecto,
                            user.getNick(),
                            user.getContrasena()),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initializeSystemUsers();

        findViewById(R.id.boton_iniciar_sesion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView campoUsuario = findViewById(R.id.campo_usuario_login);
                TextView campoContrasena = findViewById(R.id.campo_contrasena_login);
                if (!Utils.checkTextInput(campoUsuario, campoContrasena)) return;

                User user = Utils.findUserByUsername(preferences, campoUsuario.getText().toString());

                if (user != null) {
                    if (user.getContrasena().equals(campoContrasena.getText().toString())) {
                        Intent intent = new Intent(
                                v.getContext(),
                                user.isAdmin() ?
                                        UserAdminDashActivity.class :
                                        UserDashActivity.class
                        );
                        intent.putExtra("username", campoUsuario.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(
                                v.getContext(),
                                R.string.resultado_contrasena_incorrecta,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_no_usuario,
                            Toast.LENGTH_SHORT
                    ).show();
                }
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
