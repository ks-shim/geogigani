package dwayne.shim.geogigani.front.service.constants;

public enum ModelField {

    DESTINATION_INFO("destination_info"),
    DESTINATION_ADD_INFO("destination_add_info"),
    DESTINATION_DETAIL_INFO("destination_detail_info"),
    DESTINATION_SIMILAR_INFO("destination_similar_info"),
    DESTINATION_IN5KM_INFO("destination_in5km_info"),
    DESTINATION_IN10KM_INFO("destination_in10km_info"),
    DESTINATION_INTEREST_INFO("destination_interest_info"),

    DUST_INFO("dust_info"),
    DUST_MAP_INFO("dust_map_info"),
    REGION_NAME("region_name"),
    LATITUDE("latitude"),
    LONGITUDE("longitude"),

    STATISTICS_MAP_VALUES("statistics_map_values"),
    STATISTICS_AREA_LABELS("statistics_area_labels"),
    STATISTICS_AREA_VALUES("statistics_area_values"),
    STATISTICS_CONTENT_TYPE_LABELS("statistics_content_type_labels"),
    STATISTICS_CONTENT_TYPE_VALUES("statistics_content_type_values"),
    STATISTICS_SESSION_COUNT_LABELS("statistics_session_count_labels"),
    STATISTICS_SESSION_COUNT_VALUES("statistics_session_count_values");

    private String label;
    private ModelField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
