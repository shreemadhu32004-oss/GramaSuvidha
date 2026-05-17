package com.gramasuvidha.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.repository.AuthRepository;
import com.gramasuvidha.app.ui.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private AuthRepository authRepo;
    private EditText etName, etPhone, etVillage, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepo = new AuthRepository(this);

        etName = findViewById(R.id.et_reg_name);
        etPhone = findViewById(R.id.et_reg_phone);
        etVillage = findViewById(R.id.et_reg_village);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvGoLogin = findViewById(R.id.tv_go_login);

        btnRegister.setOnClickListener(v -> attemptRegister());
        tvGoLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String village = etVillage.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Registering...");

        String result = authRepo.register(name, phone, village, password);

        if ("success".equals(result)) {
            authRepo.login(phone, password);
            Toast.makeText(this, "Account created successfully! 🎉", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            btnRegister.setEnabled(true);
            btnRegister.setText("Create Account");
        }
    }
}
