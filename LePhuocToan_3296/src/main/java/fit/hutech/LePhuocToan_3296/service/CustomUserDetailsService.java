package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.User;
import fit.hutech.LePhuocToan_3296.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String trimmedUsername = username.trim();

        User user = userRepository.findByUsername(trimmedUsername)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", trimmedUsername);
                    return new UsernameNotFoundException("User not found: " + trimmedUsername);
                });

        Boolean isActive = user.getIsActive();
        if (isActive == null || !isActive) {
            log.warn("User account is disabled: {}", trimmedUsername);
            throw new UsernameNotFoundException("User account is disabled");
        }

        Collection<? extends GrantedAuthority> authorities = getAuthorities(user);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!isActive)
                .credentialsExpired(false)
                .disabled(!isActive)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            return user.getRoles().stream()
                    .map(role -> {
                        String roleName = role.getName();
                        if (!roleName.startsWith("ROLE_")) {
                            roleName = "ROLE_" + roleName;
                        }
                        return new SimpleGrantedAuthority(roleName);
                    })
                    .collect(Collectors.toSet());
        }

        // Fallback: use user.role string field
        String roleName = user.getRole();
        if (roleName != null && !roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        return Set.of(new SimpleGrantedAuthority(roleName != null ? roleName : "ROLE_USER"));
    }
}