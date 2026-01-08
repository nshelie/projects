/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.TenantStayInfo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User-104
 */
public class TenantTrackingDao {




    // Load tenant stay info: tenant, apartment, next due date, status
    public List<TenantStayInfo> getTenantStayInfo() {
        List<TenantStayInfo> list = new ArrayList<>();
        String sql = "SELECT t.full_name AS tenant_name, a.apartment_number, " +
                     "       p.next_due_date " +
                     "FROM tenant t " +
                     "LEFT JOIN apartment a ON t.apartment_id = a.id " +
                     "LEFT JOIN ( " +
                     "    SELECT tenant_id, MAX(next_due_date) AS next_due_date " +
                     "    FROM payment " +
                     "    GROUP BY tenant_id " +
                     ") p ON t.id = p.tenant_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tenantName = rs.getString("tenant_name");
                String apartmentNumber = rs.getString("apartment_number");
                Date nextDue = rs.getDate("next_due_date");
                LocalDate nextDueDate = nextDue != null ? nextDue.toLocalDate() : null;

                String status;
                if (nextDueDate == null) {
                    status = "NO PAYMENT";
                } else if (nextDueDate.isBefore(LocalDate.now())) {
                    status = "OVERDUE";
                } else {
                    status = "ACTIVE";
                }

                list.add(new TenantStayInfo(tenantName, apartmentNumber, nextDueDate, status));
            }

        } catch (SQLException ex) {
            System.out.println("Tenant Tracking Load Error: " + ex.getMessage());
        }

        return list;
    }
}
