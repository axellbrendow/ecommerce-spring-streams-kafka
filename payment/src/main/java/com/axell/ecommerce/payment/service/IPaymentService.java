package com.axell.ecommerce.payment.service;

import com.axell.ecommerce.payment.entity.Payment;
import com.axell.ecommerce.checkout.event.CheckoutCreatedEvent;

import java.util.Optional;

public interface IPaymentService {
    Optional<Payment> create(CheckoutCreatedEvent checkoutCreatedEvent);
}
