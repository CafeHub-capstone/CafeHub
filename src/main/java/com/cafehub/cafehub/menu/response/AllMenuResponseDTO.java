package com.cafehub.cafehub.menu.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMenuResponseDTO {

    // 지금은 Response에 필드가 하나 밖에 없지만, 만약 추후 페이징을 위해 total page, isLast 등을 추가 하게 되거나
    // 그 외에도 추가적인 변경사항에 대응하기 위해서 해당 DTO를 남겨둠

    private List<MenuResponseDTO> menuList;

}
