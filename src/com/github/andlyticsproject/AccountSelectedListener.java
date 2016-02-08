package com.github.andlyticsproject;

import com.github.andlyticsproject.model.DeveloperAccount;


public interface AccountSelectedListener {
    void accountSelected(boolean checked, DeveloperAccount account);
}
