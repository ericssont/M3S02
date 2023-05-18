package com.example.exerciciotestes.service;

import com.example.exerciciotestes.controller.request.ClienteRequest;
import com.example.exerciciotestes.model.Cliente;
import com.example.exerciciotestes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {


    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscaTodosClientes() {

        when(clienteRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Cliente> clientes = clienteService.buscaTodosClientes();

        // Assert
        assertTrue(clientes.isEmpty());
        verify(clienteRepository).findAll();
    }

    @Test
    void buscaClientePorId() {

        Cliente clienteMock = new Cliente(1L, "João", 100.0);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));

        // Act
        Cliente cliente = clienteService.buscaClientePorId(1L);

        // Assert
        assertNotNull(cliente);
        assertEquals("João", cliente.getNomeCliente());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void salvarCliente() {

        ClienteRequest clienteRequest = new ClienteRequest("João", 100.0);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cliente clienteSalvo = clienteService.salvarCliente(clienteRequest);

        // Assert
        assertNotNull(clienteSalvo);
        assertEquals(100.0, clienteSalvo.getSaldoCliente());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void atualizarCliente() {

        Long clienteId = 1L;
        ClienteRequest clienteRequest = new ClienteRequest("João", 200.0);
        Cliente clienteAtualMock = new Cliente(clienteId, "Antônio", 100.0);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteAtualMock));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cliente clienteAtualizado = clienteService.atualizarCliente(clienteId, clienteRequest);

        // Assert
        assertNotNull(clienteAtualizado);
        assertEquals(clienteId, clienteAtualizado.getId());
        assertEquals("João", clienteAtualizado.getNomeCliente());
        assertEquals(200.0, clienteAtualizado.getSaldoCliente());
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void detelaClientePorId_deveLancarExcecao_quandoClienteNaoExiste() {

        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> clienteService.detelaClientePorId(clienteId));
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository, never()).deleteById(clienteId);
    }


}

