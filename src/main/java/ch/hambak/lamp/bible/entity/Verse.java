package ch.hambak.lamp.bible.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"book_id", "chapter", "index"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verse {
    @Id
    @Column(name = "verse_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Short chapter;

    @Column(nullable = false)
    private Short index;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
