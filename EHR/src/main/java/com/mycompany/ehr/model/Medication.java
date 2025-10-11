package com.mycompany.ehr.model;
import java.util.*;
import java.time.*;
public class Medication {
    private int medicationId;
    private String name;
    private String genericName;
    private String description;
    private String sideEffects;
    private String contraindications;
    private String interactions;
    private String manufacturer;
    private String unit;
    private boolean requiresPrescription;
    private String barcode;
    private LocalDateTime createdAt;

    public Medication(){}
    
    public Medication(int medicationId, String name, String genericName, String description,
                       String sideEffects, String contraindications, String interactions,
                       String manufacturer, String unit, boolean requiresPrescription,
                       String barcode, LocalDateTime createdAt) {
        this.medicationId = medicationId;
        this.name = name;
        this.genericName = genericName;
        this.description = description;
        this.sideEffects = sideEffects;
        this.contraindications = contraindications;
        this.interactions = interactions;
        this.manufacturer = manufacturer;
        this.unit = unit;
        this.requiresPrescription = requiresPrescription;
        this.barcode = barcode;
        this.createdAt = createdAt;
    }

    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSideEffects() { return sideEffects; }
    public void setSideEffects(String sideEffects) { this.sideEffects = sideEffects; }

    public String getContraindications() { return contraindications; }
    public void setContraindications(String contraindications) { this.contraindications = contraindications; }

    public String getInteractions() { return interactions; }
    public void setInteractions(String interactions) { this.interactions = interactions; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}