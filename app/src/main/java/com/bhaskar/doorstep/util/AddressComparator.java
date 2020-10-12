package com.bhaskar.doorstep.util;

import com.bhaskar.doorstep.model.AddressDTO;

import java.util.Comparator;

public class AddressComparator implements Comparator<AddressDTO> {
    @Override
    public int compare(AddressDTO o1, AddressDTO o2) {
        int b1 = o1.isPrimaryAddress() ? 1 : 0;
        int b2 = o2.isPrimaryAddress() ? 1 : 0;
        int bolReturn=b2-b1;
        if(bolReturn==0) return o1.getId() - o2.getId();


        return bolReturn;
    }
}
