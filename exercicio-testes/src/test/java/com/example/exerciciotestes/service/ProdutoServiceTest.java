package com.example.exerciciotestes.service;

import com.example.exerciciotestes.controller.request.ProdutoRequest;
import com.example.exerciciotestes.model.Produto;
import com.example.exerciciotestes.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    class ProdutoServiceTest {

        @Mock
        private ProdutoRepository produtoRepository;

        @InjectMocks
        private ProdutoService produtoService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void buscaTodosProdutos() {
            // Arrange
            List<Produto> produtosMock = new ArrayList<>();
            produtosMock.add(new Produto(1L, "Produto 1", 10.0));
            produtosMock.add(new Produto(2L, "Produto 2", 20.0));
            when(produtoRepository.findAll()).thenReturn(produtosMock);

            // Act
            List<Produto> produtos = produtoService.buscaTodosProdutos();

            // Assert
            assertEquals(2, produtos.size());
            assertEquals("Produto 1", produtos.get(0).getNomeProduto());
            assertEquals("Produto 2", produtos.get(1).getNomeProduto());
            verify(produtoRepository).findAll();
        }

        @Test
        void buscaProdutoPorId() {
            // Arrange
            Produto produtoMock = new Produto(1L, "Produto 1", 10.0);
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoMock));

            // Act
            Produto produto = produtoService.buscaProdutoPorId(1L);

            // Assert
            assertNotNull(produto);
            assertEquals("Produto 1", produto.getNomeProduto());
            verify(produtoRepository).findById(1L);
        }

        @Test
        void salvarProduto() {
            // Arrange
            ProdutoRequest produtoRequest = new ProdutoRequest("Produto 1", 10.0);
            when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Produto produtoSalvo = produtoService.salvarProduto(produtoRequest);

            // Assert
            assertNotNull(produtoSalvo);
            assertEquals("Produto 1", produtoSalvo.getNomeProduto());
            assertEquals(10.0, produtoSalvo.getValorProduto());
            verify(produtoRepository).save(any(Produto.class));
        }

        @Test
        void atualizarProduto() {
            // Arrange
            Long produtoId = 1L;
            ProdutoRequest produtoRequest = new ProdutoRequest("Produto Atualizado", 20.0);
            Produto produtoAtualMock = new Produto(produtoId, "Produto Antigo", 10.0);

            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoAtualMock));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Produto produtoAtualizado = produtoService.atualizarProduto(produtoId, produtoRequest);

            // Assert
            assertNotNull(produtoAtualizado);
            assertEquals(produtoId, produtoAtualizado.getId());
            assertEquals("Produto Atualizado", produtoAtualizado.getNomeProduto());
            assertEquals(20.0, produtoAtualizado.getValorProduto());
            verify(produtoRepository).findById(produtoId);
            verify(produtoRepository).save(any(Produto.class));
        }

        @Test
        void detelaProdutoPorId() {
            // Arrange
            Long produtoId = 1L;

            // Act
            produtoService.detelaProdutoPorId(produtoId);

            // Assert
            verify(produtoRepository).deleteById(produtoId);
        }
}
