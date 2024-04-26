package com.cafehub.cafehub.menu.service;

import com.cafehub.cafehub.menu.request.AllMenuRequest;
import com.cafehub.cafehub.menu.response.AllMenuResponse;

public interface MenuService {
    AllMenuResponse getAllMenu(AllMenuRequest allMenuRequest);
}
