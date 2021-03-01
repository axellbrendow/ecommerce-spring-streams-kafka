package com.axell.ecommerce.checkout.listener;

import com.axell.ecommerce.checkout.entity.Checkout;
import com.axell.ecommerce.checkout.repository.CheckoutRepository;
import com.axell.ecommerce.checkout.service.CheckoutService;
import com.axell.ecommerce.payment.event.PaymentCreatedEvent;
import com.axell.ecommerce.checkout.streaming.PaymentPaidSink;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPaidListener {
    private final CheckoutService checkoutService;

    @StreamListener(PaymentPaidSink.INPUT)
    public void handler(PaymentCreatedEvent event) {
        checkoutService.updateStatus(event.getCheckoutCode().toString(), Checkout.Status.APPROVED);
    }
}
