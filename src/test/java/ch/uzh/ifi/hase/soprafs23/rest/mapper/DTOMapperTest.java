//package ch.uzh.ifi.hase.soprafs23.rest.mapper;
//
//import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs23.entity.User;
//import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
//import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * DTOMapperTest
// * Tests if the mapping between the internal and the external/API representation
// * works.
// */
//public class DTOMapperTest {
//  @Test
//  public void testCreateUser_fromUserPostDTO_toUser_success() {
//    // create UserPostDTO
//    UserPostDTO userPostDTO = new UserPostDTO();
//    userPostDTO.setPassword("TestPassword");
//    userPostDTO.setUsername("TestUsername");
//
//    // MAP -> Create user
//    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
//
//    // check content
//    assertEquals(userPostDTO.getPassword(), user.getPassword());
//    assertEquals(userPostDTO.getUsername(), user.getUsername());
//  }
//
//  @Test
//  public void testGetUser_fromUser_toUserGetDTO_success() {
//    // create User
//    User user = new User();
//    user.setPassword("TestPassword");
//    user.setUsername("TestUsername");
//    user.setStatus(UserStatus.OFFLINE);
//    user.setId(1L);
//
//    // MAP -> Create UserGetDTO
//    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
//
//    // check content
//    assertEquals(user.getId(), userGetDTO.getId());
//    assertEquals(user.getPassword(), userGetDTO.getPassword());
//    assertEquals(user.getUsername(), userGetDTO.getUsername());
//    assertEquals(user.getStatus(), userGetDTO.getStatus());
//  }
//}
