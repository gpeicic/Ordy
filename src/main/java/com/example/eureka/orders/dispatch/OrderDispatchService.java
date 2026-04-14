package com.example.eureka.orders.dispatch;

import java.io.IOException;

public interface OrderDispatchService {
    void sendOrder(Long orderId, boolean hideCompanyName) throws IOException;
    String confirmOrder(Long orderId);
}
