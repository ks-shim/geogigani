package dwayne.shim.geogigani.common.indexing;

public enum TravelDataIndexField {

    CONTENT_ID("contentid"),
    CONTENT_TYPE_ID("contenttypeid"),
    TITLE("title"),
    TITLE_KEYWORDS("titlekeywords"),
    ADDR1("addr1"),
    ADDR2("addr2"),
    ZIP_CODE("zipcode"),
    TEL("tel"),
    TEL_NAME("telname"),
    OVERVIEW("overview"),
    OVERVIEW_KEYWORDS("overviewkeywords"),
    FIRST_IMAGE("firstimage"),
    FIRST_IMAGE2("firstimage2"),
    AREA_CODE("areacode"),
    SIGUNGU_CODE("sigungucode"),
    CREATED_TIME("createdtime"),
    MODIFIED_TIME("modifiedtime"),
    MAP_X("mapx"),
    MAP_Y("mapy"),
    MLEVEL("mlevel"),
    CAT1("cat1"),
    CAT2("cat2"),
    CAT3("cat3");

    private String label;
    private TravelDataIndexField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
