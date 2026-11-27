package com.platform.scriptures.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "source_references")
public class SourceReferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passage_id", nullable = false)
    private Long passageId;

    @Column(name = "canonical_ref_key", nullable = false, length = 160)
    private String canonicalRefKey;

    @Column(name = "hierarchy_label_1", length = 60)
    private String hierarchyLabel1;

    @Column(name = "hierarchy_value_1", length = 80)
    private String hierarchyValue1;

    @Column(name = "hierarchy_label_2", length = 60)
    private String hierarchyLabel2;

    @Column(name = "hierarchy_value_2", length = 80)
    private String hierarchyValue2;

    @Column(name = "hierarchy_label_3", length = 60)
    private String hierarchyLabel3;

    @Column(name = "hierarchy_value_3", length = 80)
    private String hierarchyValue3;

    @Column(name = "hierarchy_label_4", length = 60)
    private String hierarchyLabel4;

    @Column(name = "hierarchy_value_4", length = 80)
    private String hierarchyValue4;

    @Column(name = "display_reference", nullable = false, length = 255)
    private String displayReference;

    public Long getId() { return id; }
    public Long getPassageId() { return passageId; }
    public void setPassageId(Long passageId) { this.passageId = passageId; }
    public String getCanonicalRefKey() { return canonicalRefKey; }
    public void setCanonicalRefKey(String canonicalRefKey) { this.canonicalRefKey = canonicalRefKey; }
    public String getHierarchyLabel1() { return hierarchyLabel1; }
    public void setHierarchyLabel1(String hierarchyLabel1) { this.hierarchyLabel1 = hierarchyLabel1; }
    public String getHierarchyValue1() { return hierarchyValue1; }
    public void setHierarchyValue1(String hierarchyValue1) { this.hierarchyValue1 = hierarchyValue1; }
    public String getHierarchyLabel2() { return hierarchyLabel2; }
    public void setHierarchyLabel2(String hierarchyLabel2) { this.hierarchyLabel2 = hierarchyLabel2; }
    public String getHierarchyValue2() { return hierarchyValue2; }
    public void setHierarchyValue2(String hierarchyValue2) { this.hierarchyValue2 = hierarchyValue2; }
    public String getHierarchyLabel3() { return hierarchyLabel3; }
    public void setHierarchyLabel3(String hierarchyLabel3) { this.hierarchyLabel3 = hierarchyLabel3; }
    public String getHierarchyValue3() { return hierarchyValue3; }
    public void setHierarchyValue3(String hierarchyValue3) { this.hierarchyValue3 = hierarchyValue3; }
    public String getHierarchyLabel4() { return hierarchyLabel4; }
    public void setHierarchyLabel4(String hierarchyLabel4) { this.hierarchyLabel4 = hierarchyLabel4; }
    public String getHierarchyValue4() { return hierarchyValue4; }
    public void setHierarchyValue4(String hierarchyValue4) { this.hierarchyValue4 = hierarchyValue4; }
    public String getDisplayReference() { return displayReference; }
    public void setDisplayReference(String displayReference) { this.displayReference = displayReference; }
}

