package com.yearup.soulfoodpos;

import com.yearup.soulfoodpos.model.ShopInfo;
import com.yearup.soulfoodpos.ui.UserInterface;

public class Program {

    public static void main(String[] args) {
        ShopInfo shop = new ShopInfo(
                "Dorothy's Soul Bowls",
                "Real soul food, real love.",
                "1247 Fillmore Street",
                "San Francisco, CA 94115",
                "(415) 555-0184",
                "Tue–Sun  ·  11am–9pm  (Closed Mon)"
        );
        UserInterface ui = new UserInterface(shop);
        ui.display();
    }
}
