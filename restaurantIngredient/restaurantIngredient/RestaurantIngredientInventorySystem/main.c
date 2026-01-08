#include <windows.h>
#include <commctrl.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include <ctype.h>
#include "resource.h"
#include "data.h"
#include "listview.h"

HWND r_hEditID, r_hEditName, r_hEditCategory, r_hEditPrice, r_hEditQty;
HWND r_hList;
HWND r_hTitle;
HFONT r_hTitleFont;
HBRUSH r_btnBrush;
HBRUSH r_titleBrush;
COLORREF r_headerBk = RGB(245, 245, 245);
COLORREF r_headerText = RGB(0, 70, 140);

static void r_resizeColumns(HWND hwnd) {
    RECT rc;
    GetClientRect(r_hList, &rc);
    int width = rc.right - rc.left - 4;
    if (width < 300) width = 300;
    int w0 = (int)(width * 0.10); // ID
    int w1 = (int)(width * 0.30); // Name
    int w2 = (int)(width * 0.25); // Category
    int w3 = (int)(width * 0.10); // Qty
    int w4 = width - (w0 + w1 + w2 + w3); // Price fills remainder
    ListView_SetColumnWidth(r_hList, 0, w0);
    ListView_SetColumnWidth(r_hList, 1, w1);
    ListView_SetColumnWidth(r_hList, 2, w2);
    ListView_SetColumnWidth(r_hList, 3, w3);
    ListView_SetColumnWidth(r_hList, 4, w4);
}

static LRESULT r_headerCustomDraw(NMHDR* pNMHDR) {
    NMCUSTOMDRAW* p = (NMCUSTOMDRAW*)pNMHDR;
    switch (p->dwDrawStage) {
        case CDDS_PREPAINT:
            return CDRF_NOTIFYITEMDRAW;
        case CDDS_ITEMPREPAINT: {
            HBRUSH b = CreateSolidBrush(r_headerBk);
            FillRect(p->hdc, &p->rc, b);
            DeleteObject(b);
            int index = (int)p->dwItemSpec;
            char text[128] = {0};
            HWND hHeader = ListView_GetHeader(r_hList);
            HDITEM hi; ZeroMemory(&hi, sizeof(hi)); hi.mask = HDI_TEXT; hi.pszText = text; hi.cchTextMax = sizeof(text);
            SendMessage(hHeader, HDM_GETITEM, index, (LPARAM)&hi);
            SetTextColor(p->hdc, r_headerText);
            SetBkMode(p->hdc, TRANSPARENT);
            RECT r = p->rc;
            DrawText(p->hdc, text, -1, &r, DT_SINGLELINE | DT_CENTER | DT_VCENTER);
            return CDRF_SKIPDEFAULT;
        }
    }
    return CDRF_DODEFAULT;
}
static float r_lastDiscount = 0.0f;

static void r_setText(HWND h, const char* s) { SetWindowText(h, s); }
static void r_getText(HWND h, char* buf, int n) { GetWindowText(h, buf, n); }
static void r_clearFields() {
    r_setText(r_hEditID, "");
    r_setText(r_hEditName, "");
    r_setText(r_hEditCategory, "");
    r_setText(r_hEditPrice, "");
    r_setText(r_hEditQty, "");
}
static void r_addHistoryWithTime(const char* action) {
    time_t t = time(NULL);
    struct tm* tm = localtime(&t);
    char stamp[64];
    strftime(stamp, sizeof(stamp), "[%Y-%m-%d %H:%M] ", tm);
    char line[260];
    snprintf(line, sizeof(line), "%s%s", stamp, action);
    R_AddHistory(line);
}

INT_PTR CALLBACK R_DiscountDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_INITDIALOG:
            SetDlgItemText(hDlg, RIDC_EDIT_DISC, "10");
            return TRUE;
        case WM_COMMAND:
            if (LOWORD(wParam) == IDOK) {
                char dBuf[32];
                GetDlgItemText(hDlg, RIDC_EDIT_DISC, dBuf, sizeof(dBuf));
                float disc = (float)atof(dBuf);
                if (disc < 10.0f || disc > 20.0f) {
                    MessageBox(hDlg, "Discount must be between 10 and 20", "Error", MB_ICONERROR);
                    return TRUE;
                }
                r_lastDiscount = disc;
                EndDialog(hDlg, 1);
                return TRUE;
            }
            if (LOWORD(wParam) == IDCANCEL) {
                EndDialog(hDlg, 0);
                return TRUE;
            }
            break;
    }
    return FALSE;
}

INT_PTR CALLBACK R_HistoryDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_INITDIALOG: {
            HWND hList = GetDlgItem(hDlg, RIDC_HISTORY_LIST);
            for (int i = 0; i < r_historyCount; i++) {
                SendMessage(hList, LB_ADDSTRING, 0, (LPARAM)r_history[i]);
            }
            return TRUE;
        }
        case WM_COMMAND:
            if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL) {
                EndDialog(hDlg, 0);
                return TRUE;
            }
            break;
    }
    return FALSE;
}

INT_PTR CALLBACK R_ReportDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_INITDIALOG: {
            int totalItems = r_itemCount;
            float stockValue = 0.0f;
            int mostIdx = -1;
            int mostUse = -1;
            for (int i = 0; i < r_itemCount; i++) {
                stockValue += r_inventory[i].quantity * r_inventory[i].unit_price;
                if (r_inventory[i].usage_count > mostUse) {
                    mostUse = r_inventory[i].usage_count;
                    mostIdx = i;
                }
            }
            char line[256];
            HWND hList = GetDlgItem(hDlg, RIDC_REPORT_LIST);
            snprintf(line, sizeof(line), "Total Items: %d", totalItems);
            SendMessage(hList, LB_ADDSTRING, 0, (LPARAM)line);
            snprintf(line, sizeof(line), "Total Stock Value: %.2f", stockValue);
            SendMessage(hList, LB_ADDSTRING, 0, (LPARAM)line);
            if (mostIdx >= 0) {
                snprintf(line, sizeof(line), "Most Used: %s (%d)", r_inventory[mostIdx].ingredient_name, r_inventory[mostIdx].usage_count);
                SendMessage(hList, LB_ADDSTRING, 0, (LPARAM)line);
            }
            return TRUE;
        }
        case WM_COMMAND:
            if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL) {
                EndDialog(hDlg, 0);
                return TRUE;
            }
            break;
    }
    return FALSE;
}

LRESULT CALLBACK R_WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_CREATE: {
            InitCommonControls();
            r_btnBrush = CreateSolidBrush(RGB(30, 144, 255));
            r_titleBrush = CreateSolidBrush(RGB(240, 240, 240));
            r_hTitle = CreateWindowEx(0, "STATIC", "Restaurant Ingredient Inventory system", WS_CHILD | WS_VISIBLE | SS_CENTER, 20, 8, 740, 32, hwnd, (HMENU)RID_TITLE, 0, 0);
            r_hTitleFont = CreateFont(24, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS, DEFAULT_QUALITY, DEFAULT_PITCH | FF_DONTCARE, "Segoe UI");
            SendMessage(r_hTitle, WM_SETFONT, (WPARAM)r_hTitleFont, TRUE);

            CreateWindowEx(0, "STATIC", "Ingredient ID", WS_CHILD | WS_VISIBLE, 20, 56, 120, 20, hwnd, 0, 0, 0);
            r_hEditID = CreateWindowEx(WS_EX_CLIENTEDGE, "EDIT", "", WS_CHILD | WS_VISIBLE | ES_NUMBER, 150, 56, 150, 22, hwnd, (HMENU)RID_EDIT_ID, 0, 0);
            CreateWindowEx(0, "STATIC", "Ingredient Name", WS_CHILD | WS_VISIBLE, 20, 86, 120, 20, hwnd, 0, 0, 0);
            r_hEditName = CreateWindowEx(WS_EX_CLIENTEDGE, "EDIT", "", WS_CHILD | WS_VISIBLE, 150, 86, 150, 22, hwnd, (HMENU)RID_EDIT_NAME, 0, 0);
            CreateWindowEx(0, "STATIC", "Category", WS_CHILD | WS_VISIBLE, 20, 116, 120, 20, hwnd, 0, 0, 0);
            r_hEditCategory = CreateWindowEx(WS_EX_CLIENTEDGE, "EDIT", "", WS_CHILD | WS_VISIBLE, 150, 116, 150, 22, hwnd, (HMENU)RID_EDIT_CATEGORY, 0, 0);
            CreateWindowEx(0, "STATIC", "Unit Price", WS_CHILD | WS_VISIBLE, 20, 146, 120, 20, hwnd, 0, 0, 0);
            r_hEditPrice = CreateWindowEx(WS_EX_CLIENTEDGE, "EDIT", "", WS_CHILD | WS_VISIBLE, 150, 146, 150, 22, hwnd, (HMENU)RID_EDIT_PRICE, 0, 0);
            CreateWindowEx(0, "STATIC", "Quantity", WS_CHILD | WS_VISIBLE, 20, 176, 120, 20, hwnd, 0, 0, 0);
            r_hEditQty = CreateWindowEx(WS_EX_CLIENTEDGE, "EDIT", "", WS_CHILD | WS_VISIBLE | ES_NUMBER, 150, 176, 150, 22, hwnd, (HMENU)RID_EDIT_QTY, 0, 0);

            CreateWindowEx(0, "BUTTON", "Add Ingredient", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 320, 56, 150, 28, hwnd, (HMENU)RID_BTN_ADD, 0, 0);
            CreateWindowEx(0, "BUTTON", "Update Stock", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 320, 86, 150, 28, hwnd, (HMENU)RID_BTN_UPDATE, 0, 0);
            CreateWindowEx(0, "BUTTON", "Use Ingredient", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 320, 116, 150, 28, hwnd, (HMENU)RID_BTN_USE, 0, 0);
            CreateWindowEx(0, "BUTTON", "Discounted Use", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 320, 146, 150, 28, hwnd, (HMENU)RID_BTN_DISCOUNT, 0, 0);
            CreateWindowEx(0, "BUTTON", "Remove Ingredient", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 320, 176, 150, 28, hwnd, (HMENU)RID_BTN_REMOVE, 0, 0);
            CreateWindowEx(0, "BUTTON", "Check Stock", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 480, 56, 130, 28, hwnd, (HMENU)RID_BTN_CHECK, 0, 0);
            CreateWindowEx(0, "BUTTON", "Total Value", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 480, 86, 130, 28, hwnd, (HMENU)RID_BTN_VALUE, 0, 0);
            CreateWindowEx(0, "BUTTON", "History", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 480, 116, 130, 28, hwnd, (HMENU)RID_BTN_HISTORY, 0, 0);
            CreateWindowEx(0, "BUTTON", "Report", WS_CHILD | WS_VISIBLE | BS_OWNERDRAW, 480, 146, 130, 28, hwnd, (HMENU)RID_BTN_REPORT, 0, 0);

            r_hList = CreateWindowEx(WS_EX_CLIENTEDGE, WC_LISTVIEW, "", WS_CHILD | WS_VISIBLE | LVS_REPORT | LVS_SINGLESEL, 20, 220, 740, 230, hwnd, (HMENU)RID_LISTVIEW, 0, 0);
            ListView_SetExtendedListViewStyle(r_hList, LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES | LVS_EX_DOUBLEBUFFER);
            LVCOLUMN col = {0};
            col.mask = LVCF_TEXT | LVCF_WIDTH | LVCF_SUBITEM;
            col.pszText = "ID"; col.cx = 60; col.iSubItem = 0; ListView_InsertColumn(r_hList, 0, &col);
            col.pszText = "Name"; col.cx = 180; col.iSubItem = 1; ListView_InsertColumn(r_hList, 1, &col);
            col.pszText = "Category"; col.cx = 140; col.iSubItem = 2; ListView_InsertColumn(r_hList, 2, &col);
            col.pszText = "Qty"; col.cx = 80; col.iSubItem = 3; ListView_InsertColumn(r_hList, 3, &col);
            col.pszText = "Unit Price"; col.cx = 120; col.iSubItem = 4; ListView_InsertColumn(r_hList, 4, &col);
            r_resizeColumns(hwnd);

            R_LoadFromFile();
            R_RefreshListView(r_hList);
            return 0;
        }
        case WM_CTLCOLORSTATIC: {
            HDC hdc = (HDC)wParam;
            HWND hCtl = (HWND)lParam;
            if (hCtl == r_hTitle) {
                SetBkColor(hdc, RGB(240, 240, 240));
                SetTextColor(hdc, RGB(32, 32, 32));
                return (LRESULT)r_titleBrush;
            }
            return DefWindowProc(hwnd, msg, wParam, lParam);
        }
        case WM_DRAWITEM: {
            DRAWITEMSTRUCT* dis = (DRAWITEMSTRUCT*)lParam;
            if (dis->CtlType == ODT_BUTTON) {
                FillRect(dis->hDC, &dis->rcItem, r_btnBrush);
                SetTextColor(dis->hDC, RGB(255, 255, 255));
                SetBkMode(dis->hDC, TRANSPARENT);
                char text[128];
                GetWindowText(dis->hwndItem, text, sizeof(text));
                DrawText(dis->hDC, text, -1, (RECT*)&dis->rcItem, DT_CENTER | DT_VCENTER | DT_SINGLELINE);
                return TRUE;
            }
            break;
        }
        case WM_NOTIFY: {
            NMHDR* p = (NMHDR*)lParam;
            if (p->hwndFrom == r_hList && p->code == NM_CUSTOMDRAW) {
                return R_ListViewCustomDraw(r_hList, p);
            }
            {
                HWND hHeader = ListView_GetHeader(r_hList);
                if (p->hwndFrom == hHeader && p->code == NM_CUSTOMDRAW) {
                    return r_headerCustomDraw(p);
                }
            }
            break;
        }
        case WM_SIZE: {
            RECT rc; GetClientRect(hwnd, &rc);
            MoveWindow(r_hTitle, 20, 8, rc.right - 40, 32, TRUE);
            MoveWindow(r_hList, 20, 220, rc.right - 40, rc.bottom - 240, TRUE);
            r_resizeColumns(hwnd);
            break;
        }
        case WM_COMMAND: {
            char buf[128];
            switch (LOWORD(wParam)) {
                case RID_BTN_ADD: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                    r_getText(r_hEditName, buf, sizeof(buf)); char name[60]; snprintf(name, sizeof(name), "%s", buf);
                    r_getText(r_hEditCategory, buf, sizeof(buf)); char cat[50]; snprintf(cat, sizeof(cat), "%s", buf);
                    r_getText(r_hEditPrice, buf, sizeof(buf)); float price = (float)atof(buf);
                    r_getText(r_hEditQty, buf, sizeof(buf)); int qty = atoi(buf);
                    if (id <= 0) { MessageBox(hwnd, "Enter a valid Ingredient ID (> 0) in the ID field.", "Missing ID", MB_ICONINFORMATION); break; }
                    if (strlen(name) == 0) { MessageBox(hwnd, "Enter Ingredient Name in the Name field.", "Missing Name", MB_ICONINFORMATION); break; }
                    if (strlen(cat) == 0) { MessageBox(hwnd, "Enter Category in the Category field.", "Missing Category", MB_ICONINFORMATION); break; }
                    if (price <= 0) { MessageBox(hwnd, "Enter Unit Price (> 0) in the Unit Price field.", "Missing Price", MB_ICONINFORMATION); break; }
                    if (qty <= 0) { MessageBox(hwnd, "Enter Quantity (> 0) in the Quantity field.", "Missing Quantity", MB_ICONINFORMATION); break; }
                    int exists = -1;
                    for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { exists = i; break; }
                    if (exists >= 0) { MessageBox(hwnd, "This Ingredient ID already exists. Use Update Stock to add more units.", "Duplicate ID", MB_ICONINFORMATION); break; }
                    Ingredient m;
                    m.ingredient_id = id;
                    snprintf(m.ingredient_name, sizeof(m.ingredient_name), "%s", name);
                    snprintf(m.category, sizeof(m.category), "%s", cat);
                    m.unit_price = price;
                    m.quantity = qty;
                    m.usage_count = 0;
                    time_t t = time(NULL); struct tm* tm = localtime(&t);
                    strftime(m.date_added, sizeof(m.date_added), "%Y-%m-%d", tm);
                    if (r_itemCount < R_MAX_ITEMS) r_inventory[r_itemCount++] = m;
                    char msg[160];
                    snprintf(msg, sizeof(msg), "Added Ingredient: %s (%s), Qty %d at %.2f", name, cat, qty, price);
                    r_addHistoryWithTime(msg);
                    R_SaveToFile();
                    R_RefreshListView(r_hList);
                    r_clearFields();
                    MessageBox(hwnd, "Ingredient added successfully. You can now Update Stock or Use Ingredient.", "Success", MB_OK);
                    break;
                }
                case RID_BTN_UPDATE: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                    if (id <= 0) { MessageBox(hwnd, "Enter Ingredient ID in the ID field before updating.", "Missing ID", MB_ICONINFORMATION); break; }
                    r_getText(r_hEditQty, buf, sizeof(buf)); int qty = atoi(buf);
                    if (qty <= 0) { MessageBox(hwnd, "Enter additional quantity in the Quantity field.", "Missing Quantity", MB_ICONINFORMATION); break; }
                    if (qty <= 5) { MessageBox(hwnd, "Additional quantity must be more than 5 units.", "Rule Reminder", MB_ICONINFORMATION); break; }
                    int idx = -1; for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { idx = i; break; }
                    if (idx < 0) { MessageBox(hwnd, "Ingredient ID not found. Add the ingredient first.", "Not Found", MB_ICONINFORMATION); break; }
                    r_inventory[idx].quantity += qty;
                    char msg[160];
                    snprintf(msg, sizeof(msg), "Updated Stock for ID %d (+%d)", id, qty);
                    r_addHistoryWithTime(msg);
                    R_SaveToFile();
                    R_RefreshListView(r_hList);
                    MessageBox(hwnd, "Stock updated successfully.", "Info", MB_OK);
                    break;
                }
                case RID_BTN_USE: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                    if (id <= 0) { MessageBox(hwnd, "Enter Ingredient ID in the ID field before using.", "Missing ID", MB_ICONINFORMATION); break; }
                    r_getText(r_hEditQty, buf, sizeof(buf)); int qty = atoi(buf);
                    if (qty <= 0) { MessageBox(hwnd, "Enter quantity to use in the Quantity field.", "Missing Quantity", MB_ICONINFORMATION); break; }
                    int idx = -1; for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { idx = i; break; }
                    if (idx < 0) { MessageBox(hwnd, "Ingredient ID not found. Add the ingredient first.", "Not Found", MB_ICONINFORMATION); break; }
                    if (qty < 5) { MessageBox(hwnd, "Enter quantity of 5 or more to use.", "Rule Reminder", MB_ICONINFORMATION); break; }
                    if (qty > r_inventory[idx].quantity) { MessageBox(hwnd, "Requested quantity exceeds available stock.", "Not Enough Stock", MB_ICONINFORMATION); break; }
                    r_inventory[idx].quantity -= qty;
                    r_inventory[idx].usage_count += qty;
                    char msg[160];
                    snprintf(msg, sizeof(msg), "Used %d units of ID %d", qty, id);
                    r_addHistoryWithTime(msg);
                    R_SaveToFile();
                    R_RefreshListView(r_hList);
                    MessageBox(hwnd, "Ingredient usage recorded.", "Info", MB_OK);
                    break;
                }
                case RID_BTN_DISCOUNT: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int idCheck = atoi(buf);
                    if (idCheck <= 0) { MessageBox(hwnd, "Enter Ingredient ID in the ID field before applying discount.", "Missing ID", MB_ICONINFORMATION); break; }
                    r_getText(r_hEditQty, buf, sizeof(buf)); int qtyCheck = atoi(buf);
                    if (qtyCheck <= 0) { MessageBox(hwnd, "Enter quantity in the Quantity field before applying discount.", "Missing Quantity", MB_ICONINFORMATION); break; }
                    int res = (int)DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(RIDD_DISCOUNT), hwnd, R_DiscountDlgProc);
                    if (res == 1) {
                        r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                        r_getText(r_hEditQty, buf, sizeof(buf)); int qty = atoi(buf);
                        int idx = -1; for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { idx = i; break; }
                        if (idx < 0) { MessageBox(hwnd, "Ingredient ID not found. Add the ingredient first.", "Not Found", MB_ICONINFORMATION); break; }
                        float disc = r_lastDiscount;
                        float total = qty * r_inventory[idx].unit_price;
                        float discounted = total * (1.0f - disc / 100.0f);
                        char msg[160];
                        snprintf(msg, sizeof(msg), "Discounted use: qty %d, total %.2f", qty, discounted);
                        MessageBox(hwnd, msg, "Discount", MB_OK);
                    }
                    break;
                }
                case RID_BTN_REMOVE: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                    if (id <= 0) { MessageBox(hwnd, "Enter Ingredient ID in the ID field to remove an item.", "Missing ID", MB_ICONINFORMATION); break; }
                    int idx = -1; for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { idx = i; break; }
                    if (idx < 0) { MessageBox(hwnd, "Ingredient ID not found. Check the ID and try again.", "Not Found", MB_ICONINFORMATION); break; }
                    char confirm[200];
                    snprintf(confirm, sizeof(confirm), "Do you want to remove this ingredient (ID %d)?", id);
                    if (MessageBox(hwnd, confirm, "Confirm Removal", MB_YESNO | MB_ICONQUESTION) != IDYES) break;
                    for (int i = idx; i < r_itemCount - 1; i++) r_inventory[i] = r_inventory[i + 1];
                    r_itemCount--;
                    char msg[160];
                    snprintf(msg, sizeof(msg), "Removed Ingredient ID %d", id);
                    r_addHistoryWithTime(msg);
                    R_SaveToFile();
                    R_RefreshListView(r_hList);
                    MessageBox(hwnd, "Ingredient removed from inventory.", "Info", MB_OK);
                    break;
                }
                case RID_BTN_CHECK: {
                    r_getText(r_hEditID, buf, sizeof(buf)); int id = atoi(buf);
                    if (id <= 0) { MessageBox(hwnd, "Enter Ingredient ID in the ID field to check stock level.", "Missing ID", MB_ICONINFORMATION); break; }
                    int idx = -1; for (int i = 0; i < r_itemCount; i++) if (r_inventory[i].ingredient_id == id) { idx = i; break; }
                    if (idx < 0) { MessageBox(hwnd, "Ingredient ID not found. Add or select a valid ID.", "Not Found", MB_ICONINFORMATION); break; }
                    char msg[120];
                    snprintf(msg, sizeof(msg), "Stock level: %d", r_inventory[idx].quantity);
                    MessageBox(hwnd, msg, "Stock Level", MB_OK);
                    break;
                }
                case RID_BTN_VALUE: {
                    float total = 0.0f;
                    if (r_itemCount == 0) { MessageBox(hwnd, "Inventory is empty. Add ingredients first.", "No Data", MB_ICONINFORMATION); break; }
                    for (int i = 0; i < r_itemCount; i++) total += r_inventory[i].quantity * r_inventory[i].unit_price;
                    char msg[120];
                    snprintf(msg, sizeof(msg), "Total Inventory Value: %.2f", total);
                    MessageBox(hwnd, msg, "Inventory Value", MB_OK);
                    break;
                }
                case RID_BTN_HISTORY: {
                    DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(RIDD_HISTORY), hwnd, R_HistoryDlgProc);
                    break;
                }
                case RID_BTN_REPORT: {
                    DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(RIDD_REPORT), hwnd, R_ReportDlgProc);
                    break;
                }
            }
            break;
        }
        case WM_DESTROY:
            if (r_hTitleFont) DeleteObject(r_hTitleFont);
            if (r_btnBrush) DeleteObject(r_btnBrush);
            if (r_titleBrush) DeleteObject(r_titleBrush);
            PostQuitMessage(0);
            return 0;
    }
    return DefWindowProc(hwnd, msg, wParam, lParam);
}

int APIENTRY WinMain(HINSTANCE hInst, HINSTANCE hPrev, LPSTR lpCmd, int nShow) {
    WNDCLASS wc = {0};
    wc.style = CS_HREDRAW | CS_VREDRAW;
    wc.lpfnWndProc = R_WndProc;
    wc.hInstance = hInst;
    wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);
    wc.lpszClassName = "RestaurantInventoryWndClass";
    RegisterClass(&wc);
    HWND hwnd = CreateWindow("RestaurantInventoryWndClass", "Restaurant Ingredient Inventory", WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT, 800, 520, 0, 0, hInst, 0);
    ShowWindow(hwnd, nShow);
    UpdateWindow(hwnd);
    MSG msg;
    while (GetMessage(&msg, 0, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }
    return (int)msg.wParam;
}
