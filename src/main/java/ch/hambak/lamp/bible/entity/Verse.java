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

    //todo: verse와 chapter 분리하기
    @Column(nullable = false)
    private Short chapter;

    //todo: short 타입 전부 integer로 변경 (연산 시 타입 변경 때문)
    //todo: index => ordinal로 변경
    @Column(nullable = false)
    private Short index;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
