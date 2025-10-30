package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class Medication {

    public enum Unit {
        VIEN("Viên"),
        ML("Ml"),
        MG("Mg"),
        GOI("Gói"),
        CHAI("Chai"),
        TUYP("Tuýp"),
        ONG_TIEM("Ống tiêm");

        private String displayName;

        Unit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int medication_id;
    private String name;
    private String generic_name;
    private String description;
    private String side_effects;
    private String contraindications;
    private String interactions;
    private String manufacturer;
    private Unit unit;
    private boolean requires_prescription;
    private String barcode;
    private LocalDateTime created_at;

    public Medication() {
        this.created_at = LocalDateTime.now();
        this.unit = Unit.VIEN; 
        this.requires_prescription = true; 
    }

    public Medication(String name, String generic_name, String description, String side_effects,
                      String contraindications, String interactions, String manufacturer,
                      Unit unit, boolean requires_prescription, String barcode) {
        this(); 
        this.name = name;
        this.generic_name = generic_name;
        this.description = description;
        this.side_effects = side_effects;
        this.contraindications = contraindications;
        this.interactions = interactions;
        this.manufacturer = manufacturer;
        this.unit = unit;
        this.requires_prescription = requires_prescription;
        this.barcode = barcode;
    }

    public Medication(int medication_id, String name, String generic_name, String description,
                      String side_effects, String contraindications, String interactions,
                      String manufacturer, String unit, boolean requires_prescription,
                      String barcode, LocalDateTime created_at) {
        this.medication_id = medication_id;
        this.name = name;
        this.generic_name = generic_name;
        this.description = description;
        this.side_effects = side_effects;
        this.contraindications = contraindications;
        this.interactions = interactions;
        this.manufacturer = manufacturer;
        setUnit(unit); 
        this.requires_prescription = requires_prescription;
        this.barcode = barcode;
        this.created_at = created_at;
    }

    public int getMedicationId() { return medication_id; }
    public void setMedicationId(int medication_id) { this.medication_id = medication_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGenericName() { return generic_name; }
    public void setGenericName(String generic_name) { this.generic_name = generic_name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSideEffects() { return side_effects; }
    public void setSideEffects(String side_effects) { this.side_effects = side_effects; }

    public String getContraindications() { return contraindications; }
    public void setContraindications(String contraindications) { this.contraindications = contraindications; }

    public String getInteractions() { return interactions; }
    public void setInteractions(String interactions) { this.interactions = interactions; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getUnit() {
        return unit != null ? unit.getDisplayName() : null;
    }
    public void setUnit(String unit) {
        this.unit = unit != null ? Unit.valueOf(unit) : null;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean isRequiresPrescription() { return requires_prescription; }
    public void setRequiresPrescription(boolean requires_prescription) { this.requires_prescription = requires_prescription; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Medication{" +
                "medication_id=" + medication_id +
                ", name='" + name + '\'' +
                ", generic_name='" + generic_name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", unit=" + (unit != null ? unit.getDisplayName() : "null") +
                ", requires_prescription=" + requires_prescription +
                ", created_at=" + created_at +
                '}';
    }
}