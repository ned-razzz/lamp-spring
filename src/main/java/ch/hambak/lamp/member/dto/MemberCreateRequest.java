package ch.hambak.lamp.member.dto;

import ch.hambak.lamp.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateRequest {
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private Role role;
}
