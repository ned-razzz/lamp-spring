package ch.hambak.lamp.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminMember {
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

    public void approvePendingMember() {
        Assert.state(this.status == MemberStatus.PENDING,
                "the status of the member is not PENDING: %s".formatted(this.email));
        this.status = MemberStatus.ACTIVE;
        this.updated = LocalDateTime.now();
    }

    public void delete() {
        this.status = MemberStatus.DELETED;
        this.updated = LocalDateTime.now();
    }
}
