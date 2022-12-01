package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void fromUserDtoTest() throws IOException {
        UserDto userDto = new UserDto(1L, "Name", "qwe@qwe.qwe");
        JsonContent<UserDto> result = json.write(userDto);
        then(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) 1L);
        then(result).extractingJsonPathStringValue("$.name").isEqualTo("Name");
        then(result).extractingJsonPathStringValue("$.email").isEqualTo("qwe@qwe.qwe");
    }

    @Test
    void toUserDtoTest() throws IOException {
        String jsonString = "{\"name\":\"Name\",\"email\":\"qwe@qwe.qwe\"}";
        UserDto userDto = this.json.parse(jsonString).getObject();
        then(userDto.getName()).isEqualTo("Name");
        then(userDto.getEmail()).isEqualTo("qwe@qwe.qwe");
    }
}