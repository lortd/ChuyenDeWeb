package com.example.dailydictation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentClosureId implements Serializable {

    @Column(name = "ancestor_id")
    private Integer ancestorId;

    @Column(name = "descendant_id")
    private Integer descendantId;
}
