package dwayne.shim.geogigani.common.indexing;

import org.apache.lucene.document.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum TravelDataIndexField {

    CONTENT_ID("contentid") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CONTENT_TYPE_ID("contenttypeid") { // 12 관광지, 14 문화시설, 15 축제공연행사, 25 여행코스, 28 레포츠, 32 숙박, 38 쇼핑, 39 음식점
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    TITLE("title") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    TITLE_KEYWORDS("titlekeywords") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    TITLE_SHORT("titleshort") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    ADDR1("addr1") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    ADDR2("addr2") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    ZIP_CODE("zipcode") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    TEL("tel") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    TEL_NAME("telname") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    OVERVIEW("overview") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    OVERVIEW_KEYWORDS("overviewkeywords") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    OVERVIEW_SHORT("overviewshort") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    FIRST_IMAGE("firstimage") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    FIRST_IMAGE2("firstimage2") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    HOMEPAGE("homepage") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    AREA_CODE("areacode") {
        // 1 서울, 2 인천, 3 대전, 4 대구, 5 광주, 6 부산, 7 울산, 8 세종특별자치시
        // 31 경기도, 32 강원도, 33 충청북도, 34 충청남도, 35 경상북도, 36 경상남도, 37 전라북도, 38 전라남도, 39 제주도
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    SIGUNGU_CODE("sigungucode") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    CREATED_TIME("createdtime") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    MODIFIED_TIME("modifiedtime") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    MAP_X("mapx") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Double.valueOf(value));
        }
    },
    MAP_Y("mapy") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Double.valueOf(value));
        }
    },
    LAT_LON_POINT("latlonpoint") {
        @Override
        public Field buildField(String value) {
            String[] latLon = value.split("\\s+");
            return new LatLonPoint(label(), Double.valueOf(latLon[0]), Double.valueOf(latLon[1]));
        }
    },
    LAT_LON_VALUE("latlonvalue") {
        @Override
        public Field buildField(String value) {
            String[] latLon = value.split("\\s+");
            return new LatLonDocValuesField(label(), Double.valueOf(latLon[0]), Double.valueOf(latLon[1]));
        }
    },
    MLEVEL("mlevel") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    CAT1("cat1") { // A01 자연, A02 인문, A03 레포츠, A04 쇼핑, A05 음식, B02 숙박, C01 추천코스
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CAT2("cat2") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CAT3("cat3") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ACCOM_COUNT("accomcount") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_BABY_CARRIAGE("chkbabycarriage") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_CREDIT_CARD("chkcreditcard") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_PET("chkpet") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EXP_AGE_RANGE("expagerange") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EXP_GUIDE("expguide") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER("infocenter") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_DATE("opendate") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING("parking") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    REST_DATE("restdate") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_SEASON("useseason") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_TIME("usetime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ACCOM_COUNT_CULTURE("accomcountculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_BABY_CARRIAGE_CULTURE("chkbabycarriageculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_CREDIT_CARD_CULTURE("chkcreditcardculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_PE_TCULTURE("chkpetculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    DISCOUNT_INFO("discountinfo") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_CULTURE("infocenterculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_CULTURE("parkingculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_FEE("parkingfee") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    REST_DATE_CULTURE("restdateculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_FEE("usefee") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_TIME_CULTURE("usetimeculture") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPEND_TIME("spendtime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    AGE_LIMIT("agelimit") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    BOOKING_PLACE("bookingplace") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    DISCOUNT_INFO_FESTIVAL("discountinfofestival") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EVENT_END_DATE("eventenddate") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EVEN_THOMEPAGE("eventhomepage") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EVENT_PLACE("eventplace") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EVEN_TSTART_DATE("eventstartdate") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PLACE_INFO("placeinfo") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PLAY_TIME("playtime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PROGRAM("program") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPEND_TIME_FESTIVAL("spendtimefestival") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPONSOR1("sponsor1") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPONSOR1_TEL("sponsor1tel") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPONSOR2("sponsor2") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPONSOR2_TEL("sponsor2tel") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SUB_EVENT("subevent") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_TIME_FESTIVAL("usetimefestival") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    DISTANCE("distance") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_TOUR_COURSE("infocentertourcourse") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SCHEDULE("schedule") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    TAKE_TIME("taketime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    THEME("theme") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ACCOM_COUNT_LEPORTS("accomcountleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_BABY_CARRIAGE_LEPORTS("chkbabycarriageleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_CREDIT_CARD_LEPORTS("chkcreditcardleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_PET_LEPORTS("chkpetleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    EXP_AGE_RANGE_LEPORTS("expagerangeleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_LEPORTS("infocenterleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_PERIOD("openperiod") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_FEE_LEPORTS("parkingfeeleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_LEPORTS("parkingleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    RESERVATION("reservation") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    REST_DATE_LEPORTS("restdateleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SCALE_LEPORTS("scaleleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_FEE_LEPORTS("usefeeleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    USE_TIME_LEPORTS("usetimeleports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ACCOM_COUNT_LODGING("accomcountlodging") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHECK_IN_TIME("checkintime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHECK_OUT_TIME("checkouttime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_COOKING("chkcooking") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    FOOD_PLACE("foodplace") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    HANOK("hanok") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_LODGING("infocenterlodging") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_LODGING("parkinglodging") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PICKUP("pickup") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ROOM_COUNT("roomcount") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    RESERVATION_LODGING("reservationlodging") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    RESERVATION_URL("reservationurl") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    ROOM_TYPE("roomtype") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SCALE_LODGING("scalelodging") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SUB_FACILITY("subfacility") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    BARBECUE("barbecue") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    BEAUTY("beauty") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    BEVERAGE("beverage") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    BICYCLE("bicycle") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CAMPFIRE("campfire") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    FITNESS("fitness") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    KARAOKE("karaoke") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PUBLIC_BATH("publicbath") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PUBLIC_PC("publicpc") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SAUNA("sauna") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SEMINAR("seminar") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SPORTS("sports") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_BABY_CARRIAGE_SHOPPING("chkbabycarriageshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_CREDIT_CARD_SHOPPING("chkcreditcardshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_PET_SHOPPING("chkpetshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CULTURE_CENTER("culturecenter") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    FAIRDAY("fairday") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_SHOPPING("infocentershopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_DATE_SHOPPING("opendateshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_TIME("opentime") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_SHOPPING("parkingshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    REST_DATE_SHOPPING("restdateshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    RESTROOM("restroom") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SALE_ITEM("saleitem") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SALE_ITEM_COST("saleitemcost") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SCALE_SHOPPING("scaleshopping") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SHOP_GUIDE("shopguide") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    CHK_CREDIT_CARD_FOOD("chkcreditcardfood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    DISCOUNT_INFO_FOOD("discountinfofood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    FIRST_MENU("firstmenu") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    INFO_CENTER_FOOD("infocenterfood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    KIDS_FACILITY("kidsfacility") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_DATE_FOOD("opendatefood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    OPEN_TIME_FOOD("opentimefood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PACKING("packing") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    PARKING_FOOD("parkingfood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    RESERVATION_FOOD("reservationfood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    REST_DATE_FOOD("restdatefood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SCALE_FOOD("scalefood") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SEAT("seat") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    SMOKING("smoking") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    TREAT_MENU("treatmenu") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    };

    public abstract Field buildField(String value);

    private String label;
    private TravelDataIndexField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }

    public static Map<String, TravelDataIndexField> map() {
        Map<String, TravelDataIndexField> map = new HashMap<>();
        for(TravelDataIndexField field : values())
            map.put(field.label, field);
        return Collections.unmodifiableMap(map);
    }
}
