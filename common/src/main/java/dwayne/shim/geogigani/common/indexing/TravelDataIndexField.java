package dwayne.shim.geogigani.common.indexing;

import org.apache.lucene.document.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum TravelDataIndexField {

    CONTENT_ID("contentid") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    CONTENT_TYPE_ID("contenttypeid") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
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
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    FIRST_IMAGE2("firstimage2") {
        @Override
        public Field buildField(String value) {
            return new TextField(label(), value, Field.Store.YES);
        }
    },
    AREA_CODE("areacode") {
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
    MLEVEL("mlevel") {
        @Override
        public Field buildField(String value) {
            return new StoredField(label(), Long.valueOf(value));
        }
    },
    CAT1("cat1") {
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
    IN_5KM("in5km") {
        @Override
        public Field buildField(String value) {
            return new StringField(label(), value, Field.Store.YES);
        }
    },
    IN_10KM("in10km") {
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
