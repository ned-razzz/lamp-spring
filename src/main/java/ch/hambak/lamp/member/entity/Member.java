package ch.hambak.lamp.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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

    public static Member create(String email, String password, Role role) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.role = role;
        return member;
    }

    public void update(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void delete() {
        //todo: modify status field for soft delete
    }
}
