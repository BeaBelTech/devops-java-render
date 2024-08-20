package com.example.produto.services;

import com.example.produto.Model.Produto;
import com.example.produto.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProdutoServices {
    private final ProdutoRepository produtoRepository;
    public ProdutoServices(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarTodosOsProdutos() {
        return produtoRepository.findAll();
    }
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarProdutoId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
    public Produto excluirProduto(Long id) {
        Optional<Produto> prod = produtoRepository.findById(id);
        if (prod.isPresent()) {
            produtoRepository.deleteById(id);
            return prod.get();
        }
        return null;
    }

    public List<Produto> buscarPorNome(String nome, double preco) {
       return produtoRepository.findByNomeLikeIgnoreCaseAndPrecoLessThan(nome, preco);
    }

    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            produtoRepository.save(produto);
            return produto;
        }
        return null;
    }

    public Produto atualizarProdutoParcialmente (Long id, Map<String, Object> updates) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);

        if(produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();

            // Atualiza apenas os campos que foram passados no corpo da requisição
            if (updates.containsKey("nome")) {
                produto.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("descricao")) {
                produto.setDescricao((String) updates.get("descricao"));
            }
            if (updates.containsKey("preco")) {
                try {
                    produto.setPreco((Double) updates.get("preco"));
                } catch (ClassCastException ce) {
                    String precoString = updates.get("preco").toString();
                    produto.setPreco(Double.parseDouble(precoString));
                }
            }
            if (updates.containsKey("quantidadeEstoque")) {
                produto.setQuantidadeEstoque((Integer) updates.get("quantidadeEstoque"));
            }
            produtoRepository.save(produto);
            return produto;
        } else {
            return null;
        }
    }


}
