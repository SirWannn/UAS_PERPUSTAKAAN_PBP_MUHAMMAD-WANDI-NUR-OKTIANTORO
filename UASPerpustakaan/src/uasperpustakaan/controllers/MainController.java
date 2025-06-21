package uasperpustakaan.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uasperpustakaan.dao.UserDAO;
import uasperpustakaan.models.Session;
import uasperpustakaan.models.User;

public class MainController {

    @FXML
    private Label labelerorr;

    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private void handleButtonLoginAction(ActionEvent event) {
        checkLogin();
    }

    private void checkLogin() {
        String username = inputUsername.getText().trim();
        String password = inputPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            labelerorr.setText("Isi username dan password");
            return;
        }

        try {
            User user = UserDAO.getAccount(username, password);

            if (user == null) {
                labelerorr.setText("Username atau password salah");
                return;
            }

            if (!user.getRole().equalsIgnoreCase("admin")) {
                labelerorr.setText("Akses ditolak. Hanya admin yang bisa login.");
                return;
            }

            // Login berhasil, simpan ke session
            Session.getInstance().setUser(user);
            labelerorr.setText("Login berhasil");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uasperpustakaan/views/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            labelerorr.setText("Terjadi kesalahan saat login.");
        }
    }
}
