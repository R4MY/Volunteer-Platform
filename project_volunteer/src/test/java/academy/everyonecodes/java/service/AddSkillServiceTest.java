package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AddSkillServiceTest {
    @Autowired
    AddSkillService addSkillService;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    Principal principal;
    @MockBean
    UserAndSkillTranslator translator;


    @Test
    void addSkill_userNotFound_test() {
        String username = "test";
        String skillToAdd = "skill";
        SkillDTO skilldto = new SkillDTO(skillToAdd);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        addSkillService.addSkill(username, skilldto, principal);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verifyNoInteractions(skillRepository);
    }

    @Test
    void addSkill_userFound_createNewSkill_noNumNoSpecialCh_test() {
        String username = "test";
        String skillToAdd = "skill";
        Long id = 1L;
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        user.setId(id);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.empty());
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);
        Mockito.when(translator.translateToSkill(skilldto)).thenReturn(skill);
        Mockito.when(skillRepository.save(skill)).thenReturn(skill);
        Mockito.when(translator.translateToSkillDTO(skill)).thenReturn(skilldto);

        addSkillService.addSkill(username, skilldto, principal);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(principal).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verify(translator).translateToSkill(skilldto);
        Mockito.verify(skillRepository).save(skill);
        Mockito.verify(translator).translateToSkillDTO(skill);

    }
    @Test
    void addSkill_userFound_createNewSkill_withNumOrSpecialCh_test() {
        String username = "test";
        String skillToAdd = "34234*#3234";
        Long id = 1L;
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        user.setId(id);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.empty());
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        addSkillService.addSkill(username, skilldto, principal);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(principal).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verifyNoInteractions(translator);
        Mockito.verifyNoMoreInteractions(skillRepository);
    }
    @Test
    void addSkill_userFound_addToExistingSkill_noNumNoSpecialCh_test() {
        String username = "test";
        String existingSkill = "existingSkill";
        String skillToAdd = "skill";

        Long id = 1L;
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        user.setId(id);
        Skill skill = new Skill(id, user, existingSkill);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.of(skill));
        existingSkill = existingSkill + ";" + skillToAdd;
        Skill skillToSave = new Skill(id, user, existingSkill);
        SkillDTO skillDTOToSave = new SkillDTO(existingSkill);

        Mockito.when(skillRepository.save(skillToSave)).thenReturn(skillToSave);
        Mockito.when(translator.translateToSkillDTO(skillToSave)).thenReturn(skillDTOToSave);

        Optional<SkillDTO> result = addSkillService.addSkill(username, skilldto, principal);
        Assertions.assertEquals(Optional.of(skillDTOToSave), result);
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(principal).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verify(skillRepository).save(skillToSave);
        Mockito.verify(translator).translateToSkillDTO(skillToSave);

    }
    @Test
    void addSkill_userFound_addToExistingSkill_withNumOrSpecialCh_test() {
        String username = "test";
        String skillToAdd = "34234*#3234";
        Long id = 1L;
        User user = new User("test", "test", "test", "test", "test", LocalDate.of(2021, 2, 2), "test", "test", "test", "test", "test", "test", "test", Set.of());
        user.setId(id);
        Skill skill = new Skill(id, user, skillToAdd);
        SkillDTO skilldto = new SkillDTO(skillToAdd);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(principal.getName()).thenReturn(username);
        Mockito.when(skillRepository.findById(id)).thenReturn(Optional.of(skill));

        addSkillService.addSkill(username, skilldto, principal);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(principal).getName();
        Mockito.verify(skillRepository).findById(id);
        Mockito.verifyNoInteractions(translator);
        Mockito.verifyNoMoreInteractions(skillRepository);
    }
}