#include <windows.h>
#include <commctrl.h>
#include <stdio.h>
#include "data.h"
#
extern Ingredient r_inventory[];
extern int r_itemCount;
#
void R_RefreshListView(HWND hList) {
    ListView_DeleteAllItems(hList);
    char buf[128];
    for (int i = 0; i < r_itemCount; i++) {
        LVITEM item = {0};
        item.iItem = i;
        item.mask = LVIF_TEXT;
        sprintf(buf, "%d", r_inventory[i].ingredient_id);
        item.pszText = buf;
        ListView_InsertItem(hList, &item);
        ListView_SetItemText(hList, i, 1, r_inventory[i].ingredient_name);
        ListView_SetItemText(hList, i, 2, r_inventory[i].category);
        sprintf(buf, "%d", r_inventory[i].quantity);
        ListView_SetItemText(hList, i, 3, buf);
        sprintf(buf, "%.2f", r_inventory[i].unit_price);
        ListView_SetItemText(hList, i, 4, buf);
    }
}
#
LRESULT R_ListViewCustomDraw(HWND hList, NMHDR* pNMHDR) {
    LPNMLVCUSTOMDRAW pLVCD = (LPNMLVCUSTOMDRAW)pNMHDR;
    switch(pLVCD->nmcd.dwDrawStage) {
        case CDDS_PREPAINT:
            return CDRF_NOTIFYITEMDRAW;
        case CDDS_ITEMPREPAINT: {
            int index = (int)pLVCD->nmcd.dwItemSpec;
            if (r_inventory[index].quantity < 5) pLVCD->clrText = RGB(255, 0, 0);
            else pLVCD->clrText = RGB(0, 0, 0);
            return CDRF_DODEFAULT;
        }
    }
    return CDRF_DODEFAULT;
}
