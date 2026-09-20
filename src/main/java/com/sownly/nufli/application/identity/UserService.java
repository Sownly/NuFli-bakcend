package com.sownly.nufli.application.identity;

import com.sownly.nufli.common.exception.ResourceNotFoundException;
import com.sownly.nufli.domain.identity.User;
import com.sownly.nufli.domain.identity.UserProfile;
import com.sownly.nufli.domain.identity.UserProfileRepository;
import com.sownly.nufli.domain.identity.UserRepository;
import com.sownly.nufli.presentation.web.identity.dto.UserProfileRequest;
import com.sownly.nufli.presentation.web.identity.dto.UserProfileResponse;
import com.sownly.nufli.presentation.web.identity.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    public UserService(UserRepository userRepository, UserProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return UserResponse.from(user);
    }

    public UserProfileResponse updateProfile(UUID userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile(user);
        }

        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setBirthDate(request.birthDate());
        profile.setBiologicalSex(request.biologicalSex());
        profile.setHeightCm(request.heightCm());
        profile.setActivityLevel(request.activityLevel());

        user.setProfile(profile);
        profileRepository.save(profile);

        return UserProfileResponse.from(profile);
    }
}
