#include <windows.h>
#include <commctrl.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include "resource.h"
#include "data.h"
#include "listview.h"

/* GUI HANDLES */
HWND hEditID, hEditModel, hEditBrand, hEditRepairType, hEditCost, hEditStock;
HWND hList;
HBRUSH hLabelBg;
int sortColumn = 0;
int sortAsc = 1;
HWND hTitle;
HFONT hTitleFont;
HBRUSH hBtnBrush;
HBRUSH hTitleBrush;
HFONT hLabelFontBold;
HWND hLblID, hLblModel, hLblBrand, hLblRepairType, hLblCost, hLblStock;
COLORREF headerBk = RGB(34, 139, 34);
COLORREF headerText = RGB(255, 255, 255);
LRESULT CALLBACK HeaderSubclassProc(HWND, UINT, WPARAM, LPARAM, UINT_PTR, DWORD_PTR);

static LRESULT HeaderCustomDraw(HWND hList, NMHDR* pNMHDR) {
    NMCUSTOMDRAW* p = (NMCUSTOMDRAW*)pNMHDR;
    switch (p->dwDrawStage) {
        case CDDS_PREPAINT:
            return CDRF_NOTIFYITEMDRAW | CDRF_NOTIFYPOSTPAINT;
        case CDDS_ITEMPREPAINT: {
            HBRUSH b = CreateSolidBrush(headerBk);
            FillRect(p->hdc, &p->rc, b);
            DeleteObject(b);
            HWND hHeader = ListView_GetHeader(hList);
            int index = (int)p->dwItemSpec;
            char text[128] = {0};
            HDITEM hi; ZeroMemory(&hi, sizeof(hi)); hi.mask = HDI_TEXT; hi.pszText = text; hi.cchTextMax = sizeof(text);
            SendMessage(hHeader, HDM_GETITEM, index, (LPARAM)&hi);
            SetTextColor(p->hdc, headerText);
            SetBkMode(p->hdc, TRANSPARENT);
            RECT r = p->rc;
            r.left += 6;
            DrawText(p->hdc, text, -1, &r, DT_SINGLELINE | DT_LEFT | DT_VCENTER);
            return CDRF_SKIPDEFAULT;
        }
        case CDDS_POSTPAINT: {
            HWND hHeader = ListView_GetHeader(hList);
            int count = Header_GetItemCount(hHeader);
            int colsWidth = 0;
            RECT rc;
            for (int i = 0; i < count; i++) {
                Header_GetItemRect(hHeader, i, &rc);
                colsWidth += rc.right - rc.left;
            }
            RECT client;
            GetClientRect(hHeader, &client);
            if (client.right > colsWidth) {
                RECT fill = client;
                fill.left = colsWidth;
                HBRUSH b = CreateSolidBrush(headerBk);
                FillRect(p->hdc, &fill, b);
                DeleteObject(b);
            }
            return CDRF_SKIPDEFAULT;
        }
    }
    return CDRF_DODEFAULT;
}

/* FUNCTION PROTOTYPES */
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK DiscountDlgProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK HistoryDlgProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK ReportDlgProc(HWND, UINT, WPARAM, LPARAM);
void ClearFields();
void AddHistoryWithDate(const char* action);
int CompareItems(const void* a, const void* b);

#ifndef IDC_REPORT_TEXT
#define IDC_REPORT_TEXT 1003
#endif

/* ===================== CLEAR INPUT FIELDS ===================== */
void ClearFields() {
    SetWindowText(hEditID,"");
    SetWindowText(hEditModel,"");
    SetWindowText(hEditBrand,"");
    SetWindowText(hEditRepairType,"");
    SetWindowText(hEditCost,"");
    SetWindowText(hEditStock,"");
}

/* ===================== HISTORY WITH DATE ===================== */
void AddHistoryWithDate(const char* action) {
    char line[200];
    time_t now = time(NULL);
    struct tm* t = localtime(&now);
    char date[40];
    strftime(date, sizeof(date), "%Y-%m-%d %H:%M", t);
    snprintf(line, sizeof(line), "[%s] %s", date, action);
    AddHistory(line);
}

/* ===================== DISCOUNT DIALOG ===================== */
INT_PTR CALLBACK DiscountDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch(msg) {
        case WM_INITDIALOG:
            SetDlgItemInt(hDlg, 1001, 10, TRUE); // default 10%
            return TRUE;
        case WM_COMMAND:
            switch(LOWORD(wParam)) {
                case IDOK: {
                    BOOL success;
                    int discount = GetDlgItemInt(hDlg, 1001, &success, TRUE);
                    if(!success || discount < 10 || discount > 20){
                        MessageBox(hDlg,"Discount must be between 10-20","Error",MB_ICONERROR);
                        return TRUE; // stay in dialog
                    }
                    EndDialog(hDlg, discount);
                    return TRUE;
                }
                case IDCANCEL:
                    EndDialog(hDlg, 0);
                    return TRUE;
            }
            break;
    }
    return FALSE;
}

/* ====================== WINMAIN ====================== */
int WINAPI WinMain(HINSTANCE hInst, HINSTANCE, LPSTR, int nCmdShow) {
    INITCOMMONCONTROLSEX icc = { sizeof(INITCOMMONCONTROLSEX), ICC_LISTVIEW_CLASSES };
    InitCommonControlsEx(&icc);

    WNDCLASS wc = {0};
    wc.lpfnWndProc = WndProc;
    wc.hInstance = hInst;
    wc.hbrBackground = (HBRUSH)(COLOR_BTNFACE + 1);
    wc.lpszClassName = "MobileInventoryApp";

    RegisterClass(&wc);

    CreateWindow(
        "MobileInventoryApp",
        "Mobile Repair Shop Inventory",
        WS_OVERLAPPEDWINDOW | WS_VISIBLE,
        80, 80, 1100, 680,
        NULL, NULL, hInst, NULL
    );

    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }
    return 0;
}

/* ====================== WINDOW PROCEDURE ====================== */
LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch(msg) {
        case WM_CREATE: {
            // Labels + Edit Fields
            hLabelBg = CreateSolidBrush(RGB(230, 230, 230));
            hBtnBrush = CreateSolidBrush(RGB(34,139,34));
            hTitleBrush = CreateSolidBrush(RGB(220,220,220));
            hTitle = CreateWindow("STATIC","Mobile Repair Shop Inventory system",WS_CHILD|WS_VISIBLE|SS_CENTER,20,8,1060,32,hwnd,(HMENU)0,0,0);
            hTitleFont = CreateFont(24,0,0,0,FW_BOLD,FALSE,FALSE,FALSE,DEFAULT_CHARSET,OUT_DEFAULT_PRECIS,CLIP_DEFAULT_PRECIS,DEFAULT_QUALITY,DEFAULT_PITCH|FF_DONTCARE,"Segoe UI");
            SendMessage(hTitle, WM_SETFONT, (WPARAM)hTitleFont, TRUE);
            hLabelFontBold = CreateFont(18,0,0,0,FW_BOLD,FALSE,FALSE,FALSE,DEFAULT_CHARSET,OUT_DEFAULT_PRECIS,CLIP_DEFAULT_PRECIS,DEFAULT_QUALITY,DEFAULT_PITCH|FF_DONTCARE,"Segoe UI");
            hLblID = CreateWindow("STATIC","Item ID",WS_CHILD|WS_VISIBLE,20,56,120,22,hwnd,0,0,0);
            SendMessage(hLblID, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditID = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,56,180,24,hwnd,(HMENU)ID_EDIT_ID,0,0);

            hLblModel = CreateWindow("STATIC","Phone Model",WS_CHILD|WS_VISIBLE,20,86,120,22,hwnd,0,0,0);
            SendMessage(hLblModel, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditModel = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,86,180,24,hwnd,(HMENU)ID_EDIT_NAME,0,0);

            hLblBrand = CreateWindow("STATIC","Phone Brand",WS_CHILD|WS_VISIBLE,20,116,120,22,hwnd,0,0,0);
            SendMessage(hLblBrand, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditBrand = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,116,180,24,hwnd,(HMENU)ID_EDIT_CATEGORY,0,0);

            hLblRepairType = CreateWindow("STATIC","Repair Type",WS_CHILD|WS_VISIBLE,20,146,120,22,hwnd,0,0,0);
            SendMessage(hLblRepairType, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditRepairType = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,146,180,24,hwnd,(HMENU)ID_EDIT_PRICE,0,0);

            hLblCost = CreateWindow("STATIC","Unit Cost",WS_CHILD|WS_VISIBLE,20,176,120,22,hwnd,0,0,0);
            SendMessage(hLblCost, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditCost = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,176,180,24,hwnd,(HMENU)ID_EDIT_QTY,0,0);

            hLblStock = CreateWindow("STATIC","Initial Qty",WS_CHILD|WS_VISIBLE,20,206,120,22,hwnd,0,0,0);
            SendMessage(hLblStock, WM_SETFONT, (WPARAM)hLabelFontBold, TRUE);
            hEditStock = CreateWindow("EDIT","",WS_CHILD|WS_VISIBLE|WS_BORDER,150,206,180,24,hwnd,(HMENU)ID_EDIT_STOCK,0,0);

            // Buttons
            CreateWindow("BUTTON","Add Item",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,240,250,30,hwnd,(HMENU)ID_BTN_ADD,0,0);
            CreateWindow("BUTTON","Update Item Stock",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,280,250,30,hwnd,(HMENU)ID_BTN_UPDATE,0,0);
            CreateWindow("BUTTON","Sell Item",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,320,250,30,hwnd,(HMENU)ID_BTN_SELL,0,0);
            CreateWindow("BUTTON","Sell Item with Discount",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,360,250,30,hwnd,(HMENU)ID_BTN_DISCOUNT,0,0);
            CreateWindow("BUTTON","Remove Item",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,400,250,30,hwnd,(HMENU)ID_BTN_REMOVE,0,0);
            CreateWindow("BUTTON","Check Item Stock",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,440,250,30,hwnd,(HMENU)ID_BTN_CHECK,0,0);
            CreateWindow("BUTTON","Total Inventory Value",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,480,250,30,hwnd,(HMENU)ID_BTN_VALUE,0,0);
            CreateWindow("BUTTON","Item History",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,520,250,30,hwnd,(HMENU)ID_BTN_HISTORY,0,0);
            CreateWindow("BUTTON","Item Report",WS_CHILD|WS_VISIBLE|BS_OWNERDRAW,20,560,250,30,hwnd,(HMENU)ID_BTN_REPORT,0,0);

            // ListView
            hList = CreateWindow(
                WC_LISTVIEW, "",
                WS_CHILD | WS_VISIBLE | WS_BORDER | LVS_REPORT | LVS_SINGLESEL,
                340, 56, 740, 520,
                hwnd, (HMENU)ID_LISTVIEW, 0, 0
            );

            ListView_SetExtendedListViewStyle(hList, LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES);
            {
                HWND hHdr = ListView_GetHeader(hList);
                SetWindowSubclass(hHdr, HeaderSubclassProc, 101, 0);
            }

            // Columns
            LVCOLUMN col = {0};
            col.mask = LVCF_TEXT | LVCF_WIDTH;
            int listWidth = 740;
            int col0 = 80, col1 = 180, col2 = 130, col3 = 120, col4 = 70;
            int col5 = listWidth - (col0+col1+col2+col3+col4);
            col.cx = col0; col.pszText = "Item ID"; ListView_InsertColumn(hList,0,&col);
            col.cx = col1; col.pszText = "Phone Model"; ListView_InsertColumn(hList,1,&col);
            col.cx = col2; col.pszText = "Phone Brand"; ListView_InsertColumn(hList,2,&col);
            col.cx = col3; col.pszText = "Repair Type"; ListView_InsertColumn(hList,3,&col);
            col.cx = col4; col.pszText = "Stock"; ListView_InsertColumn(hList,4,&col);
            col.cx = col5; col.pszText = "Unit Cost"; ListView_InsertColumn(hList,5,&col);

            LoadFromFile();
            RefreshListView(hList);
            break;
        }

        case WM_COMMAND: {
            char buf[100];
            switch(LOWORD(wParam)) {

                case ID_BTN_ADD: {
                    MobileItem m;
                    GetWindowText(hEditID, buf, 20); m.itemID=atoi(buf);
                    if(m.itemID<=0){ MessageBox(hwnd,"Enter a valid Item ID (> 0) in the Item ID field.","Missing ID",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditModel, m.phoneModel,50);
                    if(strlen(m.phoneModel)==0){ MessageBox(hwnd,"Enter Phone Model in the Model field.","Missing Model",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditBrand, m.brand,30);
                    if(strlen(m.brand)==0){ MessageBox(hwnd,"Enter Phone Brand in the Brand field.","Missing Brand",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditRepairType, m.repairType,40);
                    if(strlen(m.repairType)==0){ MessageBox(hwnd,"Enter Repair Type in the Repair Type field.","Missing Repair Type",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditCost, buf,20); m.repairCost=atof(buf);
                    if(m.repairCost<=0){ MessageBox(hwnd,"Enter Unit Cost (> 0) in the Unit Cost field.","Missing Cost",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditStock, buf,20); m.partsStock=atoi(buf);
                    if(m.partsStock<=0){ MessageBox(hwnd,"Enter Initial Qty (> 0) in the Initial Qty field.","Missing Quantity",MB_ICONINFORMATION); break; }
                    m.repairCount=0;

                    int exists = 0;
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==m.itemID){ exists=1; break;}
                    if(exists){ MessageBox(hwnd,"This Item ID already exists. Use Update Item Stock to add more units.","Duplicate ID",MB_ICONINFORMATION); break;}

                    inventory[itemCount++]=m;
                    snprintf(buf,sizeof(buf), "Added Item: %s %s, Qty %d at %.2f", m.phoneModel, m.brand, m.partsStock, m.repairCost);
                    AddHistoryWithDate(buf);

                    SaveToFile();
                    RefreshListView(hList);
                    ClearFields();
                    MessageBox(hwnd,"Item added successfully. You can now Update Item Stock or Sell Item.","Success",MB_OK);
                    break;
                }

                case ID_BTN_UPDATE: {
                    int id, qty, found=-1;
                    GetWindowText(hEditID, buf,20); id=atoi(buf);
                    if(id<=0){ MessageBox(hwnd,"Enter Item ID in the Item ID field before updating.","Missing ID",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditStock, buf,20); qty=atoi(buf);
                    if(qty<=0){ MessageBox(hwnd,"Enter additional quantity in the Initial Qty field.","Missing Quantity",MB_ICONINFORMATION); break; }
                    if(qty<=5){MessageBox(hwnd,"Additional quantity must be more than 5 units.","Rule Reminder",MB_ICONINFORMATION); break;}
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==id){found=i; break;}
                    if(found==-1){MessageBox(hwnd,"Item ID not found. Add the item first.","Not Found",MB_ICONINFORMATION); break;}
                    inventory[found].partsStock += qty;
                    snprintf(buf,sizeof(buf),"Updated item stock for ID %d (+%d)", id, qty);
                    AddHistoryWithDate(buf);
                    SaveToFile();
                    RefreshListView(hList);
                    MessageBox(hwnd,"Item stock updated successfully.","Info",MB_OK);
                    break;
                }

                case ID_BTN_SELL: {
                    int id, qty, found=-1;
                    GetWindowText(hEditID, buf,20); id=atoi(buf);
                    if(id<=0){ MessageBox(hwnd,"Enter Item ID in the Item ID field before selling.","Missing ID",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditStock, buf,20); qty=atoi(buf);
                    if(qty<=0){ MessageBox(hwnd,"Enter quantity to sell in the Initial Qty field.","Missing Quantity",MB_ICONINFORMATION); break; }
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==id){found=i; break;}
                    if(found==-1){MessageBox(hwnd,"Item ID not found. Add the item first.","Not Found",MB_ICONINFORMATION); break;}
                    if(qty<5){MessageBox(hwnd,"Enter quantity of 5 or more to sell.","Rule Reminder",MB_ICONINFORMATION); break;}
                    if(qty>inventory[found].partsStock){MessageBox(hwnd,"Requested quantity exceeds available stock.","Not Enough Stock",MB_ICONINFORMATION); break;}
                    inventory[found].partsStock -= qty;
                    inventory[found].repairCount += qty;

                    float total = inventory[found].repairCost * qty;
                    snprintf(buf,sizeof(buf),"Sold %d units of ID %d (total %.2f)", qty, id, total);
                    AddHistoryWithDate(buf);

                    SaveToFile();
                    RefreshListView(hList);
                    MessageBox(hwnd,"Sale recorded.","Info",MB_OK);
                    break;
                }

                case ID_BTN_DISCOUNT: { // Sell with discount
                    int id, qty, found=-1;
                    GetWindowText(hEditID, buf,20); id=atoi(buf);
                    if(id<=0){ MessageBox(hwnd,"Enter Item ID in the Item ID field before applying discount.","Missing ID",MB_ICONINFORMATION); break; }
                    GetWindowText(hEditStock, buf,20); qty=atoi(buf);
                    if(qty<=0){ MessageBox(hwnd,"Enter quantity in the Initial Qty field before applying discount.","Missing Quantity",MB_ICONINFORMATION); break; }
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==id){found=i; break;}
                    if(found==-1){MessageBox(hwnd,"Item ID not found. Add the item first.","Not Found",MB_ICONINFORMATION); break;}
                    if(qty<5){MessageBox(hwnd,"Enter quantity of 5 or more to sell with discount.","Rule Reminder",MB_ICONINFORMATION); break;}
                    if(qty>inventory[found].partsStock){MessageBox(hwnd,"Requested quantity exceeds available stock.","Not Enough Stock",MB_ICONINFORMATION); break;}

                    int disc = DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DISCOUNT), hwnd, DiscountDlgProc);
                    if(disc == 0) break; // cancelled

                    float discounted = inventory[found].repairCost * (1 - disc/100.0f) * qty;
                    sprintf(buf,"Discounted total: %.2f", discounted);
                    MessageBox(hwnd,buf,"Discount Sale",MB_OK);

                    inventory[found].partsStock -= qty;
                    inventory[found].repairCount += qty;
                    snprintf(buf,sizeof(buf),"Sold %d units of ID %d with %d%% discount", qty, id, disc);
                    AddHistoryWithDate(buf);

                    SaveToFile();
                    RefreshListView(hList);
                    break;
                }

                case ID_BTN_REMOVE: {
                    int id, found=-1;
                    GetWindowText(hEditID, buf,20); id=atoi(buf);
                    if(id<=0){ MessageBox(hwnd,"Enter Item ID in the Item ID field to remove an item.","Missing ID",MB_ICONINFORMATION); break; }
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==id){found=i; break;}
                    if(found==-1){MessageBox(hwnd,"Item ID not found. Check the ID and try again.","Not Found",MB_ICONINFORMATION); break;}
                    if(MessageBox(hwnd,"Do you want to remove this item?","Confirm Removal",MB_YESNO|MB_ICONQUESTION)!=IDYES) break;
                    for(int i=found;i<itemCount-1;i++) inventory[i]=inventory[i+1];
                    itemCount--;

                    snprintf(buf,sizeof(buf),"Removed Item ID %d", id);
                    AddHistoryWithDate(buf);

                    SaveToFile();
                    RefreshListView(hList);
                    MessageBox(hwnd,"Item removed from inventory.","Info",MB_OK);
                    break;
                }

                case ID_BTN_CHECK: {
                    int id, found=-1;
                    GetWindowText(hEditID, buf,20); id=atoi(buf);
                    if(id<=0){ MessageBox(hwnd,"Enter Item ID in the Item ID field to check stock level.","Missing ID",MB_ICONINFORMATION); break; }
                    for(int i=0;i<itemCount;i++) if(inventory[i].itemID==id){found=i; break;}
                    if(found==-1){MessageBox(hwnd,"Item ID not found. Add or select a valid ID.","Not Found",MB_ICONINFORMATION); break;}
                    int s = inventory[found].partsStock;
                    sprintf(buf,"Current stock level: %d",s);
                    UINT flags = (s < 5) ? (MB_OK | MB_ICONWARNING) : MB_OK;
                    MessageBox(hwnd,buf,"Item Stock Level",flags);
                    break;
                }

                case ID_BTN_VALUE: {
                    float total=0;
                    if(itemCount==0){ MessageBox(hwnd,"Inventory is empty. Add items first.","No Data",MB_ICONINFORMATION); break; }
                    for(int i=0;i<itemCount;i++) total+=inventory[i].partsStock*inventory[i].repairCost;
                    sprintf(buf,"Total inventory value: %.2f",total);
                    MessageBox(hwnd,buf,"Inventory Value",MB_OK);
                    break;
                }

                case ID_BTN_HISTORY: {
                    DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_HISTORY), hwnd, HistoryDlgProc);
                    break;
                }

                case ID_BTN_REPORT: {
                    float total=0; int sold=0;
                    int bestIndex=-1; int bestCount=-1;
                    for(int i=0;i<itemCount;i++){
                        total+=inventory[i].partsStock*inventory[i].repairCost;
                        sold+=inventory[i].repairCount;
                        if(inventory[i].repairCount>bestCount){bestCount=inventory[i].repairCount;bestIndex=i;}
                    }
                    char report[256];
                    if(bestIndex>=0){
                        sprintf(report,"Total Items: %d\nTotal Sold Units: %d\nStock Value: %.2f\nMost Active Item: ID %d (%s) - %d units sold",
                            itemCount,sold,total,inventory[bestIndex].itemID,inventory[bestIndex].phoneModel,inventory[bestIndex].repairCount);
                    } else {
                        sprintf(report,"Total Items: %d\nTotal Sold Units: %d\nStock Value: %.2f",itemCount,sold,total);
                    }
                    MessageBox(hwnd,report,"Item Report",MB_OK);
                    break;
                }
            }
            break;
        }

        case WM_NOTIFY: {
            LPNMHDR phdr = (LPNMHDR)lParam;
            if(phdr->idFrom==ID_LISTVIEW && phdr->code==NM_CUSTOMDRAW)
                return ListViewCustomDraw(hList,(LPNMHDR)lParam);
            {
                HWND hHeader = ListView_GetHeader(hList);
                if(phdr->hwndFrom == hHeader && phdr->code == NM_CUSTOMDRAW){
                    return HeaderCustomDraw(hList, (NMHDR*)lParam);
                }
            }
            HWND hHeader = ListView_GetHeader(hList);
            if(phdr->hwndFrom == hHeader && phdr->code == HDN_ITEMCLICK){
                int col = ((LPNMHEADER)lParam)->iItem;
                if(col==sortColumn) sortAsc = !sortAsc; else { sortColumn = col; sortAsc = 1; }
                qsort(inventory, itemCount, sizeof(MobileItem), CompareItems);
                RefreshListView(hList);
            }
            break;
        }

        case WM_CTLCOLORSTATIC: {
            HDC hdc = (HDC)wParam;
            HWND hCtl = (HWND)lParam;
            if (hCtl == hTitle) {
                SetBkColor(hdc, RGB(240,240,240));
                SetTextColor(hdc, RGB(32,32,32));
                return (LRESULT)hTitleBrush;
            } else {
                SetBkColor(hdc, RGB(230, 230, 230));
                SetTextColor(hdc, RGB(0, 0, 0));
                return (LRESULT)hLabelBg;
            }
        }

        case WM_DRAWITEM: {
            DRAWITEMSTRUCT* dis = (DRAWITEMSTRUCT*)lParam;
            if (dis->CtlType == ODT_BUTTON) {
                FillRect(dis->hDC, &dis->rcItem, hBtnBrush);
                SetTextColor(dis->hDC, RGB(255,255,255));
                SetBkMode(dis->hDC, TRANSPARENT);
                char text[128];
                GetWindowText(dis->hwndItem, text, sizeof(text));
                DrawText(dis->hDC, text, -1, (RECT*)&dis->rcItem, DT_CENTER | DT_VCENTER | DT_SINGLELINE);
                return TRUE;
            }
            break;
        }

        case WM_DESTROY:
            if(hLabelBg) DeleteObject(hLabelBg);
            if(hTitleFont) DeleteObject(hTitleFont);
            if(hBtnBrush) DeleteObject(hBtnBrush);
            if(hTitleBrush) DeleteObject(hTitleBrush);
            if(hLabelFontBold) DeleteObject(hLabelFontBold);
            {
                HWND hHdr = ListView_GetHeader(hList);
                RemoveWindowSubclass(hHdr, HeaderSubclassProc, 101);
            }
            PostQuitMessage(0);
            break;

        default:
            return DefWindowProc(hwnd,msg,wParam,lParam);
    }

    return 0;
}

INT_PTR CALLBACK HistoryDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch(msg){
        case WM_INITDIALOG:
            for(int i=0;i<historyCount;i++){
                SendDlgItemMessage(hDlg, IDC_HISTORY_LIST, LB_ADDSTRING, 0, (LPARAM)history[i]);
            }
            return TRUE;
        case WM_COMMAND:
            if(LOWORD(wParam)==IDOK){ EndDialog(hDlg, IDOK); return TRUE; }
            break;
    }
    return FALSE;
}

int CompareItems(const void* a, const void* b){
    const MobileItem* x = (const MobileItem*)a;
    const MobileItem* y = (const MobileItem*)b;
    int res = 0;
    switch(sortColumn){
        case 0: res = x->itemID - y->itemID; break;
        case 1: res = strcmp(x->phoneModel, y->phoneModel); break;
        case 2: res = strcmp(x->brand, y->brand); break;
        case 3: res = strcmp(x->repairType, y->repairType); break;
        case 4: res = x->partsStock - y->partsStock; break;
        case 5: res = (x->repairCost > y->repairCost) - (x->repairCost < y->repairCost); break;
        default: res = 0;
    }
    return sortAsc ? res : -res;
}

INT_PTR CALLBACK ReportDlgProc(HWND hDlg, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch(msg){
        case WM_INITDIALOG: {
            float total=0; int sold=0;
            int bestIndex=-1; int bestCount=-1;
            for(int i=0;i<itemCount;i++){
                total+=inventory[i].partsStock*inventory[i].repairCost;
                sold+=inventory[i].repairCount;
                if(inventory[i].repairCount>bestCount){bestCount=inventory[i].repairCount;bestIndex=i;}
            }
            char line[256];
            sprintf(line,"Total Items: %d", itemCount);
            SendDlgItemMessage(hDlg, IDC_REPORT_TEXT, LB_ADDSTRING, 0, (LPARAM)line);
            sprintf(line,"Total Sold Units: %d", sold);
            SendDlgItemMessage(hDlg, IDC_REPORT_TEXT, LB_ADDSTRING, 0, (LPARAM)line);
            sprintf(line,"Stock Value: %.2f", total);
            SendDlgItemMessage(hDlg, IDC_REPORT_TEXT, LB_ADDSTRING, 0, (LPARAM)line);
            if(bestIndex>=0){
                sprintf(line,"Most Active Item: ID %d (%s) - %d units sold",
                    inventory[bestIndex].itemID,inventory[bestIndex].phoneModel,inventory[bestIndex].repairCount);
                SendDlgItemMessage(hDlg, IDC_REPORT_TEXT, LB_ADDSTRING, 0, (LPARAM)line);
            }
            return TRUE;
        }
        case WM_COMMAND:
            if(LOWORD(wParam)==IDOK){ EndDialog(hDlg, IDOK); return TRUE; }
            break;
    }
    return FALSE;
}
LRESULT CALLBACK HeaderSubclassProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, UINT_PTR uIdSubclass, DWORD_PTR dwRefData){
    switch(msg){
        case WM_ERASEBKGND:
            return 1;
        case WM_PAINT: {
            PAINTSTRUCT ps; HDC hdc = BeginPaint(hWnd, &ps);
            RECT client; GetClientRect(hWnd, &client);
            HBRUSH b = CreateSolidBrush(headerBk);
            FillRect(hdc, &client, b);
            DeleteObject(b);
            int count = Header_GetItemCount(hWnd);
            for(int i=0;i<count;i++){
                RECT rc; Header_GetItemRect(hWnd, i, &rc);
                char text[128]={0};
                HDITEM hi; ZeroMemory(&hi,sizeof(hi)); hi.mask = HDI_TEXT; hi.pszText = text; hi.cchTextMax = sizeof(text);
                SendMessage(hWnd, HDM_GETITEM, i, (LPARAM)&hi);
                SetTextColor(hdc, headerText);
                SetBkMode(hdc, TRANSPARENT);
                rc.left += 6;
                DrawText(hdc, text, -1, &rc, DT_SINGLELINE | DT_LEFT | DT_VCENTER);
            }
            EndPaint(hWnd, &ps);
            return 0;
        }
    }
    return DefSubclassProc(hWnd, msg, wParam, lParam);
}
