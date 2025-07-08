package com.example.vlcctrl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String ipAddress;
    private String password;

    private void sendCommand(String command) {
        if (ipAddress == null || password == null) {
            Toast.makeText(this, "尚未设置IP和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + ipAddress + ":8080/requests/status.xml?command=" + command;
        NetworkUtil.sendGetRequest(url, password, new NetworkUtil.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "命令已发送", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "发送失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });


    }

    private void showConfigDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入 VLC IP 和 密码");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_config, null);
        EditText ipInput = dialogView.findViewById(R.id.ip_input);
        EditText pwdInput = dialogView.findViewById(R.id.password_input);

        ipInput.setText(ipAddress != null ? ipAddress : "");
        pwdInput.setText(password != null ? password : "");

        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("保存", (dialog, which) -> {
            ipAddress = ipInput.getText().toString().trim();
            password = pwdInput.getText().toString().trim();
            SharedPrefUtil.saveIP(this, ipAddress);
            SharedPrefUtil.savePassword(this, password);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }

    private void initButtons() {
        findViewById(R.id.play).setOnClickListener(v -> sendCommand("pl_play"));
        findViewById(R.id.pause).setOnClickListener(v -> sendCommand("pl_pause"));
        findViewById(R.id.next).setOnClickListener(v -> sendCommand("pl_next"));
        findViewById(R.id.prev).setOnClickListener(v -> sendCommand("pl_previous"));
        findViewById(R.id.seek_fwd_10).setOnClickListener(v -> sendCommand("seek&val=+10s"));
        findViewById(R.id.seek_fwd_60).setOnClickListener(v -> sendCommand("seek&val=+60s"));
        findViewById(R.id.seek_back_10).setOnClickListener(v -> sendCommand("seek&val=-10s"));
        findViewById(R.id.seek_back_60).setOnClickListener(v -> sendCommand("seek&val=-60s"));

        findViewById(R.id.btn_config).setOnClickListener(v -> showConfigDialog());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("VLC遥控器");
        setContentView(R.layout.activity_main);

        ipAddress = SharedPrefUtil.getIP(this);
        password = SharedPrefUtil.getPassword(this);

        if (ipAddress == null || password == null) {
            showConfigDialog();
        }

        initButtons();
    }
}
