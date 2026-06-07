package com.example.dailydictation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_closure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentClosure {

    @EmbeddedId
    private CommentClosureId id;

    @Column(name = "depth")
    private int depth;

    public CommentClosure(int ancestorId, int descendantId, int depth) {
        this.id = new CommentClosureId(ancestorId, descendantId);
        this.depth = depth;
    }

    public int getAncestorId() {
        return this.id.getAncestorId();
    }

    public int getDescendantId() {
        return this.id.getDescendantId();
    }

}
