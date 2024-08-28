/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ilham_Oktavian
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Kelas untuk merepresentasikan tiket
class Ticket {
    private int id;
    private String eventName;
    private String eventDate;
    private int price;
    private int availableSeats;

    public Ticket(int id, String eventName, String eventDate, int price, int availableSeats) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeat(int quantity) {
        if (availableSeats >= quantity) {
            availableSeats -= quantity;
        } else {
            System.out.println("Maaf, jumlah kursi tidak mencukupi.");
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Acara: " + eventName + " | Tanggal: " + eventDate + " | Harga: Rp" + price + " | Kursi Tersedia: " + availableSeats;
    }
}

// Kelas utama
public class TicketBookingSystem {
    private static List<Ticket> tickets = new ArrayList<>();
    private static List<Ticket> bookedTickets = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeTickets();
        int choice = -1;

        System.out.println("=== Selamat Datang di Sistem Pemesanan Tiket ===");

        while (choice != 4) {
            showMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayAvailableTickets();
                        break;
                    case 2:
                        bookTicket();
                        break;
                    case 3:
                        displayBookedTickets();
                        break;
                    case 4:
                        System.out.println("Terima kasih telah menggunakan sistem kami. Sampai jumpa!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka.");
            }
        }
    }

    // Inisialisasi daftar tiket yang tersedia
    private static void initializeTickets() {
        tickets.add(new Ticket(1, "Konser Musik", "2024-09-10", 150000, 50));
        tickets.add(new Ticket(2, "Pertunjukan Teater", "2024-09-15", 100000, 30));
        tickets.add(new Ticket(3, "Festival Film", "2024-10-05", 200000, 20));
    }

    // Menampilkan menu utama
    private static void showMenu() {
        System.out.println("\nPilih opsi:");
        System.out.println("1. Lihat tiket yang tersedia");
        System.out.println("2. Pesan tiket");
        System.out.println("3. Lihat tiket yang telah dipesan");
        System.out.println("4. Keluar");
        System.out.print("Masukkan pilihan Anda: ");
    }

    // Menampilkan tiket yang tersedia
    private static void displayAvailableTickets() {
        System.out.println("\n=== Daftar Tiket Tersedia ===");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    // Proses pemesanan tiket
    private static void bookTicket() {
        System.out.print("\nMasukkan ID tiket yang ingin dipesan: ");
        try {
            int ticketId = Integer.parseInt(scanner.nextLine());
            Ticket selectedTicket = findTicketById(ticketId);

            if (selectedTicket != null) {
                System.out.print("Masukkan jumlah tiket yang ingin dipesan: ");
                int quantity = Integer.parseInt(scanner.nextLine());

                if (quantity <= 0) {
                    System.out.println("Jumlah tiket harus lebih dari 0.");
                    return;
                }

                if (selectedTicket.getAvailableSeats() >= quantity) {
                    selectedTicket.bookSeat(quantity);
                    bookedTickets.add(selectedTicket);
                    System.out.println("Tiket berhasil dipesan!");
                } else {
                    System.out.println("Maaf, kursi yang tersedia tidak mencukupi.");
                }
            } else {
                System.out.println("Tiket dengan ID tersebut tidak ditemukan.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Silakan masukkan angka.");
        }
    }

    // Menampilkan tiket yang telah dipesan
    private static void displayBookedTickets() {
        if (bookedTickets.isEmpty()) {
            System.out.println("\nAnda belum memesan tiket apapun.");
        } else {
            System.out.println("\n=== Daftar Tiket yang Telah Dipesan ===");
            for (Ticket ticket : bookedTickets) {
                System.out.println(ticket);
            }
        }
    }

    // Mencari tiket berdasarkan ID
    private static Ticket findTicketById(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }
}
