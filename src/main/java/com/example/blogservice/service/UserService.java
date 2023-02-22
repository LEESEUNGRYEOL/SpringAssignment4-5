package com.example.blogservice.service;

import com.example.blogservice.dto.LoginRequestDto;
import com.example.blogservice.dto.BaseResponseDto;
import com.example.blogservice.dto.SignupRequestDto;
import com.example.blogservice.entity.User;
import com.example.blogservice.entity.UserRoleEnum;
import com.example.blogservice.jwt.JwtUtil;
import com.example.blogservice.repository.UserRepository;
import com.example.blogservice.util.CustomException;
import com.example.blogservice.util.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.blogservice.util.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 요구사항 1) 회원가입 기능 (ver. Builder, ResponseEntity)
    @Transactional
    public ResponseEntity<BaseResponseDto> signup(SignupRequestDto signupRequestDto) throws CustomException {

        // 1) SignupRequestDto 를 통해서 Client 에게 username 과 password 를 전달받고, User의 Role 지정.
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 2) 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(DUPLICATE_USER);
        }

        // 3) 사용자 ROLE 확인
        // USER 인 경우
        UserRoleEnum role = UserRoleEnum.USER;

        // ADMIN 인 경우
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(INVALID_ADMIN_TOKEN);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 4) DB 저장후 Return.
        User user = User.of(username, password, role);
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(BaseResponseDto.of(SuccessCode.SIGNUP_SUCCESS));
    }

    // 요구사항 2) 로그인 기능(ver. Builder, ResponseEntity)
    @Transactional
    public ResponseEntity<BaseResponseDto> login(LoginRequestDto loginRequestDto) {
        // 1)  SignupRequestDto 를 통해서 Client 에게 username 과 password 를 전달받고, User의 Role 지정.
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 2) 가져온 Username 을 통해서 Db에 있는 username 과 비교해 사용자가 존재하는지 확인.
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND_USER);
        }

        // 3) 비밀번호가 Client 에게 받은 비밀번호와 일치하는지 확인.
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new CustomException(NOT_FOUND_USER);
        }

        // 4) header 에 들어갈 JWT 설정함.
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));

        // 5) ResponseEntity 로 Return
        return ResponseEntity.ok()
                .headers(headers)
                .body(BaseResponseDto.of(SuccessCode.LOGIN_SUCCESS));
    }

}

