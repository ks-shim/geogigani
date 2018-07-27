package dwayne.shim.geogigani.common.code;

public enum AreaCode {

    SEOUL("서울", "1"),
    INCHEON("인천", "2"),
    DAEJEON("대전", "3"),
    DAEGU("대구", "4"),
    KWANGJU("광주", "5"),
    PUSAN("부산", "6"),
    ULSAN("울산", "7"),
    SEJONG("세종특별자치시", "8"),
    SEJONG_SHORT("세종", "8"),
    KYUNGGI("경기도", "31"),
    KYUNGGI_NAMBU("경기남부", "31"),
    KYUNGGI_BUKBU("경기북부", "31"),
    KANGWON("강원도", "32"),
    KANGWON_WEST("영서", "32"),
    KANGWON_EAST("영동", "32"),
    CHOONGBOOK("충청북도", "33"),
    CHOONGBOOK_SHORT("충북", "33"),
    CHOONGNAM("충청남도", "34"),
    CHOONGNAM_SHORT("충남", "34"),
    KYUNGBOOK("경상북도", "35"),
    KYUNGBOOK_SHORT("경북", "35"),
    KYUNGNAM("경상남도", "36"),
    KYUNGNAM_SHORT("경남", "36"),
    JEONBOOK("전라북도", "37"),
    JEONBOOK_SHORT("전북", "37"),
    JEONNAM("전라남도", "38"),
    JEONNAM_SHORT("전남", "38"),
    JEJU("제주도", "39"),
    JEJU_SHORT("제주", "39");

    private String code;
    private String label;

    private AreaCode(String _label,
                     String _code) {
        code = _code;
        label = _label;
    }

    public String label() {
        return label;
    }

    public String code() {
        return code;
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

    public static void isValid(String _code) {
        for(AreaCode areaCode : values())
            if(areaCode.code.equals(_code)) return;

        throw new RuntimeException("Code (" + _code + ") isn't a valid code ...");
    }
}
