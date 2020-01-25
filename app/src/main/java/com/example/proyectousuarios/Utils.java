package com.example.proyectousuarios;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean checkTextInput(TextView... views) {
        boolean result = true;

        for (TextView view : views) {
            if (view.length() == 0) {
                view.setError("Este campo es obligatorio");
                result = false;
            }
        }

        return result;
    }

    public static void createUser(SharedPreferences sharedPreferences, User user) {
        int currentIdx = sharedPreferences.getInt("metadata#index", 0);
        user.setId(currentIdx);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("metadata#index", currentIdx + 1).apply();
        editor.apply();

        storeUser(sharedPreferences, user);
    }

    public static void storeUser(SharedPreferences sharedPreferences, User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("instance#" + user.getId(), 0); // 0 es OK, 1 es GONE
        editor.putString("property#" + user.getId() + "#nick", user.getNick());
        editor.putString("property#" + user.getId() + "#nombre", user.getNombre());
        editor.putString("property#" + user.getId() + "#apellidos", user.getApellidos());
        editor.putString("property#" + user.getId() + "#contrasena", user.getContrasena());
        editor.putString("property#" + user.getId() + "#dni", user.getDni());
        editor.putBoolean("property#" + user.getId() + "#admin", user.isAdmin());
        editor.apply();
    }

    public static void deleteUser(SharedPreferences sharedPreferences, User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("instance#" + user.getId());
        editor.remove("property#" + user.getId() + "#nick");
        editor.remove("property#" + user.getId() + "#nombre");
        editor.remove("property#" + user.getId() + "#apellidos");
        editor.remove("property#" + user.getId() + "#contrasena");
        editor.remove("property#" + user.getId() + "#dni");
        editor.remove("property#" + user.getId() + "#admin");
        editor.apply();
    }

    public static User fetchUser(SharedPreferences sharedPreferences, int id) {
        if (!sharedPreferences.contains("instance#" + id)) {
            return null;
        }

        return new User(
                id,
                sharedPreferences.getString("property#" + id + "#nick", "unknown"),
                sharedPreferences.getString("property#" + id + "#nombre", "unknown"),
                sharedPreferences.getString("property#" + id + "#apellidos", "unknown"),
                sharedPreferences.getString("property#" + id + "#contrasena", "unknown"),
                sharedPreferences.getString("property#" + id + "#dni", "unknown"),
                sharedPreferences.getBoolean("property#" + id + "#admin", false)
        );
    }

    public static User findUserByUsername(SharedPreferences sharedPreferences, String username) {
        int currentIdx = sharedPreferences.getInt("metadata#index", 0);

        for (int i = 0; i < currentIdx; i++) {
            User user = fetchUser(sharedPreferences, i);
            if (user != null && user.getNick().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static List<User> listUsers(SharedPreferences sharedPreferences) {
        int currentIdx = sharedPreferences.getInt("metadata#index", 0);
        List<User> users = new ArrayList<>();

        for (int i = 0; i < currentIdx; i++) {
            User user = fetchUser(sharedPreferences, i);
            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }
}
