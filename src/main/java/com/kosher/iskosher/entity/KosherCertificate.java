package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "kosher_certificates")
public class KosherCertificate {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "certificate", nullable = false, length = Integer.MAX_VALUE)
    private String certificate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @OneToMany(mappedBy = "kosherCertificate", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Business> businesses = new LinkedHashSet<>();

}