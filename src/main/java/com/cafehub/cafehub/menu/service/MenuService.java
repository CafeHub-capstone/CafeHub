package com.cafehub.cafehub.menu.service;

import com.cafehub.cafehub.menu.request.AllMenuGetRequestDTO;
import com.cafehub.cafehub.menu.response.AllMenuResponseDTO;

public interface MenuService {

    AllMenuResponseDTO getAllMenuByCafeId(AllMenuGetRequestDTO request);
}
