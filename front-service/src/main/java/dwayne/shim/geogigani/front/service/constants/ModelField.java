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
    REGION_NAME("region_name"),
    LATITUDE("latitude"),
    LONGITUDE("longitude");

    private String label;
    private ModelField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
