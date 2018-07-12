package dwayne.shim.geogigani.searching;

import lombok.Data;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

@Data
public class SearchQueryInfo {

    private final Query query;
    private final BooleanClause.Occur occur;

    public SearchQueryInfo(Query query,
                           BooleanClause.Occur occur) {
        this.query = query;
        this.occur = occur;
    }
}
