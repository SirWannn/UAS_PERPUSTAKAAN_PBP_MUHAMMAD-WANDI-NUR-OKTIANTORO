
package uasperpustakaan.models;

public class KategoriBuku {
    private String kodeKategori;
    private String namaKategori;
    
    public KategoriBuku(String kodeKategori, String namaKategori) {
        this.kodeKategori = kodeKategori;
        this.namaKategori = namaKategori;
    }
    
    public String getKodeKategori() {
        return kodeKategori;
    }
    
    public void setKodeKategori(String namaKategori) {
        this.kodeKategori = kodeKategori;
    }
    
    public String getNamaKategori() {
        return namaKategori;
    }
    
    public void setNamaKatgeori(String namaKategeori) {
        this.namaKategori = namaKategori;
    }
    
    public String toString() {
        return namaKategori;
    }
}
