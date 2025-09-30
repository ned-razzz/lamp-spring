package ch.hambak.lamp.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@SQLRestriction("status = 'ACTIVE'")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column
    @PastOrPresent
    private LocalDateTime created;

    @Column
    @PastOrPresent
    private LocalDateTime updated;

    //== Business Logic ==//
    public static Member create(String email, String password, Role role) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.role = role;
        member.status = MemberStatus.ACTIVE;
        member.created = LocalDateTime.now();
        member.updated = LocalDateTime.now();
        return member;
    }

    public void update(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.updated = LocalDateTime.now();
    }

    public void delete() {
        this.status = MemberStatus.DELETED;
        this.updated = LocalDateTime.now();
    }
}
