# Apartment Rent Management System - Project Evaluation

**Project Name:** Apartment Rent Management System  
**Course:** INSY 7312 - JAVA PROGRAMMING  
**Evaluation Date:** Based on Lecturer Requirements

---

## Executive Summary

This document provides a comprehensive evaluation of the Apartment Rent Management System against the requirements specified in the mid-semester project instructions. The evaluation covers all 8 requirements with detailed analysis of what is fulfilled and what needs improvement.

---

## Requirement 1: Database Creation [1 Point]

### ✅ **FULFILLED** - Score: 1/1

**Evidence:**
- Database name: `apartment_rent_management_system_db` (found in `DBConnection.java` line 16)
- Database Management System: MySQL (JDBC URL: `jdbc:mysql://localhost:3306/...`)
- Connection properly configured with username and password

**Location:** `src/dao/DBConnection.java`

**Status:** ✅ **COMPLETE** - The database is properly named and configured.

---

## Requirement 2: Minimum 4 Relations (Tables) [1 Point]

### ✅ **FULFILLED** - Score: 1/1

**Evidence of Tables Found:**

1. **`apartment`** table
   - Referenced in: `ApartmentDao.java`
   - Attributes: id, apartment_number, location, rent_amount, is_occupied, rent_type

2. **`tenant`** table
   - Referenced in: `TenantDao.java`
   - Attributes: id, full_name, gender, phone, national_id, move_in_date, apartment_id

3. **`payment`** table
   - Referenced in: `PaymentDao.java`
   - Attributes: payment_id, tenant_id, apartment_id, rent_type, rent_amount, amount_paid, payment_for_duration, date_paid, next_due_date

4. **`expense`** table
   - Referenced in: `ExpenseDao.java`
   - Attributes: expense_id, apartment_id, category, amount, description, date

**Additional Tables Found:**
- `pending_maintenance` table (PendingMaintenanceDAO.java)
- `user` table (UserDao.java)
- `notification` table (NotificationDao.java)
- `tenant_tracking` table (TenantTrackingDao.java)

**Note:** Lease table files exist but are not used in the GUI (user confirmed deletion).

**Status:** ✅ **EXCEEDS REQUIREMENT** - The project has 4 primary tables (apartment, tenant, payment, expense) plus additional supporting tables.

---

## Requirement 3: 4 Primary Subject Tables with at least 5 Attributes [1 Point]

### ✅ **FULFILLED** - Score: 1/1

**Analysis of Primary Tables:**

1. **`apartment`** table - **6 attributes** ✅
   - id (Primary Key)
   - apartment_number
   - location
   - rent_amount
   - is_occupied
   - rent_type

2. **`tenant`** table - **7 attributes** ✅
   - id (Primary Key)
   - full_name
   - gender
   - phone
   - national_id
   - move_in_date
   - apartment_id (Foreign Key)

3. **`payment`** table - **9 attributes** ✅
   - payment_id (Primary Key)
   - tenant_id (Foreign Key)
   - apartment_id (Foreign Key)
   - rent_type
   - rent_amount
   - amount_paid
   - payment_for_duration
   - date_paid
   - next_due_date

4. **`expense`** table - **6 attributes** ✅
   - expense_id (Primary Key)
   - apartment_id (Foreign Key)
   - category
   - amount
   - description
   - date

**Status:** ✅ **EXCEEDS REQUIREMENT** - All 4 primary tables have more than 5 attributes each.

---

## Requirement 4: Validation Rules [5 Points]

### ⚠️ **PARTIALLY FULFILLED** - Score: 2.5/5

**Requirement:** At least 5 business validation rules and 5 technical validation rules. At least one table must contain a minimum of two technical validations.

### **Technical Validation Rules Found:**

1. ✅ **Empty Field Validation (ApartmentPanel.java:382-384)**
   - Validates that apartment number, location, and rent amount are not empty
   - Location: `ApartmentPanel.java` line 382-384

2. ✅ **Empty Field Validation (LoginForm.java:176-178)**
   - Validates username and password are not empty
   - Location: `LoginForm.java` line 176-178

3. ✅ **Selection Validation (ExpensePanel.java:352-354)**
   - Validates apartment is selected before saving expense
   - Location: `ExpensePanel.java` line 352-354

4. ✅ **Selection Validation (ExpensePanel.java:360-362)**
   - Validates category is selected before saving expense
   - Location: `ExpensePanel.java` line 360-362

5. ✅ **Date Validation (ExpensePanel.java:372-374)**
   - Validates date is selected before saving expense
   - Location: `ExpensePanel.java` line 372-374

6. ✅ **Row Selection Validation (ApartmentPanel.java:406-408)**
   - Validates that a row is selected before update operation
   - Location: `ApartmentPanel.java` line 406-408

7. ✅ **Row Selection Validation (TenantPanel.java:435-437)**
   - Validates that a tenant is selected before update operation
   - Location: `TenantPanel.java` line 435-437

**Technical Validations Count: 7+** ✅

### **Business Validation Rules Found:**

1. ⚠️ **Apartment Occupancy Logic (TenantDao.java:33)**
   - When tenant is added, apartment status is automatically set to occupied
   - This is a business rule but implemented as automatic logic
   - Location: `TenantDao.java` line 33

2. ⚠️ **Payment Duration Calculation (Payment.java:139-141)**
   - Automatically calculates payment duration based on amount paid and rent amount
   - Business logic: `paymentForDuration = amountPaid / rentAmount`
   - Location: `Payment.java` line 139-141

3. ⚠️ **Next Due Date Calculation (Payment.java:146-162)**
   - Calculates next due date based on rent type (daily, weekly, monthly, yearly)
   - Business logic for different rent periods
   - Location: `Payment.java` line 146-162

4. ⚠️ **Apartment Availability Check (TenantPanel.java:80)**
   - Only shows available apartments when assigning tenants
   - Business rule: Cannot assign tenant to occupied apartment
   - Location: `TenantPanel.java` line 80

5. ⚠️ **Apartment Status Update (TenantDao.java:59-64)**
   - When tenant apartment is changed, old apartment is freed and new one is occupied
   - Business rule: Maintains apartment occupancy consistency
   - Location: `TenantDao.java` line 59-64

**Business Validations Count: 5** ✅

### **Issues Identified:**

1. ❌ **Missing Explicit Business Validations:**
   - No validation for: rent amount must be positive
   - No validation for: phone number format
   - No validation for: national ID format/length
   - No validation for: end date must be after start date (lease)
   - No validation for: amount paid must be positive
   - No validation for: apartment number uniqueness

2. ❌ **Missing Technical Validations:**
   - No validation for: numeric fields (rent amount, amount paid) must be valid numbers
   - No validation for: date format validation
   - No validation for: string length limits
   - No validation for: foreign key existence before insert

3. ⚠️ **Validation Messages:**
   - Some validations use `System.out.println()` instead of `JOptionPane` (ExpensePanel.java)
   - Should use JOptionPane for all validation messages as per requirement 7

**Status:** ⚠️ **PARTIALLY FULFILLED** - Has sufficient validations but many are implicit business logic rather than explicit validation rules with proper error messages. The requirement asks for explicit validation rules that show messages when criteria are not met.

**Recommendation:** Add explicit validation methods that check business rules and display JOptionPane messages when validation fails.

---

## Requirement 5: MVC, DAO, JDBC, and CRUD Operations [10 Points]

### ✅ **FULFILLED** - Score: 10/10

### **MVC Pattern Implementation:**

✅ **Model Layer** (`src/model/`):
- `Apartment.java` - Model class
- `Tenant.java` - Model class
- `Payment.java` - Model class
- `Expense.java` - Model class
- `PendingMaintenance.java` - Model class
- `User.java` - Model class

✅ **View Layer** (`src/view/`):
- `ApartmentPanel.java` - GUI for apartment management
- `TenantPanel.java` - GUI for tenant management
- `PaymentPanel.java` - GUI for payment management
- `DashBoard.java` - Main navigation dashboard
- `LoginForm.java` - Login interface
- Additional panels for expense, maintenance, etc.

✅ **Controller Logic:**
- Business logic embedded in View classes (action listeners)
- Could be improved with separate controller classes, but functional

### **DAO Pattern Implementation:**

✅ **DAO Classes** (`src/dao/`):
- `ApartmentDao.java` - Data Access Object for apartments
- `TenantDao.java` - Data Access Object for tenants
- `PaymentDao.java` - Data Access Object for payments
- `ExpenseDao.java` - Data Access Object for expenses
- `PendingMaintenanceDAO.java` - Data Access Object for maintenance
- `UserDao.java` - Data Access Object for users
- `DBConnection.java` - Database connection utility

**DAO Pattern Evidence:**
- All DAOs use PreparedStatement for SQL operations
- Proper connection management with try-with-resources
- Separation of data access logic from business logic

### **JDBC API Usage:**

✅ **JDBC Implementation:**
- Uses `java.sql.Connection`, `PreparedStatement`, `ResultSet`
- Proper use of `PreparedStatement` for parameterized queries (prevents SQL injection)
- Connection pooling through `DBConnection.getConnection()`
- Proper resource management with try-with-resources blocks

**Evidence:** All DAO classes extensively use JDBC API.

### **CRUD Operations:**

#### **CREATE Operations:**
✅ `ApartmentDao.addApartment()` - Insert new apartment
✅ `TenantDao.insertTenant()` - Insert new tenant
✅ `ExpenseDao.insertExpense()` - Insert new expense
✅ `PaymentDao.insertPayment()` - Insert new payment
✅ `PendingMaintenanceDAO.insertMaintenance()` - Insert maintenance record

#### **READ Operations:**
✅ `ApartmentDao.getAllApartments()` - Get all apartments
✅ `ApartmentDao.getApartmentById()` - Get apartment by ID
✅ `ApartmentDao.searchApartments()` - Search apartments
✅ `TenantDao.getAllTenants()` - Get all tenants
✅ `TenantDao.searchTenants()` - Search tenants
✅ `PaymentDao.getAllPayments()` - Get all payments
✅ `ExpenseDao.getAllExpenses()` - Get all expenses
✅ `PendingMaintenanceDAO.getAllMaintenance()` - Get all maintenance records

**READ Operations Displayed in JTable:**
✅ `ApartmentPanel.loadApartmentTable()` - Displays apartments in JTable
✅ `TenantPanel.loadTenantTable()` - Displays tenants in JTable
✅ `PaymentPanel.loadAllPayments()` - Displays payments in JTable
✅ Other panels also use JTable for data display

#### **UPDATE Operations:**
✅ `ApartmentDao.updateApartment()` - Update apartment
✅ `TenantDao.updateTenant()` - Update tenant
✅ `PendingMaintenanceDAO.updateMaintenance()` - Update maintenance

#### **DELETE Operations:**
✅ `ApartmentDao.deleteApartment()` - Delete apartment
✅ `TenantDao.deleteTenant()` - Delete tenant
✅ `PendingMaintenanceDAO.deleteMaintenance()` - Delete maintenance

**Status:** ✅ **FULLY FULFILLED** - All CRUD operations are implemented using MVC and DAO patterns with JDBC API. Read operations properly display data in JTable components.

---

## Requirement 6: Minimum 4 Swing GUI Pages with Communication [5 Points]

### ✅ **FULFILLED** - Score: 5/5

### **GUI Pages Count:**

1. ✅ **LoginForm** - Login page
2. ✅ **DashBoard** - Main dashboard with navigation
3. ✅ **HomePage** - Home page
4. ✅ **ApartmentPanel** - Apartment management page
5. ✅ **TenantPanel** - Tenant management page
6. ✅ **PaymentPanel** - Payment management page
7. ✅ **ExpensePanel** - Expense management page
8. ✅ **MaintenancePanel** - Maintenance management page
9. ✅ **TenantTrackingPanel** - Tenant tracking page
10. ✅ **ProfitLossPanel** - Profit/Loss overview page
11. ✅ **NotificationPanel** - Notification page

**Total: 11 GUI pages** ✅ (Exceeds minimum requirement of 4)

### **Page Communication:**

✅ **Navigation System:**
- `DashBoard.java` uses `CardLayout` to switch between panels
- All panels are added to the dashboard's content panel
- Navigation buttons in dashboard switch between pages
- Evidence: `DashBoard.java` lines 36-59, 248-314

✅ **Data Flow Between Pages:**
- Apartment data flows to TenantPanel (apartment combo box)
- Tenant data flows to PaymentPanel (tenant selection)
- Apartment data flows to ExpensePanel and MaintenancePanel
- All pages share the same database connection through DAOs

✅ **User Flow:**
- LoginForm → DashBoard → Various Panels
- Logout from DashBoard returns to LoginForm
- Evidence: `DashBoard.java` line 270 (logout action)

**Status:** ✅ **EXCEEDS REQUIREMENT** - The system has 11 GUI pages (well above the minimum of 4) and they communicate effectively through the dashboard navigation system and shared data access layer.

---

## Requirement 7: JOptionPane for Messages [2 Points]

### ⚠️ **PARTIALLY FULFILLED** - Score: 1.5/2

### **JOptionPane Usage Analysis:**

#### ✅ **Success Messages (Information Messages):**
- ✅ `ApartmentPanel.java:396` - "Apartment added successfully!"
- ✅ `ApartmentPanel.java:421` - "Apartment updated successfully!"
- ✅ `ApartmentPanel.java:445` - "Apartment deleted successfully!"
- ✅ `TenantPanel.java:423` - "Tenant added successfully!"
- ✅ `TenantPanel.java:462` - "Tenant updated successfully!"
- ✅ `TenantPanel.java:485` - "Tenant deleted!"
- ✅ `MaintenancePanel.java:407` - "Maintenance record added successfully!"
- ✅ `MaintenancePanel.java:434` - "Maintenance record updated!"
- ✅ `LoginForm.java:186` - "Welcome [name]!" (INFORMATION_MESSAGE)
- ✅ `MaintenancePanel.java:454` - "Record deleted!"

#### ✅ **Error Messages:**
- ✅ `ApartmentPanel.java:400` - "Failed to add apartment."
- ✅ `ApartmentPanel.java:426` - "Failed to update apartment."
- ✅ `ApartmentPanel.java:450` - "Failed to delete apartment."
- ✅ `TenantPanel.java:426` - "Failed to add tenant."
- ✅ `TenantPanel.java:465` - "Failed to update tenant."
- ✅ `TenantPanel.java:489` - "Failed to delete tenant."
- ✅ `LoginForm.java:177` - "Please enter username and password." (ERROR_MESSAGE)
- ✅ `LoginForm.java:194` - "Invalid username or password." (ERROR_MESSAGE)

#### ✅ **Validation Messages:**
- ✅ `ApartmentPanel.java:383` - "Please fill all fields!"
- ✅ `ApartmentPanel.java:407` - "Select an apartment from the table first!"
- ✅ `ApartmentPanel.java:433` - "Please select an apartment to delete!"
- ✅ `TenantPanel.java:436` - "Please select a tenant from the table!"
- ✅ `TenantPanel.java:476` - "Please select a tenant to delete."
- ✅ `MaintenancePanel.java:443` - "Select a record to update."
- ✅ `MaintenancePanel.java:460` - "Select a record to delete."

#### ❌ **Issues Found:**

1. **ExpensePanel Uses System.out.println() Instead of JOptionPane:**
   - Line 353: `System.out.println("Please select an apartment.");`
   - Line 361: `System.out.println("Please select a category.");`
   - Line 373: `System.out.println("Please select a date.");`
   - Line 390: `System.out.println("Expense saved successfully!");`
   - Line 396: `System.out.println("Save Expense Error: " + ex.getMessage());`
   
   **Should be:** JOptionPane.showMessageDialog() for user visibility

2. **PaymentPanel Uses System.out.println():**
   - Line 409: `System.out.println("Payment saved successfully!");`
   - Line 423: `System.out.println("Save Payment Error: " + ex.getMessage());`
   
   **Should be:** JOptionPane.showMessageDialog()

3. **Missing Validation Messages:**
   - Some validation checks don't show JOptionPane messages
   - Error handling in catch blocks sometimes only prints to console

**Status:** ⚠️ **PARTIALLY FULFILLED** - JOptionPane is extensively used for success, error, and validation messages in most panels. However, ExpensePanel and PaymentPanel use System.out.println() instead of JOptionPane, which violates the requirement. All messages should be visible to end users through JOptionPane dialogs.

**Recommendation:** Replace all System.out.println() calls with JOptionPane.showMessageDialog() in ExpensePanel and PaymentPanel.

---

## Requirement 8: Viva [5 Points]

### ⚠️ **CANNOT EVALUATE** - Score: N/A

**Status:** ⚠️ **CANNOT BE EVALUATED** - Viva is an oral examination that requires interaction with the student. This cannot be assessed through code review alone.

**Preparation Tips:**
- Be ready to explain the MVC and DAO patterns
- Understand how CRUD operations work in your code
- Be able to explain the database schema
- Know the validation rules you implemented
- Understand the flow between GUI pages
- Be prepared to demonstrate the application

---

## Summary of Scores

| Requirement | Points Available | Points Awarded | Status |
|------------|------------------|----------------|--------|
| 1. Database Creation | 1 | 1 | ✅ Complete |
| 2. Minimum 4 Tables | 1 | 1 | ✅ Complete |
| 3. Tables with 5+ Attributes | 1 | 1 | ✅ Complete |
| 4. Validation Rules (5 Business + 5 Technical) | 5 | 2.5 | ⚠️ Partial |
| 5. MVC, DAO, JDBC, CRUD | 10 | 10 | ✅ Complete |
| 6. 4+ GUI Pages with Communication | 5 | 5 | ✅ Complete |
| 7. JOptionPane Messages | 2 | 1.5 | ⚠️ Partial |
| 8. Viva | 5 | N/A | ⚠️ N/A |
| **TOTAL** | **30** | **22/25** | **73.3%** |

*Note: Viva score (5 points) not included in calculation as it requires oral examination.*

---

## Detailed Issues and Recommendations

### **Critical Issues (Must Fix):**

1. **ExpensePanel and PaymentPanel Missing JOptionPane:**
   - **Location:** `ExpensePanel.java` lines 353, 361, 373, 390, 396
   - **Location:** `PaymentPanel.java` lines 409, 423
   - **Fix:** Replace `System.out.println()` with `JOptionPane.showMessageDialog()`
   - **Impact:** Requirement 7 not fully met

2. **Missing Explicit Validation Rules:**
   - **Issue:** Many validations are implicit business logic rather than explicit validation with error messages
   - **Fix:** Add explicit validation methods that check:
     - Rent amount > 0
     - Amount paid > 0
     - Phone number format
     - National ID format
     - Date validations (end date after start date)
     - Apartment number uniqueness
   - **Impact:** Requirement 4 partially met

### **Minor Issues (Should Fix):**

1. **Error Handling:**
   - Some catch blocks only print to console
   - Should show JOptionPane error messages to users

2. **Code Organization:**
   - Consider separating controller logic from view classes
   - Current implementation embeds business logic in action listeners (acceptable but not ideal MVC)

3. **Database Schema Documentation:**
   - No SQL script file found
   - Consider adding database schema documentation

### **Strengths:**

1. ✅ Excellent use of MVC and DAO patterns
2. ✅ Comprehensive CRUD operations
3. ✅ Good separation of concerns
4. ✅ Extensive GUI with 11 pages
5. ✅ Proper use of JDBC with PreparedStatement
6. ✅ Good navigation system between pages
7. ✅ JTable properly used for data display
8. ✅ Database properly named and configured

---

## Conclusion

The Apartment Rent Management System demonstrates **strong implementation** of core requirements:
- ✅ Database structure is well-designed
- ✅ MVC and DAO patterns are properly implemented
- ✅ CRUD operations are complete and functional
- ✅ GUI is extensive and well-connected
- ⚠️ Validation rules need to be more explicit with proper error messages
- ⚠️ Some panels need to use JOptionPane instead of System.out.println()

**Overall Assessment:** The project meets most requirements but needs minor fixes to fully comply with requirements 4 and 7. With the recommended fixes, this project should score **22-24 out of 25 points** (excluding viva).

**Estimated Final Score (excluding viva): 22-24/25 (88-96%)**

---

## Action Items Before Submission

1. ✅ Replace all `System.out.println()` with `JOptionPane.showMessageDialog()` in:
   - ExpensePanel.java
   - PaymentPanel.java

2. ✅ Add explicit validation methods with JOptionPane error messages for:
   - Numeric field validation (positive numbers)
   - Date validation (end date after start date)
   - Format validation (phone, national ID)
   - Uniqueness validation (apartment number)

3. ✅ Ensure all error handling shows JOptionPane messages to users

4. ✅ Prepare for viva by understanding:
   - Your code structure
   - Database design
   - Design patterns used
   - How pages communicate

---

**Document Generated:** Based on code analysis  
**Last Updated:** [Current Date]  
**Evaluator:** Code Review Analysis

