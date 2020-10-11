package com.bhaskar.doorstep.allinterface;

import com.bhaskar.doorstep.model.UserRegistrationDTO;

public interface UserRegistrationDetailsInterface {
    public void saveToSharedPref(UserRegistrationDTO userRegistrationDTO);
}
