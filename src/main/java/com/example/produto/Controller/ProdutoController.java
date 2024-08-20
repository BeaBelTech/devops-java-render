package com.example.produto.Controller;

import com.example.produto.Model.Produto;
import com.example.produto.services.ProdutoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController //Abrindo para receber uma requisição DOS CLIENTES (Controller), devolvendo um JSON (Rest)
@RequestMapping("/api/produtos") //Qual é a rota que a API vai receber; URL base
public class ProdutoController {
    private ProdutoServices produtoServices; //Variável é uma const de JS. AUTOWIRED não funciona com constante, tem que ser uma variável
    private final Validator validator;

    @Autowired
    public ProdutoController(ProdutoServices produtoService, Validator validator) {
        this.produtoServices = produtoService;
        this.validator = validator;
    } //Criando um objeto que o container vai gerenciar (Bean)

    @GetMapping("/selecionar")
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista de todos os produtos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public List<Produto> listarProdutos() {
        return produtoServices.buscarTodosOsProdutos();
    }

    @PostMapping("/inserir") //Utilizaremos o Post como ENDPOINT
    public ResponseEntity<?> inserirProduto(@Valid @RequestBody Produto produto) { //A entidade que irá responder será em formato de SPRING. Transforma o JSON em Objeto Produto
        produtoServices.salvarProduto(produto);
        return ResponseEntity.ok("Produto inserido com sucesso");
    }
//@PostMapping("/inserir") //Utilizaremos o Post como ENDPOINT
//public ResponseEntity<Map<String, String>> inserirProduto(@Valid @RequestBody Produto produto, BindingResult result) {
//    if (result.hasErrors()) {
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : result.getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//        return ResponseEntity.badRequest().body(errors);
//    }
//    // Lógica para salvar o produto (não incluída neste exemplo)
//    return ResponseEntity.ok().build();
//}
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handler(MethodArgumentNotValidException manve) {
//        return manve.getBindingResult().getFieldError().getDefaultMessage();
//    }

//    @DeleteMapping("/excluir/{id}")
//    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
//        // Verifica se o produto existe
//        if (!produtoRepository.existsById(id)) {
//            // Retorna uma resposta de não encontrado se o produto não existir
//            return ResponseEntity.notFound().build();
//        }
//
//        // Se existir, exclui o produto
//        produtoRepository.deleteById(id);
//        return ResponseEntity.ok("Produto excluído com sucesso");
//    }

    @DeleteMapping("/excluir/{id}")
    @Operation(summary = "Excluir produto por ID", description = "Remove um produto do sistema pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Produto.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        Produto produtoExistente = produtoServices.excluirProduto(id);
        if (produtoExistente != null) {
            return ResponseEntity.ok("Produto removido com sucesso");
        }
        return ResponseEntity.status(404).body("Produto não encontrado");
    }

//        @DeleteMapping("/excluir/{id}")
//    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
//        return produtoRepository.findById(id)
//                .map(produto -> {
//                    produtoRepository.delete(produto);
//                    return ResponseEntity.ok("Produto excluído com sucesso");
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        if (produtoAtualizado.getPreco() > 0 && produtoAtualizado.getQuantidadeEstoque() > 0 && !produtoAtualizado.getNome().strip().equals("") && !produtoAtualizado.getDescricao().strip().equals("")) {

            Produto produtoExistente = produtoServices.atualizarProduto(id, produtoAtualizado);
            if (produtoExistente != null) {
                return ResponseEntity.ok("Produto atualizado com sucesso");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(400).body("Confira os valores digitados e tente novamente!");
        }
    }
//    @PatchMapping("/atualizarParcial/{id}")
//    public ResponseEntity<String> atualizarProdutoParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//
//        try {
//            Produto produtoExistente = produtoServices.atualizarProdutoParcialmente(id, updates);
//            if (produtoExistente != null) {
//                return ResponseEntity.ok("Produto atualizado parcialmente com sucesso");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto com ID " + id + " não encontrado");
//            }
//        } catch (ConstraintViolationException e) {
//            Map<String, String> errors = new HashMap<>();
//            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
//            }
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//    }


    @PatchMapping("/atualizarParcial/{id}")
    public ResponseEntity<?> atualizarProdutoParcial(@Valid @PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Produto produto = produtoServices.buscarProdutoId(id);
            if (updates.containsKey("nome")) {
                produto.setNome((String) updates.get("nome"));
            }
            if (updates.containsKey("descricao")) {
                produto.setDescricao((String) updates.get("descricao"));
            }
            if (updates.containsKey("preco")) {
                produto.setPreco(Double.parseDouble(String.valueOf(updates.get("preco"))));
//                try {
//                    produto.setPreco((Double) updates.get("preco"));
//                } catch (ClassCastException cne) {
//                    int precoInt = (Integer) updates.get("preco");
//                    produto.setPreco(Double.parseDouble(String.valueOf(precoInt)));
//                }
            }

            if (updates.containsKey("quantidadeEstoque")) {
                produto.setQuantidadeEstoque((Integer) updates.get("quantidadeEstoque"));
            }

            //validando os dados
            DataBinder binder = new DataBinder(produto); //vincula o databinder ao produto
            binder.setValidator(validator); //configura o validador ao DataBinder
            binder.validate(); //execita o validaddor no objeto vinculado
            BindingResult resultado = binder.getBindingResult();

            if (resultado.hasErrors()) {
                Map erros = validarProduto(resultado);
                return ResponseEntity.badRequest().body(erros);
            }
            Produto produtoSalvo = produtoServices.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo);
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }


    }

    public Map<String, String> validarProduto(BindingResult resultado) {
        Map<String, String> erros = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        return erros;
    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome, double preco) {
        List<Produto> listaProdutos = produtoServices.buscarPorNome(nome, preco);
        if (!listaProdutos.isEmpty()) {
            return ResponseEntity.ok(listaProdutos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }
}
