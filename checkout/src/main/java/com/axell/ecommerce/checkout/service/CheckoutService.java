package com.axell.ecommerce.checkout.service;

import com.axell.ecommerce.checkout.controller.checkout.CheckoutRequest;
import com.axell.ecommerce.checkout.entity.Checkout;
import com.axell.ecommerce.checkout.entity.CheckoutItem;
import com.axell.ecommerce.checkout.entity.Shipping;
import com.axell.ecommerce.checkout.event.CheckoutCreatedEvent;
import com.axell.ecommerce.checkout.repository.CheckoutRepository;
import com.axell.ecommerce.checkout.streaming.CheckoutCreatedSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService implements ICheckoutService {
    private final CheckoutRepository checkoutRepository;
    private final CheckoutCreatedSource checkoutCreatedSource;

    @Override
    public Optional<Checkout> create(CheckoutRequest checkoutRequest) {
        log.info("M=create, checkoutRequest={}", checkoutRequest);

        final var checkout = Checkout.builder()
                .code(UUID.randomUUID().toString())
                .status(Checkout.Status.CREATED)
                .saveAddress(checkoutRequest.getSaveAddress())
                .saveInformation(checkoutRequest.getSaveInfo())
                .shipping(
                    Shipping.builder()
                    .address(checkoutRequest.getAddress())
                    .complement(checkoutRequest.getComplement())
                    .country(checkoutRequest.getCountry())
                    .state(checkoutRequest.getState())
                    .cep(checkoutRequest.getCep())
                    .build()
                )
                .build();

        checkout.setItems(checkoutRequest.getProducts()
                .stream()
                .map(product -> CheckoutItem.builder()
                        .checkout(checkout)
                        .product(product)
                        .build())
                .collect(Collectors.toList()));

        var savedCheckout = checkoutRepository.save(checkout);
        final CheckoutCreatedEvent checkoutCreatedEvent = CheckoutCreatedEvent.newBuilder()
                .setCheckoutCode(savedCheckout.getCode())
                .setStatus(savedCheckout.getStatus().name())
                .build();

        checkoutCreatedSource.output().send(MessageBuilder.withPayload(checkoutCreatedEvent).build());

        return Optional.of(savedCheckout);
    }

    @Override
    public Optional<Checkout> updateStatus(String checkoutCode, Checkout.Status status) {
        final Checkout checkout = checkoutRepository
                .findByCode(checkoutCode)
                .orElse(Checkout.builder().build());
        checkout.setStatus(status);
        return Optional.of(checkoutRepository.save(checkout));
    }
}
