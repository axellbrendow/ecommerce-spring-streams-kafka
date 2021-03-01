package com.axell.ecommerce.checkout.service;

import com.axell.ecommerce.checkout.controller.checkout.CheckoutRequest;
import com.axell.ecommerce.checkout.entity.Checkout;

import java.util.Optional;

public interface ICheckoutService {
    Optional<Checkout> create(CheckoutRequest checkoutRequest);
    Optional<Checkout> updateStatus(String checkoutCode, Checkout.Status status);
}
