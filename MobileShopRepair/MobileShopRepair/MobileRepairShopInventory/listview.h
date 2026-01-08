#ifndef LISTVIEW_H
#define LISTVIEW_H

#include <windows.h>
#include <commctrl.h>

void RefreshListView(HWND hList);
LRESULT ListViewCustomDraw(HWND hList, NMHDR* pNMHDR);

#endif
