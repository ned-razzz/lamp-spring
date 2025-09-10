package ch.hambak.lamp.bible.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"book_id", "ordinal"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chapter {
    @Id
    @Column(name = "chapter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer ordinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    private List<Verse> verses = new ArrayList<>();
}
