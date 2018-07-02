package dwayne.shim.geogigani.searching;

public enum SearchResultItem {

    TOTAL_HITS("total_hits"),
    RESULT_LIST("result_list");

    private String label;
    private SearchResultItem(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
