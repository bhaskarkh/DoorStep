package com.bhaskar.doorstep.allinterface;

import com.bhaskar.doorstep.model.OrderDTO;

import java.util.List;

public interface OrderStatusInterface {
    public void setOrderStatusAdaptor(List<OrderDTO> orderDTOList);
    public void orderStatusChange();
}
