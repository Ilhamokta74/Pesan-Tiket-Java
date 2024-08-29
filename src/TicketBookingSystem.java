import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TicketBookingSystem {
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        connection = Koneksi.getKoneksi();
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

    private static void showMenu() {
        System.out.println("\nPilih opsi:");
        System.out.println("1. Lihat tiket yang tersedia");
        System.out.println("2. Pesan tiket");
        System.out.println("3. Lihat tiket yang telah dipesan");
        System.out.println("4. Keluar");
        System.out.print("Masukkan pilihan Anda: ");
    }

    private static void displayAvailableTickets() {
        String query = "SELECT * FROM tickets";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n=== Daftar Tiket Tersedia ===");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        " | Acara: " + resultSet.getString("event_name") +
                        " | Tanggal: " + resultSet.getDate("event_date") +
                        " | Harga: Rp" + resultSet.getDouble("price") +
                        " | Kursi Tersedia: " + resultSet.getInt("available_seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void bookTicket() {
        System.out.print("\nMasukkan ID tiket yang ingin dipesan: ");
        try {
            int ticketId = Integer.parseInt(scanner.nextLine());

            String selectQuery = "SELECT * FROM tickets WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, ticketId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int availableSeats = resultSet.getInt("available_seats");

                    System.out.print("Masukkan jumlah tiket yang ingin dipesan: ");
                    int quantity = Integer.parseInt(scanner.nextLine());

                    if (quantity <= 0) {
                        System.out.println("Jumlah tiket harus lebih dari 0.");
                        return;
                    }

                    if (availableSeats >= quantity) {
                        String updateQuery = "UPDATE tickets SET available_seats = ? WHERE id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, availableSeats - quantity);
                            updateStatement.setInt(2, ticketId);
                            updateStatement.executeUpdate();
                        }

                        String insertBooking = "INSERT INTO bookings (ticket_id, quantity) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertBooking)) {
                            insertStatement.setInt(1, ticketId);
                            insertStatement.setInt(2, quantity);
                            insertStatement.executeUpdate();
                        }

                        System.out.println("Tiket berhasil dipesan!");
                    } else {
                        System.out.println("Maaf, kursi yang tersedia tidak mencukupi.");
                    }
                } else {
                    System.out.println("Tiket dengan ID tersebut tidak ditemukan.");
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private static void displayBookedTickets() {
        String query = "SELECT b.id, t.event_name, b.quantity, b.booking_date " +
                       "FROM bookings b JOIN tickets t ON b.ticket_id = t.id";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.isBeforeFirst()) {
                System.out.println("\nAnda belum memesan tiket apapun.");
            } else {
                System.out.println("\n=== Daftar Tiket yang Telah Dipesan ===");
                while (resultSet.next()) {
                    System.out.println("ID Pemesanan: " + resultSet.getInt("id") +
                            " | Acara: " + resultSet.getString("event_name") +
                            " | Jumlah: " + resultSet.getInt("quantity") +
                            " | Tanggal Pemesanan: " + resultSet.getTimestamp("booking_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
