package ru.bati4eli.smartcloud.android.client.config;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

public class MyFontAwesomeModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        // todo тут можно переопределить шрифт иконок
        // see https://github.com/JoanZapata/android-iconify?tab=readme-ov-file
        return "FontAwesome/fontawesome.ttf";
    }

    @Override
    public Icon[] characters() {
        return null;
    }
}
