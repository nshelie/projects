#ifndef DATA_H
#define DATA_H

#define MAX_ITEMS 100
#define MAX_HISTORY 100

typedef struct {
    int itemID;
    char phoneModel[50];
    char brand[30];
    char repairType[40];
    float repairCost;
    int partsStock;
    int repairCount;
} MobileItem;

/* ====================== GLOBAL VARIABLES ====================== */
extern MobileItem inventory[MAX_ITEMS];
extern int itemCount;
extern int totalRepairs;

extern char history[MAX_HISTORY][200];
extern int historyCount;

/* ====================== FUNCTION PROTOTYPES ====================== */
void LoadFromFile(void);
void SaveToFile(void);
void AddHistory(const char* action);

#endif
