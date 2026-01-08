#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "data.h"

/* ====================== GLOBAL VARIABLES ====================== */
MobileItem inventory[MAX_ITEMS];
int itemCount = 0;
int totalRepairs = 0;

char history[MAX_HISTORY][200];
int historyCount = 0;

void LoadFromFile(void) {
    FILE* f = fopen("inventory.csv", "r");
    itemCount = 0;
    if (f) {
        char line[512];
        while (fgets(line, sizeof(line), f)) {
            char* p = strtok(line, ",");
            if (!p) continue;
            MobileItem m;
            m.itemID = atoi(p);
            p = strtok(NULL, ","); if (!p) continue; strncpy(m.phoneModel, p, sizeof(m.phoneModel)); m.phoneModel[sizeof(m.phoneModel)-1]=0;
            p = strtok(NULL, ","); if (!p) continue; strncpy(m.brand, p, sizeof(m.brand)); m.brand[sizeof(m.brand)-1]=0;
            p = strtok(NULL, ","); if (!p) continue; strncpy(m.repairType, p, sizeof(m.repairType)); m.repairType[sizeof(m.repairType)-1]=0;
            p = strtok(NULL, ","); if (!p) continue; m.repairCost = (float)atof(p);
            p = strtok(NULL, ","); if (!p) continue; m.partsStock = atoi(p);
            p = strtok(NULL, ","); if (!p) continue; m.repairCount = atoi(p);
            if (itemCount < MAX_ITEMS) inventory[itemCount++] = m;
        }
        fclose(f);
    }
    FILE* h = fopen("history.txt", "r");
    historyCount = 0;
    if (h) {
        char line[256];
        while (fgets(line, sizeof(line), h)) {
            size_t len = strlen(line);
            if (len && line[len-1] == '\n') line[len-1] = 0;
            if (historyCount < MAX_HISTORY) {
                snprintf(history[historyCount++], 200, "%s", line);
            }
        }
        fclose(h);
    }
}

void SaveToFile(void) {
    FILE* f = fopen("inventory.csv", "w");
    if (f) {
        for (int i = 0; i < itemCount; i++) {
            fprintf(f, "%d,%s,%s,%s,%.2f,%d,%d\n",
                    inventory[i].itemID,
                    inventory[i].phoneModel,
                    inventory[i].brand,
                    inventory[i].repairType,
                    inventory[i].repairCost,
                    inventory[i].partsStock,
                    inventory[i].repairCount);
        }
        fclose(f);
    }
    FILE* h = fopen("history.txt", "w");
    if (h) {
        for (int i = 0; i < historyCount; i++) {
            fprintf(h, "%s\n", history[i]);
        }
        fclose(h);
    }
}
void AddHistory(const char* action) {
    if(historyCount < MAX_HISTORY) {
        snprintf(history[historyCount++], 200, "%s", action);
    }
}
