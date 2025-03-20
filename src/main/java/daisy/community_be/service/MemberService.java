package daisy.community_be.service;

import daisy.community_be.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<String> getAllMembers() {
        return memberRepository.getAllMemberNames();
    }
}