
package uasperpustakaan.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import uasperpustakaan.models.KategoriBuku;

public class KategoriBukuDAO {
    public static List<KategoriBuku> getAllKategoriBuku() {
        List<KategoriBuku> kategoriBukuList = new ArrayList<>();
        String sql = "SELECT * FROM kategori_buku";

        try (
            Connection koneksi = DBConnection.getConnection();
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                kategoriBukuList.add(new KategoriBuku(
                    rs.getString("kode_kategori"),
                    rs.getString("nama_kategori")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kategoriBukuList;
    }
}
