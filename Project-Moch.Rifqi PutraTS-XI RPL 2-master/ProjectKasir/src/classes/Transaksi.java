/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Moch.Rifqi Putra TS
 */

import java.util.ArrayList;

public class Transaksi {
    private String noTransaksi;
    private String namaPemesan;
    private String tanggal;
    private String noMeja;
    private ArrayList<Pesanan> pesanan;
    private double pajak;
    private double uangBayar;
    private double totalBayar;
    private double biayaService=0;

    public Transaksi (String no_transaksi, String nm_pemesan, String tanggal, String no_meja) { 
        this.noTransaksi = no_transaksi;
        this.namaPemesan = nm_pemesan;
        this.tanggal =  tanggal;
        this.noMeja = no_meja;

        pesanan = new ArrayList<>();
    }

    public void tambahPesanan(Pesanan pesanan) { 
        this.pesanan.add(pesanan);
    }

    public ArrayList<Pesanan> getSemuaPesanan() {
        return pesanan; 
    }

    public double hitungTotalPesanan(){
        for (int i = 0; i < pesanan.size(); i++){
        Pesanan psn = pesanan.get(i);
        double harga = psn.getMenu().getHarga();
        totalBayar += (harga * psn.getJumlah());
        }
        
        return totalBayar; 
    }
    
    public double hitungPajak(){
        return totalBayar * pajak;
    }
    
    public double hitungBiayaService(){
        return totalBayar * biayaService;
    }
    
    public double hitungTotalBayar(double pajak, double service){
        totalBayar = totalBayar + pajak + service;
        return totalBayar;
    }

    public double hitungKembalian(double uang_bayar) {
        return uang_bayar - totalBayar; 
    }

    public void cetakStruk() {
        System.out.println("\n======== RR | RUNGKADRAMEN ========");
        System.out.println("No Transaksi : " + noTransaksi);
        System.out.println("Pemesan : " + namaPemesan);
        System.out.println("Tanggal : " + tanggal);
        
        //cek Jika Nomor Meja Kosong, berarti Take Away
        if(noMeja.equals("")){
            noMeja = "Take Away";
        }
        
        System.out.println("Meja : " + noMeja);
        System.out.println("====================================");
        for (int i = 0; i < pesanan.size(); i++){
            Pesanan psn = pesanan.get(i);
            Menu m = psn.getMenu();
            String pesanan = psn.getJumlah() + " " + m.getNama_menu() + "\t" + (m.getHarga() * psn.getJumlah());
            
            //Jika pesanan kuah, tambah spasi diawal 2
            if (m.getKategori().equals("Kuah")){
                pesanan = "  " + pesanan;
            }
            System.out.println(pesanan);
        }
    }
    
    public void setPajak(double pajak){
        this.pajak = pajak;
    }
    
    public void setBiayaServive(double service){
        this.biayaService = service;
    }
    
}
