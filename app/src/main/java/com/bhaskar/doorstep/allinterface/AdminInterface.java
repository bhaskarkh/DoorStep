package com.bhaskar.doorstep.allinterface;

public interface AdminInterface {
    public void afterAdminVerificationSuccess(String callingSource);
    public  void afterAdminVerificationFailed(String callingSource);
}
