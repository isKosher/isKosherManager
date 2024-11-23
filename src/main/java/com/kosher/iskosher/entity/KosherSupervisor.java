package com.kosher.iskosher.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "kosher_supervisors")
public class KosherSupervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotNull
    @Column(name = "contact_info", nullable = false, length = Integer.MAX_VALUE)
    private String contactInfo;

    @NotNull
    @Column(name = "authority", nullable = false, length = Integer.MAX_VALUE)
    private String authority;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<SupervisorsBusiness> supervisorsVsBusinesses = new LinkedHashSet<>();

}