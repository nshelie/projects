/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Expense;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author User-104
 */
public class ExpenseDao {

    public boolean insertExpense(Expense expense) {
        String sql = "INSERT INTO expense(apartment_id, category, amount, description, date) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, expense.getApartmentId());
            ps.setString(2, expense.getCategory());
            ps.setDouble(3, expense.getAmount());
            ps.setString(4, expense.getDescription());
            ps.setDate(5, Date.valueOf(expense.getDate()));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Insert Expense Error: " + ex.getMessage());
            return false;
        }
    }

    public List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expense ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Expense e = new Expense();
                e.setExpenseId(rs.getInt("expense_id"));
                e.setApartmentId(rs.getInt("apartment_id"));
                e.setCategory(rs.getString("category"));
                e.setAmount(rs.getDouble("amount"));
                e.setDescription(rs.getString("description"));
                e.setDate(rs.getDate("date").toLocalDate());

                list.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Load Expenses Error: " + ex.getMessage());
        }
        return list;
    }
    
    public boolean updateExpense(Expense expense) {
    String sql = "UPDATE expense SET apartment_id=?, category=?, amount=?, description=?, date=? WHERE expense_id=?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, expense.getApartmentId());
        ps.setString(2, expense.getCategory());
        ps.setDouble(3, expense.getAmount());
        ps.setString(4, expense.getDescription());
        ps.setDate(5, Date.valueOf(expense.getDate()));
        ps.setInt(6, expense.getExpenseId());

        return ps.executeUpdate() > 0;

    } catch (SQLException ex) {
        System.out.println("Update Expense Error: " + ex.getMessage());
        return false;
    }
}

public boolean deleteExpense(int expenseId) {
    String sql = "DELETE FROM expense WHERE expense_id=?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, expenseId);
        return ps.executeUpdate() > 0;

    } catch (SQLException ex) {
        System.out.println("Delete Expense Error: " + ex.getMessage());
        return false;
    }
}

public List<Expense> searchExpenses(String keyword) {
    List<Expense> list = new ArrayList<>();
    String sql = "SELECT * FROM expense e "
               + "JOIN apartment a ON e.apartment_id = a.id "
               + "WHERE e.description LIKE ? OR e.category LIKE ? OR a.apartment_number LIKE ? "
               + "ORDER BY e.date DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String searchPattern = "%" + keyword + "%";
        ps.setString(1, searchPattern);
        ps.setString(2, searchPattern);
        ps.setString(3, searchPattern);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Expense e = new Expense();
                e.setExpenseId(rs.getInt("expense_id"));
                e.setApartmentId(rs.getInt("apartment_id"));
                e.setCategory(rs.getString("category"));
                e.setAmount(rs.getDouble("amount"));
                e.setDescription(rs.getString("description"));
                e.setDate(rs.getDate("date").toLocalDate());
                list.add(e);
            }
        }

    } catch (SQLException ex) {
        System.out.println("Search Expenses Error: " + ex.getMessage());
    }
    return list;
}


}
