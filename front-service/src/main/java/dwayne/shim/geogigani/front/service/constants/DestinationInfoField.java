package dwayne.shim.geogigani.front.service.constants;

public enum DestinationInfoField {

    IMPRESSION_COUNT("impressioncount"),
    CLICK_COUNT("clickcount"),
    SCORE("score"),
    SCORE_SNAPSHOT("scoresnapshot"),
    LAST_ACCESS_TIME("lastaccesstime"),
    CONTENT_ID("contentid"),
    CONTENT_TYPE_ID("contenttypeid"),
    CONTENT_TYPE_LABEL("contenttypelabel"),
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
    AREA_LABEL("arealabel"),
    SIGUNGU_CODE("sigungucode"),
    CREATED_TIME("createdtime"),
    MODIFIED_TIME("modifiedtime"),
    MAP_X("mapx"),
    MAP_Y("mapy"),
    MLEVEL("mlevel"),
    CAT1("cat1"),
    CAT2("cat2"),
    CAT3("cat3"),
    IN_5KM("in5km"),
    IN_10KM("in10km");

    private String label;
    private DestinationInfoField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
