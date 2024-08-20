package com.example.produto.Model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Números gerados automaticamente
    @Schema(description = "ID único do produto", example = "1234")
    private long id;

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    @Schema(description = "Nome do produto", example = "Hambúrguer de frango")
    private String nome;
    @Schema(description = "Descrição detalhada do produto", example = "Hambúrguer de frango congelago de 500g")
    private String descricao;
        @NotNull(message = "O preço não pode ser nulo")
        @Min(value = 0, message = "O preço deve ser pelo menos 0")
        @Schema(description = "Preço do produto", example = "1999.99")
        private double preco;

    @NotNull(message = "A quantidade não pode ser nula")
    @Min(value = 0 , message = "A quantidade deve ser pelo menos 0")
    @Schema(description = "Quantidade disponível em estoque", example = "50")
    @Column(name = "quantidadeestoque")
    private int quantidadeEstoque;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeestoque) {
        this.quantidadeEstoque = quantidadeestoque;
    }

    public Produto() {
    }

    public Produto(long id, String nome, String descricao, double preco, int quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", quantidadeestoque=" + quantidadeEstoque +
                '}';
    }
}
