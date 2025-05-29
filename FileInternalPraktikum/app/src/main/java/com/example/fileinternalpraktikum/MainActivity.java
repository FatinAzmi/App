package com.example.fileinternalpraktikum;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText etFileName, etFileContent;
    private TextView tvOutput;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etFileName = findViewById(R.id.etFileName);
        etFileContent = findViewById(R.id.etFileContent);
        tvOutput = findViewById(R.id.tvOutput);
        btnCreate = findViewById(R.id.btnCreate);
        btnRead = findViewById(R.id.btnRead);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Set button listeners
        btnCreate.setOnClickListener(v -> createFile());
        btnRead.setOnClickListener(v -> readFile());
        btnUpdate.setOnClickListener(v -> updateFile());
        btnDelete.setOnClickListener(v -> deleteFile());
    }

    private void createFile() {
        String fileName = etFileName.getText().toString().trim();
        String content = etFileContent.getText().toString();

        if (fileName.isEmpty()) {
            showToast("Nama file tidak boleh kosong!");
            return;
        }

        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();

            showOutput("✅ FILE BERHASIL DIBUAT!\n" +
                    "Nama: " + fileName + "\n" +
                    "Isi: " + content + "\n" +
                    "Waktu: " + getCurrentTime());
            showToast("File berhasil dibuat!");

        } catch (IOException e) {
            showOutput("❌ ERROR: Gagal membuat file\n" + e.getMessage());
            showToast("Gagal membuat file!");
        }
    }

    private void readFile() {
        String fileName = etFileName.getText().toString().trim();

        if (fileName.isEmpty()) {
            showToast("Nama file tidak boleh kosong!");
            return;
        }

        try {
            FileInputStream fis = openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            String content = new String(buffer);
            showOutput("📖 FILE BERHASIL DIBACA!\n" +
                    "Nama: " + fileName + "\n" +
                    "Isi: " + content + "\n" +
                    "Ukuran: " + buffer.length + " bytes");

            // Set content ke EditText untuk memudahkan edit
            etFileContent.setText(content);
            showToast("File berhasil dibaca!");

        } catch (IOException e) {
            showOutput("❌ ERROR: File tidak ditemukan atau gagal dibaca\n" +
                    "Pastikan file sudah dibuat terlebih dahulu!");
            showToast("File tidak ditemukan!");
        }
    }

    private void updateFile() {
        String fileName = etFileName.getText().toString().trim();
        String newContent = etFileContent.getText().toString();

        if (fileName.isEmpty()) {
            showToast("Nama file tidak boleh kosong!");
            return;
        }

        // Cek apakah file ada
        File file = new File(getFilesDir(), fileName);
        if (!file.exists()) {
            showOutput("❌ ERROR: File tidak ditemukan!\n" +
                    "Buat file terlebih dahulu sebelum mengubah.");
            showToast("File tidak ditemukan!");
            return;
        }

        try {
            // Baca isi file lama
            FileInputStream fis = openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            String oldContent = new String(buffer);

            // Tulis isi baru
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(newContent.getBytes());
            fos.close();

            showOutput("🔄 FILE BERHASIL DIUBAH!\n" +
                    "Nama: " + fileName + "\n" +
                    "Isi Lama: " + oldContent + "\n" +
                    "Isi Baru: " + newContent + "\n" +
                    "Waktu: " + getCurrentTime());
            showToast("File berhasil diubah!");

        } catch (IOException e) {
            showOutput("❌ ERROR: Gagal mengubah file\n" + e.getMessage());
            showToast("Gagal mengubah file!");
        }
    }

    private void deleteFile() {
        String fileName = etFileName.getText().toString().trim();

        if (fileName.isEmpty()) {
            showToast("Nama file tidak boleh kosong!");
            return;
        }

        File file = new File(getFilesDir(), fileName);

        if (file.exists()) {
            boolean deleted = deleteFile(fileName);
            if (deleted) {
                showOutput("🗑️ FILE BERHASIL DIHAPUS!\n" +
                        "Nama: " + fileName + "\n" +
                        "Waktu: " + getCurrentTime());
                showToast("File berhasil dihapus!");
                etFileContent.setText(""); // Clear content
            } else {
                showOutput("❌ ERROR: Gagal menghapus file");
                showToast("Gagal menghapus file!");
            }
        } else {
            showOutput("❌ ERROR: File tidak ditemukan!\n" +
                    "File '" + fileName + "' tidak ada.");
            showToast("File tidak ditemukan!");
        }
    }


    private void showOutput(String message) {
        tvOutput.setText(message);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentTime() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                java.util.Locale.getDefault()).format(new java.util.Date());
    }
}