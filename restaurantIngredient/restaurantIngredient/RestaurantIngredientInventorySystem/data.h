#ifndef R_DATA_H
#define R_DATA_H
#
#define R_MAX_ITEMS 200
#define R_MAX_HISTORY 200
#
typedef struct {
    int ingredient_id;
    char ingredient_name[50];
    char category[40];
    float unit_price;
    int quantity;
    int usage_count;
    char date_added[20];
} Ingredient;
#
extern Ingredient r_inventory[R_MAX_ITEMS];
extern int r_itemCount;
extern char r_history[R_MAX_HISTORY][200];
extern int r_historyCount;
#
void R_LoadFromFile(void);
void R_SaveToFile(void);
void R_AddHistory(const char* action);
#
#endif
