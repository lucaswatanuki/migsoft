package migsoft.controller;

import migsoft.Exceptions.EstoqueException;
import migsoft.Exceptions.Resposta;
import migsoft.model.ProdutoEntity;
import migsoft.model.VendaEntity;
import migsoft.model.request.RelatorioRequest;
import migsoft.model.request.VendaRequest;
import migsoft.model.response.ItemProdutoResponse;
import migsoft.model.response.ProdutoResponse;
import migsoft.model.response.RelatorioProdutos;
import migsoft.model.response.VendaResponse;
import migsoft.service.EstoqueService;
import migsoft.service.ProdutoService;
import migsoft.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/venda")
public class VendaController {

    private final VendaService vendaService;
    private final EstoqueService estoqueService;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    public VendaController(VendaService vendaService, EstoqueService estoqueService) {
        this.vendaService = vendaService;
        this.estoqueService = estoqueService;
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> postVenda(@RequestBody VendaRequest venda) throws EstoqueException {
        try {
            estoqueService.subEstoque(estoqueService.findProdutoByNome(venda.getProduto()).getId(), venda.getQuantidade());
            VendaResponse response = vendaService.save(venda);
            return ResponseEntity.ok(response);
        } catch (EstoqueException e) {
            return new ResponseEntity<Object>(new Resposta(e.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> putVenda(@PathVariable("id") Integer id, @RequestBody VendaRequest venda) throws EstoqueException {
        ProdutoResponse produtoResponse = produtoService.findByNome(venda.getProduto());
        VendaResponse vendaResponse = vendaService.findById(id);
        try {
            estoqueService.subEstoque(produtoResponse.getId(), venda.getQuantidade() - vendaResponse.getQuantidade());
            VendaResponse response = vendaService.update(venda, id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | EstoqueException exception) {
            return ResponseEntity.badRequest().body("Estoque insuficiente");
        }
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public VendaResponse setStatus(@PathVariable("id") Integer id) {
        estoqueService.addEstoque(produtoService.findByNome(vendaService.findById(id).getProduto()).getId(), vendaService.findById(id).getQuantidade());
        return vendaService.cancel(id);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<VendaResponse> getAllVendas() {
        return vendaService.findAll();
    }



    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public VendaResponse getVendaById(@PathVariable Integer id) {
        return vendaService.findById(id);
    }


}
