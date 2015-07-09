package com.mckesson.smora.util;

import java.util.Comparator;

import com.mckesson.smora.dto.AccountDetailsVO;


/**
 * QC-9713 : Sort by Account name
 *
 */
public class AccountDetailsComparator implements Comparator<AccountDetailsVO>{
 
    public int compare(AccountDetailsVO o1, AccountDetailsVO o2) {
        return (o1.getAccountName().compareToIgnoreCase(o2.getAccountName()));
    }
} 

