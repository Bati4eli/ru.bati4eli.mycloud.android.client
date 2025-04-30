package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AlbumCardModel {
    private String label;
    private int amount;
    private String fontAwesomeIcon;
    private RespAlbumInfo albumInfo;

    public static AlbumCardModel of(RespAlbumInfo album, String fontAwesomeIcon, String label) {
        return new AlbumCardModel()
                .setLabel(label)
                .setFontAwesomeIcon(fontAwesomeIcon)
                .setAlbumInfo(album)
                .setAmount(album.getAmount());
    }
}
