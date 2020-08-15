package com.senla.training_2019.smolka.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@NamedEntityGraph(name = "Phone.store",
        attributeNodes = @NamedAttributeNode("store")
)
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private Store store;

    @Column(name = "operator_code")
    private String operatorCode;

    @Column(name = "number")
    private String number;

    public Phone() {
    }

    public Phone(Store store, String operatorCode, String number) {
        this.store = store;
        this.operatorCode = operatorCode;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
