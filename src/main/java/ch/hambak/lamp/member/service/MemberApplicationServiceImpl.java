package ch.hambak.lamp.member.service;

import ch.hambak.lamp.member.dto.MemberCreateRequest;
import ch.hambak.lamp.member.entity.Member;
import ch.hambak.lamp.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberApplicationServiceImpl implements MemberApplicationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerMember(MemberCreateRequest createRequest) {
        memberRepository.findByEmail(createRequest.getEmail())
                .ifPresent(member -> { throw new IllegalStateException("already exist member"); });
        Member member = Member.create(
                createRequest.getEmail(),
                passwordEncoder.encode(createRequest.getPassword()),
                createRequest.getRole());
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("active member of id %d is not exist.".formatted(id)));
        member.delete();
    }
}
