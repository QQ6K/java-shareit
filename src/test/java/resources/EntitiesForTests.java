package resources;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public class EntitiesForTests {

    private static final UserDto userDto1 = new UserDto(
            1L,
            "Паша",
            "test1@test.test"
    );

    private static final UserDto userDto2 = new UserDto(
            2L,
            "Миша",
            "doornail@test.test"
    );

    private static final ItemDto itemDto1 = new ItemDto(
            1L,
            "Гвоздь",
            "Очень хорошие гвозди",
            true,
            1L,
            "ссылка.гвоздь.1"
    );

    private static final ItemDto itemDto2 = new ItemDto(
            2L,
            "Цветы",
            "Цветы для подоконника",
            true,
            2L,
            "ссылка.цвето.ком"
    );

    private static final ItemDto itemDto3 = new ItemDto(
            3L,
            "Шарж",
            "Несмешной, но в этом вся суть",
            true,
            1L,
            "пятькопеек.тык.ру"
    );

    public static UserDto getTestUserDto1() {
        return userDto1;
    }

    public static UserDto getTestUserDto2() {
        return userDto2;
    }

    public static ItemDto getTestItem1() {
        return itemDto1;
    }

    public static ItemDto getTestItem2() {
        return itemDto2;
    }

    public static ItemDto getTestItem3() {
        return itemDto3;
    }

}
