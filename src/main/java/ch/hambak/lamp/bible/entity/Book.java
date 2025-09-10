package ch.hambak.lamp.bible.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 5, nullable = false, unique = true)
    private String abbrKor;

    @Column(length = 5, nullable = false, unique = true)
    private String abbrEng;

    @Column(nullable = false, unique = true)
    private Integer sequence;
}
