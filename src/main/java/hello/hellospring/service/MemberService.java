package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class MemberService {

    // 기존에는 회원 서비스가 메모리 회원 리포지토리를 직접 생성
    // private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 회원 서비스 코드를 DI 가능하게 변경
    MemberRepository memberRepository;

    // MemberServiceTest에 있는 MemberRepository가 같은 인스턴스이게 하기 위함
    // 1개만 있으면 @Autowired 는 생략 가능
    // @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");      // 부정 또는 부적절한 때에 메서드가 불려 간 것을 나타내는 오류
                });
        // IllegalStateException 부정 또는 올바르지 않은 때에 메소드가 불려 간 것
        // void ifPresent()는 Optional 객체가 값을 가지고 있으면 실행 값이 없으면 넘어감
        // Boolean isPresent()는 Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
