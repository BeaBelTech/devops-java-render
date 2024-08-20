package com.example.produto.Repository;

import com.example.produto.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//Essa interface é criada para a injeção de dependência, especificando onde vamos aplicar
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Query("DELETE FROM Produto e WHERE e.id = ?1")
    void deleteById(Long id);

    List<Produto> findByNomeLikeIgnoreCaseAndPrecoLessThan(String nome, Double preco);
}
