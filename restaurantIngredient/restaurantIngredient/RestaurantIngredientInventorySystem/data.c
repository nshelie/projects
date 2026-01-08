#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "data.h"
#
Ingredient r_inventory[R_MAX_ITEMS];
int r_itemCount = 0;
char r_history[R_MAX_HISTORY][200];
int r_historyCount = 0;
#
void R_LoadFromFile(void) {
    FILE* f = fopen("ingredients.csv", "r");
    r_itemCount = 0;
    if (f) {
        char line[600];
        while (fgets(line, sizeof(line), f)) {
            Ingredient m;
            char* p = strtok(line, ","); if(!p) continue; m.ingredient_id = atoi(p);
            p = strtok(NULL, ","); if(!p) continue; strncpy(m.ingredient_name, p, sizeof(m.ingredient_name)); m.ingredient_name[sizeof(m.ingredient_name)-1]=0;
            p = strtok(NULL, ","); if(!p) continue; strncpy(m.category, p, sizeof(m.category)); m.category[sizeof(m.category)-1]=0;
            p = strtok(NULL, ","); if(!p) continue; m.unit_price = (float)atof(p);
            p = strtok(NULL, ","); if(!p) continue; m.quantity = atoi(p);
            p = strtok(NULL, ","); if(!p) continue; m.usage_count = atoi(p);
            p = strtok(NULL, ","); if(!p) continue; strncpy(m.date_added, p, sizeof(m.date_added)); m.date_added[sizeof(m.date_added)-1]=0;
            if (r_itemCount < R_MAX_ITEMS) r_inventory[r_itemCount++] = m;
        }
        fclose(f);
    }
    FILE* h = fopen("ingredient_history.txt", "r");
    r_historyCount = 0;
    if (h) {
        char line[256];
        while (fgets(line, sizeof(line), h)) {
            size_t len = strlen(line);
            if (len && line[len-1] == '\n') line[len-1] = 0;
            if (r_historyCount < R_MAX_HISTORY) snprintf(r_history[r_historyCount++], 200, "%s", line);
        }
        fclose(h);
    }
}
#
void R_SaveToFile(void) {
    FILE* f = fopen("ingredients.csv", "w");
    if (f) {
        for (int i = 0; i < r_itemCount; i++) {
            fprintf(f, "%d,%s,%s,%.2f,%d,%d,%s\n",
                r_inventory[i].ingredient_id,
                r_inventory[i].ingredient_name,
                r_inventory[i].category,
                r_inventory[i].unit_price,
                r_inventory[i].quantity,
                r_inventory[i].usage_count,
                r_inventory[i].date_added);
        }
        fclose(f);
    }
    FILE* h = fopen("ingredient_history.txt", "w");
    if (h) {
        for (int i = 0; i < r_historyCount; i++) fprintf(h, "%s\n", r_history[i]);
        fclose(h);
    }
}
#
void R_AddHistory(const char* action) {
    if (r_historyCount < R_MAX_HISTORY) snprintf(r_history[r_historyCount++], 200, "%s", action);
}
