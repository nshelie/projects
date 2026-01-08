#ifndef R_LISTVIEW_H
#define R_LISTVIEW_H
#
#include <windows.h>
#include <commctrl.h>
#
void R_RefreshListView(HWND hList);
LRESULT R_ListViewCustomDraw(HWND hList, NMHDR* pNMHDR);
#
#endif
