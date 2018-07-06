package dwayne.shim.geogigani.front.service.constants;

public enum ModelField {

    DESTINATION_INFO("destination_info"),
    DESTINATION_ADD_INFO("destination_add_info"),
    DESTINATION_DETAIL_INFO("destination_detail_info");

    private String label;
    private ModelField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
