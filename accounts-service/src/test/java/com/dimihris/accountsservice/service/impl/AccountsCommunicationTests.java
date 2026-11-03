package com.dimihris.accountsservice.service.impl;

import com.dimihris.accountsservice.dto.AccountsMsgDto;
import com.dimihris.accountsservice.dto.CustomerDto;
import com.dimihris.accountsservice.entity.Accounts;
import com.dimihris.accountsservice.entity.Customer;
import com.dimihris.accountsservice.exception.CustomerAlreadyExistsException;
import com.dimihris.accountsservice.repository.AccountsRepository;
import com.dimihris.accountsservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountsCommunicationTests {
    private final CustomerRepository customers = mock(CustomerRepository.class);
    private final AccountsRepository accounts = mock(AccountsRepository.class);
    private final StreamBridge bridge = mock(StreamBridge.class);
    private final AccountsServiceImpl service = new AccountsServiceImpl(customers, accounts, bridge);
    private final CustomerDto request = new CustomerDto("Test Learner", "learner@example.com", "0888123456");

    @Test
    void creatingAnAccountPublishesItsSavedAccountNumberAndContactDetails() {
        when(customers.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.empty());
        when(customers.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setCustomerId(1L);
            return customer;
        });
        when(bridge.send(eq("sendCommunication-out-0"), any())).thenReturn(true);

        service.createAccount(request);

        var savedAccount = ArgumentCaptor.forClass(Accounts.class);
        var event = ArgumentCaptor.forClass(AccountsMsgDto.class);
        var sequence = inOrder(accounts, bridge);
        sequence.verify(accounts).save(savedAccount.capture());
        sequence.verify(bridge).send(eq("sendCommunication-out-0"), event.capture());
        assertEquals(savedAccount.getValue().getAccountNumber(), event.getValue().accountNumber());
        assertEquals(request.getName(), event.getValue().name());
        assertEquals(request.getEmail(), event.getValue().email());
        assertEquals(request.getMobileNumber(), event.getValue().mobileNumber());
    }

    @Test
    void duplicateCustomersDoNotPublishAnEvent() {
        when(customers.findByMobileNumber(request.getMobileNumber())).thenReturn(Optional.of(new Customer()));
        assertThrows(CustomerAlreadyExistsException.class, () -> service.createAccount(request));
        verifyNoInteractions(accounts, bridge);
    }
}
