package com.gramasuvidha.app.utils;

import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;

public class LanguageUtils {

    public static Context setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    public static String formatBudget(double amount) {
        if (amount >= 10000000) {
            return String.format("₹%.1f Cr", amount / 10000000);
        } else if (amount >= 100000) {
            return String.format("₹%.1f L", amount / 100000);
        } else {
            return String.format("₹%.0f", amount);
        }
    }

    public static int getStatusColor(String status) {
        switch (status) {
            case "Completed": return 0xFF4CAF50;
            case "In Progress": return 0xFF2196F3;
            case "Not Started": return 0xFF9E9E9E;
            case "Delayed": return 0xFFFF5722;
            default: return 0xFF607D8B;
        }
    }

    public static int getCategoryIcon(String category) {
        switch (category) {
            case "Road Construction": return android.R.drawable.ic_menu_directions;
            case "Water Supply": return android.R.drawable.ic_menu_compass;
            case "Public Facility": return android.R.drawable.ic_menu_agenda;
            case "Drainage": return android.R.drawable.ic_menu_rotate;
            case "Lake/Pond Rejuvenation": return android.R.drawable.ic_menu_mapmode;
            default: return android.R.drawable.ic_menu_help;
        }
    }
}
