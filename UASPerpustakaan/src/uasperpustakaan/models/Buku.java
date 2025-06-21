
package uasperpustakaan.models;


public class Buku {
    private String kodeBuku;
    private String kodeKategori;
    private String judul;
    private String pengarang;
    private String penerbit;
    private int tahun;
    private int edisi;
    private String tanggalPengadaan;
    private String namaKategori;
    
    public Buku(String kodeBuku, String kodeKategori, String judul, String pengarang, String penerbit, int tahun, int edisi, String tanggalPengadaan, String namaKategori) {
        this.kodeBuku = kodeBuku;
        this.kodeKategori = kodeKategori;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahun = tahun;
        this.edisi = edisi;
        this.tanggalPengadaan = tanggalPengadaan;
        this.namaKategori = namaKategori;
    }

    // Getter
    public String getKodeBuku() {
        return kodeBuku;
    }
    
    public String getKodeKategori(){
        return kodeKategori;
    }
    

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public int getTahun() {
        return tahun;
    }

    public int getEdisi() {
        return edisi;
    }
    
    public String getTanggalPengadaan() {
        return tanggalPengadaan;
    }
    
    public String getNamaKategori(){
        return namaKategori;
    }

    // Setter
    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }
    
    public void setKodeKategori(String kodeKategori) {
        this.kodeKategori = kodeKategori;
    }
    

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public void setEdisi(int edisi) {
        this.edisi = edisi;
    }
    
    public void setTanggalPengadaan(String tanggalPengadaan) {
        this.tanggalPengadaan = tanggalPengadaan;
    }
    
    public void setNamaKategori(String namaKategori ) {
        this.namaKategori = namaKategori;
    }
}


