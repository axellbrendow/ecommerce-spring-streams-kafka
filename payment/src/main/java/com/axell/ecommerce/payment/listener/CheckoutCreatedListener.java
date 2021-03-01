package com.axell.ecommerce.payment.listener;

import com.axell.ecommerce.checkout.event.CheckoutCreatedEvent;
import com.axell.ecommerce.payment.event.PaymentCreatedEvent;
import com.axell.ecommerce.payment.service.PaymentService;
import com.axell.ecommerce.payment.streaming.CheckoutProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckoutCreatedListener {
    private final CheckoutProcessor checkoutProcessor;
    private final PaymentService paymentService;

    @StreamListener(CheckoutProcessor.INPUT)
    public void handler(GenericData.Record event) {
        // Aqui processaria pagamento em algum gateway

        // Recria o evento
        final CheckoutCreatedEvent checkoutCreatedEvent = CheckoutCreatedEvent.newBuilder()
                .setCheckoutCode(event.get("checkoutCode").toString())
                .setStatus(event.get("status").toString())
                .build();
        log.info("checkoutCreatedEvent={}", checkoutCreatedEvent);

        // Salvar os dados de pagamento
        var payment = paymentService.create(checkoutCreatedEvent).orElseThrow();

        // enviar o evento do pagamento processado
        final PaymentCreatedEvent paymentCreatedEvent = PaymentCreatedEvent.newBuilder()
                .setCheckoutCode(payment.getCheckoutCode())
                .setPaymentCode(payment.getCode())
                .build();
        checkoutProcessor.output().send(MessageBuilder.withPayload(paymentCreatedEvent).build());
    }
}
