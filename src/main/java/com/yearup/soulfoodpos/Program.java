package com.yearup.soulfoodpos;

import com.yearup.soulfoodpos.ui.UserInterface;

public class Program {


    public static final String SHOP_NAME = "Dorothy's Oven";

    public static void main(String[] args) {
        UserInterface ui = new UserInterface(SHOP_NAME);
        ui.display();
    }
}
