package com.mckesson.smora.util;

import java.util.Comparator;

import com.mckesson.smora.dto.UserAccountVO;

/**
 * QC-9713 : Sort by Account name
 *
 */
public class AccountNameComparator implements Comparator<UserAccountVO>{
 
    public int compare(UserAccountVO o1, UserAccountVO o2) {
        return (o1.getDescription().compareToIgnoreCase(o2.getDescription()));
    }
} 
