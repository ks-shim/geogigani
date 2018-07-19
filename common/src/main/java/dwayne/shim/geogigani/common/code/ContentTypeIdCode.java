package dwayne.shim.geogigani.common.code;

public enum ContentTypeIdCode {

    TOUR_SPOT("관광지", "12"),
    CULTURE("문화시설", "14"),
    FESTIVAL("축제공연행사", "15"),
    COURSE("여행코스", "25"),
    LEPORTS("레포츠", "28"),
    LODGING("숙박", "32"),
    SHOPPING("쇼핑", "38"),
    FOOD("음식점", "39");

    private String label;
    private String code;
    private ContentTypeIdCode(String _label, String _code) {
        label = _label;
        code = _code;
    }

    public String label() {
        return label;
    }

    public String code() {
        return code;
    }

    public static ContentTypeIdCode getTypeIdCode(String _code) {
        for(ContentTypeIdCode typeIdCode : values())
            if(typeIdCode.code.equals(_code)) return typeIdCode;

        throw new RuntimeException("Code (" + _code + ") doesn't exist ...");
    }
}
