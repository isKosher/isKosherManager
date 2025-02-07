package com.kosher.iskosher.entity;

import com.kosher.iskosher.common.interfaces.NamedEntity;
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
@Table(name = "kosher_types")
public class KosherType implements NamedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "kosher_icon_url", length = Integer.MAX_VALUE)
    private String kosherIconUrl;

    @OneToMany(mappedBy = "kosherType")
    private Set<KosherTypeBusiness> kosherTypeVsBusinesses = new LinkedHashSet<>();

}