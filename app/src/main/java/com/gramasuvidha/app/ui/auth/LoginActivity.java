package com.gramasuvidha.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.repository.AuthRepository;
import com.gramasuvidha.app.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private AuthRepository authRepo;
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private TextView tvGoRegister, tvDemoHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authRepo = new AuthRepository(this);

        // Skip login if already logged in
        if (authRepo.isLoggedIn()) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.et_login_phone);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvGoRegister = findViewById(R.id.tv_go_register);
        tvDemoHint = findViewById(R.id.tv_demo_hint);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        String result = authRepo.login(phone, password);

        if ("success".equals(result)) {
            Toast.makeText(this, "Welcome back! 🎉", Toast.LENGTH_SHORT).show();
            goToMain();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            btnLogin.setEnabled(true);
            btnLogin.setText("Login");
        }
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
