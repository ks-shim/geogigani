package dwayne.shim.geogigani.common.code;

public enum AreaCode {

    SEOUL("서울", "1", "kr-so"),
    INCHEON("인천", "2", "kr-in"),
    DAEJEON("대전", "3", "kr-tj"),
    DAEGU("대구", "4", "kr-tg"),
    KWANGJU("광주", "5", "kr-kj"),
    PUSAN("부산", "6", "kr-pu"),
    ULSAN("울산", "7", "kr-ul"),
    SEJONG("세종특별자치시", "8", "kr-sj"),
    SEJONG_SHORT("세종", "8", "kr-sj"),
    KYUNGGI("경기도", "31", "kr-kg"),
    KYUNGGI_NAMBU("경기남부", "31", "kr-kg"),
    KYUNGGI_BUKBU("경기북부", "31", "kr-kg"),
    KANGWON("강원도", "32", "kr-kw"),
    KANGWON_WEST("영서", "32", "kr-kw"),
    KANGWON_EAST("영동", "32", "kr-kw"),
    CHOONGBOOK("충청북도", "33", "kr-gb"),
    CHOONGBOOK_SHORT("충북", "33", "kr-gb"),
    CHOONGNAM("충청남도", "34", "kr-gn"),
    CHOONGNAM_SHORT("충남", "34", "kr-gn"),
    KYUNGBOOK("경상북도", "35", "kr-2688"),
    KYUNGBOOK_SHORT("경북", "35", "kr-2688"),
    KYUNGNAM("경상남도", "36", "kr-kn"),
    KYUNGNAM_SHORT("경남", "36", "kr-kn"),
    JEONBOOK("전라북도", "37", "kr-cb"),
    JEONBOOK_SHORT("전북", "37", "kr-cb"),
    JEONNAM("전라남도", "38", "kr-2685"),
    JEONNAM_SHORT("전남", "38", "kr-2685"),
    JEJU("제주도", "39", "kr-cj"),
    JEJU_SHORT("제주", "39", "kr-cj");

    private String code;
    private String label;
    private String code3;
    private AreaCode(String _label,
                     String _code,
                     String _code3) {
        code = _code;
        label = _label;
        code3 = _code3;
    }

    public String label() {
        return label;
    }

    public String code() {
        return code;
    }

    public String code3() {
        return code3;
    }

    public static AreaCode getAreaCode(String _code) {
        for(AreaCode areaCode : values())
            if(areaCode.code.equals(_code)) return areaCode;

        throw new RuntimeException("Code (" + _code + ") doesn't exist ...");
    }

    public static AreaCode getAreaCodeByLabel(String _label) {
        for(AreaCode areaCode : values())
            if(areaCode.label.equals(_label)) return areaCode;

        throw new RuntimeException("Label (" + _label + ") doesn't exist ...");
    }

    public static AreaCode getBaseAreaCodeByLabel(String _label) {
        for(AreaCode areaCode : values()) {
            if (!areaCode.label.equals(_label)) continue;

            if(areaCode == KYUNGGI_NAMBU || areaCode == KYUNGGI_BUKBU) return KYUNGGI;
            else if(areaCode == KANGWON_WEST || areaCode == KANGWON_EAST) return KANGWON;
            else return areaCode;
        }

        throw new RuntimeException("Label (" + _label + ") doesn't exist ...");
    }

    public static void isValid(String _code) {
        if(_code == null || _code.trim().isEmpty()) throw new NullPointerException();

        for(AreaCode areaCode : values())
            if(areaCode.code.equals(_code)) return;

        throw new RuntimeException("Code (" + _code + ") isn't a valid code ...");
    }
}
