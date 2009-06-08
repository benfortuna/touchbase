/*
 * This file is part of Touchbase.
 *
 * Created: [07/08/2008]
 *
 * Copyright (c) 2008, Ben Fortuna
 *
 * Touchbase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Touchbase is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Touchbase.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.touchbase.im;

import javax.swing.AbstractButton;
import javax.swing.border.Border;

import org.mnode.base.views.icon.AbstractIconSetFactory;
import org.mnode.base.views.icon.IconSet;
import org.mnode.base.views.icon.LiquidicityIconSetFactory;

/**
 * @author Ben
 * 
 */
public final class Actions {

    private static IconSet icons = AbstractIconSetFactory.getInstance(LiquidicityIconSetFactory.class.getName())
            .getDefaultIconSet();

    /**
     * Enforce static nature.
     */
    private Actions() {
    }

    /**
     * @param a1 the button to configure
     * @param border the border to apply to the button
     * @return the configured button instance
     */
    public static AbstractButton configureSmallButton(AbstractButton a1, Border border) {
        return configureSmallButton(a1, null, border);
    }

    /**
     * @param a1 the button to configure
     * @param iconKey the key of the icon to apply to the button
     * @param border the border to apply to the button
     * @return the configured button instance
     */
    public static AbstractButton configureSmallButton(AbstractButton a1, String iconKey, Border border) {
        if (iconKey != null) {
            a1.setSelectedIcon(icons.getSelectedIcon(iconKey));
            a1.setRolloverIcon(icons.getRolloverIcon(iconKey));
        }
        a1.setMargin(null);
        a1.setBorder(border);
        a1.setBorderPainted(false);
        a1.setContentAreaFilled(false);
        a1.setFocusPainted(false);
        a1.setOpaque(false);
        return a1;
    }
}
