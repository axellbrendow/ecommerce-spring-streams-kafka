package com.axell.ecommerce.payment.service;

import com.axell.ecommerce.payment.entity.Payment;
import com.axell.ecommerce.checkout.event.CheckoutCreatedEvent;
import com.axell.ecommerce.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public Optional<Payment> create(CheckoutCreatedEvent checkoutCreatedEvent) {
        var payment = Payment.builder()
                .checkoutCode(checkoutCreatedEvent.getCheckoutCode().toString())
                .code(UUID.randomUUID().toString())
                .build();
        paymentRepository.save(payment);
        return Optional.of(payment);
    }
}
