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
    private int albumImage;
    private int labelIconSrc;
    private RespAlbumInfo albumInfo;

    public static AlbumCardModel of(RespAlbumInfo album) {
        return new AlbumCardModel()
                .setAlbumInfo(album)
                .setAmount(album.getAmount())
                .setLabel(album.getAlbumName());
    }
}
