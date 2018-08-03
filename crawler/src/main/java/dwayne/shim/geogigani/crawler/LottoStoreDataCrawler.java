package dwayne.shim.geogigani.crawler;

import java.util.List;

public class LottoStoreDataCrawler {

    enum COLUMN {
        ADDR1("addr1"),
        COUNT_1ST("1stcount"),
        COUNT_2ND("2ndcount"),
        AREA_CODE("areacode"),
        CONTENT_ID("contentid"),
        CONTENT_TYPE_ID("contenttypeid"),
        CREATED_TIME("createdtime"),
        FIRST_IMAGE("firstimage"),
        MAP_X("mapx"),
        MAP_Y("mapy"),
        TITLE("title"),
        OVERVIEW("overview");

        private final String label;
        private COLUMN(String _label) {
            label = _label;
        }

        public String labe() {
            return label;
        }
    }

    public void execute(String storeInfoText1st,
                        String storeInfoText2nd,
                        String urlL645,
                        String urlLatLong) throws Exception {

        // 1. read 1st store info

    }



    public static void main(String[] args) throws Exception {

        if(args.length < 2) {
            System.err.println("Usage : java LottoStoreDataCralwer <1st_store_info_text_path> <2nd_store_info_text_path>");
            System.exit(0);
        }

        final String storeInfoText1st = args[0].trim();
        final String storeInfoText2nd = args[1].trim();
        final String urlL645 = "http://www.nlotto.co.kr/store.do?method=topStoreRank&pageGubun=L645&rank=";
        final String urlLatLong = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&address=";

    }

    private static class LottoStore {

        String id;
        String name;
        int count1st;
        int count2nd;
        String address;

    }
}
