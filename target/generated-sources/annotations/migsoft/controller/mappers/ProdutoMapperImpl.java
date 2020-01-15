package migsoft.controller.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import migsoft.controller.response.ProdutoResponse;
import migsoft.model.ProdutoEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-15T17:22:44-0200",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_212 (Oracle Corporation)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public ProdutoResponse toProdutoResponse(ProdutoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProdutoResponse produtoResponse = new ProdutoResponse();

        produtoResponse.setQtdEstoqueProduto( entity.getQtdEstoque() );
        produtoResponse.setIdProduto( entity.getId() );
        if ( entity.getPreco() != null ) {
            produtoResponse.setPrecoProduto( entity.getPreco() );
        }
        produtoResponse.setNomeProduto( entity.getNome() );

        return produtoResponse;
    }

    @Override
    public List<ProdutoResponse> toListaProdutoResponse(List<ProdutoEntity> lista) {
        if ( lista == null ) {
            return null;
        }

        List<ProdutoResponse> list = new ArrayList<ProdutoResponse>( lista.size() );
        for ( ProdutoEntity produtoEntity : lista ) {
            list.add( toProdutoResponse( produtoEntity ) );
        }

        return list;
    }
}
