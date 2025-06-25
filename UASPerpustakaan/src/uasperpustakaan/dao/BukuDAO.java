package uasperpustakaan.dao;

import uasperpustakaan.models.Buku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    public static String filterTanggalDari;
    public static String filterTanggalSampai;
    public static String filterJudul;
    public static String filterKategori;
    public static String sortingOption = "";

    public static List<Buku> getAllBuku() {
        List<Buku> bukuList = new ArrayList<>();
        String sql = "SELECT buku.*, kategori_buku.nama_kategori FROM buku " +
                     "LEFT JOIN kategori_buku ON buku.kode_kategori = kategori_buku.kode_kategori WHERE 1=1";

        if (filterTanggalDari != null && !filterTanggalDari.isEmpty() &&
            filterTanggalSampai != null && !filterTanggalSampai.isEmpty()) {
            sql += " AND buku.tanggal_pengadaan BETWEEN '" + filterTanggalDari + "' AND '" + filterTanggalSampai + "'";
        }

        if (filterJudul != null && !filterJudul.isEmpty()) {
            sql += " AND (buku.judul LIKE '%" + filterJudul + "%' OR buku.pengarang LIKE '%" + filterJudul + "%') ";
        }

        if (filterKategori != null && !filterKategori.isEmpty()) {
            sql += " AND buku.kode_kategori = '" + filterKategori + "'";
        }

        if (sortingOption != null && !sortingOption.isEmpty()) {
            switch (sortingOption) {
                case "Judul A-Z":
                    sql += " ORDER BY buku.judul ASC";
                    break;
                case "Judul Z-A":
                    sql += " ORDER BY buku.judul DESC";
                    break;
                case "Pengadaan Terbaru":
                    sql += " ORDER BY buku.tanggal_pengadaan DESC";
                    break;
                case "Pengadaan Lama":
                    sql += " ORDER BY buku.tanggal_pengadaan ASC";
                    break;
            }
        }

        try (
            Connection koneksi = DBConnection.getConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getString("kode_buku"),
                    rs.getString("kode_kategori"),
                    rs.getString("judul"),
                    rs.getString("pengarang"),
                    rs.getString("penerbit"),
                    rs.getInt("tahun_terbit"),
                    rs.getInt("edisi"),
                    rs.getString("tanggal_pengadaan"),
                    rs.getString("nama_kategori")
                );
                bukuList.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bukuList;
    }

    public static void addBuku(Buku buku) {
        String query = "INSERT INTO buku (kode_buku, kode_kategori, judul, pengarang, penerbit, tahun_terbit, edisi, tanggal_pengadaan) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection koneksi = DBConnection.getConnection();
            PreparedStatement ps = koneksi.prepareStatement(query)
        ) {
            ps.setString(1, buku.getKodeBuku());
            ps.setString(2, buku.getKodeKategori());
            ps.setString(3, buku.getJudul());
            ps.setString(4, buku.getPengarang());
            ps.setString(5, buku.getPenerbit());
            ps.setInt(6, buku.getTahun());
            ps.setInt(7, buku.getEdisi());
            ps.setString(8, buku.getTanggalPengadaan());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBuku(Buku buku) {
        String query = "UPDATE buku SET kode_kategori=?, judul=?, pengarang=?, penerbit=?, tahun_terbit=?, edisi=?, tanggal_pengadaan=? " +
                       "WHERE kode_buku=?";
        try (
            Connection koneksi = DBConnection.getConnection();
            PreparedStatement ps = koneksi.prepareStatement(query)
        ) {
            ps.setString(1, buku.getKodeKategori());
            ps.setString(2, buku.getJudul());
            ps.setString(3, buku.getPengarang());
            ps.setString(4, buku.getPenerbit());
            ps.setInt(5, buku.getTahun());
            ps.setInt(6, buku.getEdisi());
            ps.setString(7, buku.getTanggalPengadaan());
            ps.setString(8, buku.getKodeBuku());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBuku(String kodeBuku) {
        String query = "DELETE FROM buku WHERE kode_buku = ?";
        try (
            Connection koneksi = DBConnection.getConnection();
            PreparedStatement ps = koneksi.prepareStatement(query)
        ) {
            ps.setString(1, kodeBuku);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String generateKodeBukuOtomatis() {
    String kodeBaru = "B00001";
    try (
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(kode_buku) AS max_kode FROM buku")
    ) {
        if (rs.next() && rs.getString("max_kode") != null) {
            String maxKode = rs.getString("max_kode");
            int nomor = Integer.parseInt(maxKode.substring(1)); // Hilangkan 'B'
            nomor++; // Tambah 1
            kodeBaru = String.format("B%05d", nomor);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return kodeBaru;
}
    
    public static List<Buku> getBukuByTanggalRange(String dari, String sampai) {
    List<Buku> list = new ArrayList<>();

    String sql = "SELECT b.*, k.nama_kategori FROM buku b "
               + "JOIN kategori_buku k ON b.kode_kategori = k.kode_kategori "
               + "WHERE tanggal_pengadaan BETWEEN ? AND ? "
               + "ORDER BY tanggal_pengadaan ASC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, dari);
        stmt.setString(2, sampai);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Buku buku = new Buku(
                rs.getString("kode_buku"),
                rs.getString("kode_kategori"),
                rs.getString("judul"),
                rs.getString("pengarang"),
                rs.getString("penerbit"),
                rs.getInt("tahun_terbit"),
                rs.getInt("edisi"),
                rs.getString("tanggal_pengadaan"),
                rs.getString("nama_kategori")
            );
            list.add(buku);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


}
