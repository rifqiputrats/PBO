/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Moch.Rifqi Putra TS
 */

 import classes.DaftarMenu;
 import classes.Kuah;
 import classes.Menu;
 import classes.Minuman;
 import classes.Pesanan;
 import classes.Ramen;
 import classes.Toping;
 import classes.Transaksi;
 import java.util.InputMismatchException;
 import java.util.Scanner;

public class MainAplikasiKasir {

    public DaftarMenu daftarMenu;
    
    public static double PAJAK_PPN = 0.10;
    public static double BIAYA_SERVICE = 0.05;
    
    public static void main(String[] args){
        //Inisialisasi kelas Scanner untuk mengam bil
        //input dari keyboard
        Scanner input = new Scanner(System.in);

        String no_transaksi, nama_pemesan, tanggal, no_meja = "";


        String transaksi_lagi = "", pesan_lagi = "", keterangan = "", makan_ditempat;
        int jumlah_pesanan, no_menu;

        MainAplikasiKasir app = new MainAplikasiKasir();
        //Tampilkan daftar menu
        app.generateDaftarMenu();
        

        //Mulai Transaksi
        System.out.println("======== TRANSAKSI ========");
        do{
        //Ambil data transaksi
        System.out.print("No Transaksi : ");
        no_transaksi = input.next();
        System.out.print("Pemesan : ");
        nama_pemesan = input.next();
        System.out.print("Tanggal : [dd-mm-yyyy] ");
        tanggal = input.next();
        System.out.print("Makan ditempat? [Y/N] ");
        makan_ditempat = input.next();

        if (makan_ditempat.equalsIgnoreCase("Y")){
            System.out.print("Nomor Meja : ");
            no_meja = input.next();
        }
        
        Transaksi trans = new Transaksi (no_transaksi, nama_pemesan, tanggal, no_meja);
        System.out.println("========= PESANAN ========");
        int no_kuah;
        do {
            //Ambil Menu berdasarkan nomot urut yang dipilih
            Menu menu_yang_dipilih = app.daftarMenu.pilihMenu();
            
            jumlah_pesanan = (int) app.CekInputNumber("Jumlah : ");
            
            //Buat Pesanan
            Pesanan pesanan = new Pesanan(menu_yang_dipilih, jumlah_pesanan);
            trans.tambahPesanan(pesanan);
            
            //Khusus Untuk Mrnu Ramen, Pesanan Kuahnya Langsung Diinput Juga
            if(menu_yang_dipilih.getKategori().equals("Ramen")){
                //Looping Sesuai Jumlah Pesanan
                int jumlah_ramen = jumlah_pesanan;
                do {
                    //Ambil Objek Menu Berdasarkan Nomor Yang Dipilih
                    Menu kuah_yang_dipilih = app.daftarMenu.pilihMenu();
                    
                    System.out.print("Level [0-10] : ");
                    String level = input.next();
                    
                    //Validasi Jumlah Kuah tidak boleh lebih besar dari jumlah_ramen
                    int jumlah_kuah = 0;
                 do {
                     jumlah_kuah = (int) app.CekInputNumber("Jumlah : ");
                     
                     if (jumlah_kuah > jumlah_ramen){
                         System.out.println("[Err] Jumlah kuah melebihi jumlah ramen");
                     }else{
                         break;
                     }
                 }while (jumlah_kuah > jumlah_ramen);
                         
                         //Set Pesanan Kuah
                         Pesanan pesanan_kuah = new Pesanan(kuah_yang_dipilih, jumlah_kuah);
                         pesanan_kuah.setKeterangan("Level " + level);
                         
                         //Tambahkan pesanan kuah ke transaksi
                         trans.tambahPesanan(pesanan_kuah);
                         
                         //Hitung jumlah ramen yang belum dipesan kuahnya
                         jumlah_ramen -= jumlah_kuah;
                }while(jumlah_ramen > 0);
            }else{
                //Jika Keterangan tidak diisi tulis -
                System.out.print("Keterangan [- jika tidak ada] : ");
                keterangan = input.next();
            }
            
            if(!keterangan.equals("-")){
                pesanan.setKeterangan(keterangan);
            }
            
            //Konfirmasi Tambahan Menu
            System.out.print("Tambah Pesanan Lagi? [Y/N] : ");
            pesan_lagi = input.next();
                    
        } while (pesan_lagi.equalsIgnoreCase("Y"));
        
        //Cetak Struk
        trans.cetakStruk();
        
        //Penghitungan Total Harga
        double total_pesanan = trans.hitungTotalPesanan();
        System.out.println("====================================");
        System.out.println("Total : \t\t " + total_pesanan);
        
        //Plus Hitung Biaya Pajak
        trans.setPajak(PAJAK_PPN);
        double ppn = trans.hitungPajak();
        System.out.println("Pajak 10% : \t\t " + ppn);
        
        //Hitung Biaya Service Apabila Makan di Tempat
        double biaya_service = 0;
        if (makan_ditempat.equalsIgnoreCase("Y")){
            trans.setBiayaServive(BIAYA_SERVICE);
            biaya_service = trans.hitungBiayaService();
            System.out.println("Biaya Service  5% : \t\t " + biaya_service);
        }
        
        //Tampilkan Total Bayar Keseluruhan
        System.out.println("Total Pembayaran : \t " + trans.hitungTotalBayar(ppn, biaya_service));
        
        double kembalian = 0;
        do {
         //Input Uang Bayar
         double uang_bayar = app.CekInputNumber("Uang Bayar : \t\t");
         
         kembalian = trans.hitungKembalian(uang_bayar);
         if (kembalian < 0){
            System.out.println("[Err] Uang anda kurang");
        }else{
             System.out.println("Kembalian : \t\t " + kembalian);
             break;
         }
        }while(kembalian < 0);
        
        System.out.println("Lakukan Transaksi Lagi?[Y/N]");
        transaksi_lagi = input.next();
        }while(transaksi_lagi.equalsIgnoreCase("Y"));
        System.out.println("======== TERIMA KASIH ========");
    }
   

    public void generateDaftarMenu() { 
        daftarMenu = new DaftarMenu();
        daftarMenu.tambahMenu (new Ramen ("Ramen Seafood", 25000));
        daftarMenu.tambahMenu (new Ramen("Ramen Original", 18000));
        daftarMenu.tambahMenu (new Ramen("Ramen Vegetarian", 22000));
        daftarMenu.tambahMenu (new Ramen("Ramen Karnivor", 28000));
        daftarMenu.tambahMenu (new Kuah("Kuah Orisinil"));
        daftarMenu.tambahMenu (new Kuah("Kuah Internasional"));
        daftarMenu.tambahMenu (new Kuah("Kuah Spicy Lada"));
        daftarMenu.tambahMenu (new Kuah("Kuah Soto Padang"));
        daftarMenu.tambahMenu (new Toping("Crab Stick Bakar", 6000));
        daftarMenu.tambahMenu (new Toping("Chicken Katsu", 8000));
        daftarMenu.tambahMenu (new Toping("Gyoza Goreng", 4000));
        daftarMenu.tambahMenu (new Toping("Bakso Goren", 7000));
        daftarMenu.tambahMenu (new Toping("Enoki Goreng", 5000));
        daftarMenu.tambahMenu (new Minuman("Es Teh Anget Manis", 5000));
        daftarMenu.tambahMenu (new Minuman("Jus Alpukat", 10000));
        daftarMenu.tambahMenu (new Minuman("Jus Mangga", 9000));
        daftarMenu.tambahMenu (new Minuman("Cappucino Coffe", 15000));
        daftarMenu.tambahMenu (new Minuman("Vietnam Dripp", 14000));

        daftarMenu.tampilDaftarMenu();
    }
    
    public double CekInputNumber(String label){
        try{
         Scanner get_input = new Scanner(System.in);
         System.out.print(label);
         double nilai = get_input.nextDouble();
         
         return nilai;
        }catch(InputMismatchException ex){
            System.out.print("[Err] Harap Masukkan Angka");
            return CekInputNumber(label);
        }
    }
}
