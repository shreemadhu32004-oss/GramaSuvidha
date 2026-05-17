package com.gramasuvidha.app.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gramasuvidha.app.model.User;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AuthRepository {

    private static final String PREFS_NAME = "GramaSuvidhaAuth";
    private static final String KEY_USERS = "registered_users";
    private static final String KEY_LOGGED_IN = "logged_in_phone";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences prefs;
    private final Gson gson;

    public AuthRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        seedDefaultUsers();
    }

    // Seed demo users on first launch
    private void seedDefaultUsers() {
        if (prefs.getString(KEY_USERS, null) != null) return;
        List<User> defaults = new ArrayList<>();
        defaults.add(new User("Rahul Awati", "9999999999", "Halasuru", "admin123", "admin"));
        defaults.add(new User("Demo Citizen", "8888888888", "Kengeri", "demo123", "citizen"));
        prefs.edit().putString(KEY_USERS, gson.toJson(defaults)).apply();
    }

    public String register(String name, String phone, String village, String password) {
        if (name.isEmpty() || phone.isEmpty() || village.isEmpty() || password.isEmpty())
            return "All fields are required";
        if (phone.length() != 10)
            return "Enter a valid 10-digit phone number";
        if (password.length() < 6)
            return "Password must be at least 6 characters";

        List<User> users = getAllUsers();
        for (User u : users) {
            if (u.getPhone().equals(phone))
                return "Phone number already registered";
        }

        users.add(new User(name, phone, village, password, "citizen"));
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply();
        return "success";
    }

    public String login(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty())
            return "Please enter phone and password";

        List<User> users = getAllUsers();
        for (User u : users) {
            if (u.getPhone().equals(phone) && u.getPassword().equals(password)) {
                prefs.edit()
                        .putString(KEY_LOGGED_IN, phone)
                        .putBoolean(KEY_IS_LOGGED_IN, true)
                        .apply();
                return "success";
            }
        }
        return "Invalid phone number or password";
    }

    public void logout() {
        prefs.edit()
                .remove(KEY_LOGGED_IN)
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getLoggedInUser() {
        String phone = prefs.getString(KEY_LOGGED_IN, null);
        if (phone == null) return null;
        for (User u : getAllUsers()) {
            if (u.getPhone().equals(phone)) return u;
        }
        return null;
    }

    private List<User> getAllUsers() {
        String json = prefs.getString(KEY_USERS, "[]");
        Type type = new TypeToken<List<User>>() {}.getType();
        List<User> users = gson.fromJson(json, type);
        return users != null ? users : new ArrayList<>();
    }
}
