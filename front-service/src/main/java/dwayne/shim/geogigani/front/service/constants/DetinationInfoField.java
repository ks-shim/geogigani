package dwayne.shim.geogigani.front.service.constants;

public enum DetinationInfoField {

    IMPRESSION_COUNT("impressioncount"),
    CLICK_COUNT("clickcount"),
    SCORE("score"),
    LAST_ACCESS_TIME("lastaccesstime"),
    CONTENT_ID("contentid"),
    CONTENT_TYPE_ID("contenttypeid"),
    TITLE("title"),
    TITLE_KEYWORDS("titlekeywords"),
    TITLE_SHORT("titleshort"),
    ADDR1("addr1"),
    ADDR2("addr2"),
    ZIP_CODE("zipcode"),
    TEL("tel"),
    TEL_NAME("telname"),
    OVERVIEW("overview"),
    OVERVIEW_KEYWORDS("overviewkeywords"),
    OVERVIEW_SHORT("overviewshort"),
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
    private DetinationInfoField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
