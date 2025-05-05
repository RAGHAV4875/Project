import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }
    
    // Add new book
    public void addBook(int id, String title, String author, int quantity) throws SQLException {
        String sql = "INSERT INTO books (id, title, author, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
        }
    }
    
    // Get all books
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                books.add(new Book(id, title, author, quantity));
            }
        }
        return books;
    }
    
    // Issue a book
    public void issueBook(int bookId, String userName) throws SQLException {
        String insertSql = "INSERT INTO issued_books (id, user_name, issue_date) VALUES (?, ?, CURRENT_DATE)";
        String updateSql = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
    
        try (
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            PreparedStatement updateStmt = conn.prepareStatement(updateSql)
        ) {
            conn.setAutoCommit(false); // Begin transaction
    
            // Insert into issued_books
            insertStmt.setInt(1, bookId);
            insertStmt.setString(2, userName);
            insertStmt.executeUpdate();
    
            // Update book quantity
            updateStmt.setInt(1, bookId);
            int rowsAffected = updateStmt.executeUpdate();
    
            if (rowsAffected == 0) {
                conn.rollback();
                throw new SQLException("Book issue failed: Book not available.");
            }
    
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true); // Restore auto-commit
        }
    }
    
    
    // Return a book
    // Return a book
public void returnBook(int bookId, String userName) throws SQLException {
    String updateSql = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
    String deleteSql = "DELETE FROM issued_books WHERE id = ? AND user_name = ?";

    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql);
         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

        // First, update the quantity
        updateStmt.setInt(1, bookId);
        updateStmt.executeUpdate();

        // Then, delete the issued record
        deleteStmt.setInt(1, bookId);
        deleteStmt.setString(2, userName);
        deleteStmt.executeUpdate();
    }
}

    
    // Get issued book details using JOIN
    public List<String[]> getIssuedBookDetails() throws SQLException {
        List<String[]> issuedList = new ArrayList<>();
        String sql = """
            SELECT ib.issue_id, b.title, ib.user_name, ib.issue_date
            FROM issued_books ib
            JOIN books b ON ib.id = b.id
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt("issue_id"));
                row[1] = rs.getString("title");
                row[2] = rs.getString("user_name");
                row[3] = rs.getString("issue_date");
                issuedList.add(row);
            }
        }
        return issuedList;
    }
    
}
