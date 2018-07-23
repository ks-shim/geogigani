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
    KYUNGGI("경기도", "31"),
    KANGWON("강원도", "32"),
    CHOONGBOOK("충청북도", "33"),
    CHOONGNAM("충청남도", "34"),
    KYUNGBOOK("경상북도", "35"),
    KYUNGNAM("경상남도", "36"),
    JEONBOOK("전라북도", "37"),
    JEONNAM("전라남도", "38"),
    JEJU("제주도", "39");

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
}
