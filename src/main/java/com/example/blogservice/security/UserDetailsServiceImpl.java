package com.example.blogservice.security;

import com.example.blogservice.entity.User;
import com.example.blogservice.repository.UserRepository;
import com.example.blogservice.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.blogservice.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // 0. DI
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. DB 에서 User 의 이름을 통해서 조홰함.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        // 2. UserDetailsImpl 로 return.
        return new UserDetailsImpl(user, user.getUsername());
    }

}