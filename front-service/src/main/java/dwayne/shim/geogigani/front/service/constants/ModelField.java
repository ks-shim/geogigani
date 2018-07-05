package dwayne.shim.geogigani.front.service.constants;

public enum ModelField {

    DESTINATION_INFO("destination_info"),
    DESTINATION_ADD_INFO("detination_add_info");

    private String label;
    private ModelField(String _label) {
        label = _label;
    }

    public String label() {
        return label;
    }
}
