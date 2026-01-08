#include <windows.h>
#include <commctrl.h>
#include <stdio.h>
#include "data.h"

/* Extern globals from data.c */
extern MobileItem inventory[];
extern int itemCount;

/* ====================== REFRESH LISTVIEW ====================== */
void RefreshListView(HWND hList) {
    ListView_DeleteAllItems(hList);
    char buf[100];

    for (int i = 0; i < itemCount; i++) {
        LVITEM item = {0};
        item.iItem = i;
        item.mask = LVIF_TEXT;
        item.iSubItem = 0;

        // Column 0: ID
        sprintf(buf, "%d", inventory[i].itemID);
        item.pszText = buf;
        ListView_InsertItem(hList, &item);

        // Columns 1-3
        ListView_SetItemText(hList, i, 1, inventory[i].phoneModel);
        ListView_SetItemText(hList, i, 2, inventory[i].brand);
        ListView_SetItemText(hList, i, 3, inventory[i].repairType);

        // Column 4: Stock
        sprintf(buf, "%d", inventory[i].partsStock);
        ListView_SetItemText(hList, i, 4, buf);

        // Column 5: Cost
        sprintf(buf, "%.2f", inventory[i].repairCost);
        ListView_SetItemText(hList, i, 5, buf);
    }
}

/* ====================== CUSTOM DRAW ====================== */
LRESULT ListViewCustomDraw(HWND hList, NMHDR* pNMHDR) {
    LPNMLVCUSTOMDRAW pLVCD = (LPNMLVCUSTOMDRAW)pNMHDR;

    switch(pLVCD->nmcd.dwDrawStage) {
        case CDDS_PREPAINT:
            return CDRF_NOTIFYITEMDRAW;

        case CDDS_ITEMPREPAINT: {
            int index = (int)pLVCD->nmcd.dwItemSpec;
            if (inventory[index].partsStock < 5) {
                pLVCD->clrText = RGB(255, 0, 0); // Red if low stock
            } else {
                pLVCD->clrText = RGB(0, 0, 0);   // Black normally
            }
            return CDRF_DODEFAULT;
        }
    }
    return CDRF_DODEFAULT;
}
