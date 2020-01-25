package com.example.proyectousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserAdminDashActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin_dash);

        preferences = getSharedPreferences("app#users", Context.MODE_PRIVATE);

        String usernameProp = getIntent().getStringExtra("username");
        user = Utils.findUserByUsername(preferences, usernameProp);

        if (user == null) {
            Toast.makeText(
                    this,
                    R.string.resultado_no_usuario,
                    Toast.LENGTH_SHORT
            ).show();
            UserAdminDashActivity.this.finish();
            return;
        }

        final Spinner spinnerListUsuarios = findViewById(R.id.lista_usuarios);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item
        );

        for (User otroUsuario : Utils.listUsers(preferences)) {
            adapter.add(otroUsuario.getNick());
        }

        spinnerListUsuarios.setAdapter(adapter);

        findViewById(R.id.accion_hacer_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUsername = (String) spinnerListUsuarios.getSelectedItem();
                User selectedUser = Utils.findUserByUsername(preferences, selectedUsername);

                if (selectedUser != null) {
                    selectedUser.setAdmin(true);
                    Utils.storeUser(preferences, selectedUser);
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_no_usuario,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        findViewById(R.id.accion_quitar_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUsername = (String) spinnerListUsuarios.getSelectedItem();
                User selectedUser = Utils.findUserByUsername(preferences, selectedUsername);

                if (selectedUser != null) {
                    selectedUser.setAdmin(false);
                    Utils.storeUser(preferences, selectedUser);
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_no_usuario,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        findViewById(R.id.accion_modificar_datos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUsername = (String) spinnerListUsuarios.getSelectedItem();
                User selectedUser = Utils.findUserByUsername(preferences, selectedUsername);

                if (selectedUser != null) {
                    Intent intent = new Intent(
                            v.getContext(),
                            UserDashActivity.class
                    );
                    intent.putExtra("username", selectedUser.getNick());
                    startActivity(intent);
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_no_usuario,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        findViewById(R.id.accion_borrar_cuenta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUsername = (String) spinnerListUsuarios.getSelectedItem();
                User selectedUser = Utils.findUserByUsername(preferences, selectedUsername);

                if (selectedUser != null) {
                    adapter.remove(selectedUsername);
                    Utils.deleteUser(preferences, selectedUser);
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_borrar_cuenta,
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            v.getContext(),
                            R.string.resultado_no_usuario,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}
