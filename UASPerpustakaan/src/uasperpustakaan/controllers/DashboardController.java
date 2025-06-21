package uasperpustakaan.controllers;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uasperpustakaan.dao.BukuDAO;
import uasperpustakaan.dao.KategoriBukuDAO;
import uasperpustakaan.models.Buku;
import uasperpustakaan.models.KategoriBuku;

public class DashboardController implements Initializable {

    @FXML
    private ComboBox<KategoriBuku> cbxKategori1, cbxKategori2;
    @FXML
    private ComboBox<String> cbxSorting;
    @FXML
    private DatePicker dpcDari, dpcSampai, dpcPilih;
    @FXML
    private TextField txtJudul, tfKodeBuku, tfJudul, tfPengarang, tfPenerbit, tfTahun, tfEdisi;
    @FXML
    private TableView<Buku> tblViewBuku;
    @FXML
    private TableColumn<Buku, String> colJudul, colKategori, colKodeBuku, colPengarang, colPenerbit, colPengadaan;
    @FXML
    private TableColumn<Buku, Integer> colTahun, colEdisi;
    @FXML
    private Button add, update, delete;

    private ObservableList<Buku> bukuList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComboBoxKategori();
        initComboBoxSorting();
        initTableViewBuku();
        tfKodeBuku.setText(BukuDAO.generateKodeBukuOtomatis());
        loadDataBuku();

        tblViewBuku.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> selectBuku(newVal)
        );

        cbxKategori2.setOnAction(e -> tfKodeBuku.setText(BukuDAO.generateKodeBukuOtomatis()));
    }

    private void initComboBoxKategori() {
        ObservableList<KategoriBuku> kategoriList = FXCollections.observableArrayList(KategoriBukuDAO.getAllKategoriBuku());
        cbxKategori1.setItems(kategoriList);
        cbxKategori2.setItems(kategoriList);
    }

    private void initComboBoxSorting() {
        cbxSorting.setItems(FXCollections.observableArrayList("", "Judul A-Z", "Judul Z-A", "Pengadaan Terbaru", "Pengadaan Lama"));
        cbxSorting.getSelectionModel().selectFirst();
    }

    private void initTableViewBuku() {
        colKodeBuku.setCellValueFactory(new PropertyValueFactory<>("kodeBuku"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));
        colJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        colPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        colPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        colTahun.setCellValueFactory(new PropertyValueFactory<>("tahun"));
        colEdisi.setCellValueFactory(new PropertyValueFactory<>("edisi"));
        colPengadaan.setCellValueFactory(new PropertyValueFactory<>("tanggalPengadaan"));
    }

    private void loadDataBuku() {
        bukuList = FXCollections.observableArrayList(BukuDAO.getAllBuku());
        tblViewBuku.setItems(bukuList);
    }

    @FXML
    private void handleBtnFilter(ActionEvent event) {
        String keyword = txtJudul.getText();
        BukuDAO.filterJudul = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        BukuDAO.filterKategori = (cbxKategori1.getValue() != null) ? cbxKategori1.getValue().getKodeKategori() : null;
        BukuDAO.filterTanggalDari = (dpcDari.getValue() != null) ? dpcDari.getValue().toString() : null;
        BukuDAO.filterTanggalSampai = (dpcSampai.getValue() != null) ? dpcSampai.getValue().toString() : null;
        BukuDAO.sortingOption = cbxSorting.getValue();
        loadDataBuku();
    }

    @FXML
    private void handleBtnReset(ActionEvent event) {
        txtJudul.clear();
        cbxKategori1.getSelectionModel().clearSelection();
        dpcDari.setValue(null);
        dpcSampai.setValue(null);
        cbxSorting.getSelectionModel().selectFirst();
        BukuDAO.filterJudul = BukuDAO.filterKategori = BukuDAO.filterTanggalDari = BukuDAO.filterTanggalSampai = null;
        BukuDAO.sortingOption = "";
        loadDataBuku();
    }

    @FXML
    private void handleAdd() {
        try {
            if (!tfTahun.getText().matches("\\d{4}")) {
                showAlert("Validasi", "Tahun harus 4 digit", Alert.AlertType.ERROR);
                return;
            }
            if (!tfEdisi.getText().matches("\\d+")) {
                showAlert("Validasi", "Edisi harus angka", Alert.AlertType.ERROR);
                return;
            }
            Buku buku = new Buku(tfKodeBuku.getText(), getSelectedKodeKategori2(), tfJudul.getText(), tfPengarang.getText(),
                    tfPenerbit.getText(), Integer.parseInt(tfTahun.getText()), Integer.parseInt(tfEdisi.getText()),
                    dpcPilih.getValue().toString(), "");
            BukuDAO.addBuku(buku);
            clearForm();
            loadDataBuku();
            showAlert("Informasi", "Buku berhasil ditambahkan", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Kesalahan", "Data tidak lengkap atau salah input", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText("Perbarui data?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Buku buku = new Buku(tfKodeBuku.getText(), getSelectedKodeKategori2(), tfJudul.getText(), tfPengarang.getText(),
                            tfPenerbit.getText(), Integer.parseInt(tfTahun.getText()), Integer.parseInt(tfEdisi.getText()),
                            dpcPilih.getValue().toString(), "");
                    BukuDAO.updateBuku(buku);
                    clearForm();
                    loadDataBuku();
                    showAlert("Informasi", "Buku diperbarui", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Kesalahan", "Input tidak valid atau kosong", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleDelete() {
        String kode = tfKodeBuku.getText();
        if (!kode.isEmpty()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi");
            confirm.setHeaderText("Hapus buku ini?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BukuDAO.deleteBuku(kode);
                    clearForm();
                    loadDataBuku();
                    showAlert("Informasi", "Buku dihapus", Alert.AlertType.INFORMATION);
                }
            });
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Logout");
        confirm.setHeaderText("Yakin ingin logout?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Clear session
                uasperpustakaan.models.Session.getInstance().clearSession();

                // Kembali ke halaman login
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/uasperpustakaan/views/Main.fxml")); // sesuaikan path jika berbeda
                    Parent root = loader.load();
                    Stage stage = (Stage) tfKodeBuku.getScene().getWindow(); // ambil window aktif
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Kesalahan", "Gagal kembali ke halaman login", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleExportCSV(ActionEvent event) {
        LocalDate dari = dpcDari.getValue();
        LocalDate sampai = dpcSampai.getValue();

        if (dari == null || sampai == null) {
            showAlert("Validasi", "Silakan pilih tanggal pengadaan 'Dari' dan 'Sampai'", Alert.AlertType.WARNING);
            return;
        }

        List<Buku> bukuFiltered = BukuDAO.getBukuByTanggalRange(dari.toString(), sampai.toString());

        if (bukuFiltered.isEmpty()) {
            showAlert("Informasi", "Tidak ada data buku pada rentang tanggal tersebut", Alert.AlertType.INFORMATION);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan File CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("laporan_buku.csv");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Kode Buku,Kategori,Judul,Pengarang,Penerbit,Tahun,Edisi,Tanggal Pengadaan");

                for (Buku b : bukuFiltered) {
                    writer.printf("%s,%s,%s,%s,%s,%d,%d,%s\n",
                            b.getKodeBuku(),
                            b.getNamaKategori(),
                            b.getJudul(),
                            b.getPengarang(),
                            b.getPenerbit(),
                            b.getTahun(),
                            b.getEdisi(),
                            b.getTanggalPengadaan()
                    );
                }

                showAlert("Sukses", "Data berhasil diexport ke CSV!", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Gagal menulis file CSV", Alert.AlertType.ERROR);
            }
        }
    }

    private void selectBuku(Buku selected) {
        if (selected != null) {
            tfKodeBuku.setText(selected.getKodeBuku());
            tfJudul.setText(selected.getJudul());
            tfPengarang.setText(selected.getPengarang());
            tfPenerbit.setText(selected.getPenerbit());
            tfTahun.setText(String.valueOf(selected.getTahun()));
            tfEdisi.setText(String.valueOf(selected.getEdisi()));
            dpcPilih.setValue(LocalDate.parse(selected.getTanggalPengadaan()));
            cbxKategori2.getItems().stream()
                    .filter(k -> k.getKodeKategori().equals(selected.getKodeKategori()))
                    .findFirst()
                    .ifPresent(k -> cbxKategori2.setValue(k));
        }
    }

    private String getSelectedKodeKategori2() {
        KategoriBuku selected = cbxKategori2.getSelectionModel().getSelectedItem();
        return (selected != null) ? selected.getKodeKategori() : null;
    }

    @FXML
    private void handleResetForm(ActionEvent event) {
        clearForm();
        tfKodeBuku.setText(uasperpustakaan.dao.BukuDAO.generateKodeBukuOtomatis()); // generate ulang kode buku
    }

    private void clearForm() {
        tfKodeBuku.clear(); // hanya clear, tidak generate ulang
        tfJudul.clear();
        tfPengarang.clear();
        tfPenerbit.clear();
        tfTahun.clear();
        tfEdisi.clear();
        dpcPilih.setValue(null);
        cbxKategori2.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
