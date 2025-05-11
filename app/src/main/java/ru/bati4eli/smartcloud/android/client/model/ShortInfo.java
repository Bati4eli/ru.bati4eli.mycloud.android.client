package ru.bati4eli.smartcloud.android.client.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumCardModel;

/**
 * ДТО для скачивания файлов или превью.
 */
@Data
@Accessors(chain = true)
public class ShortInfo {
    private Long fileId;
    private String fileName;
    private TypeOfFile type;

    public static ShortInfo of(GrpcFile file) {
        return new ShortInfo()
                .setFileId(file.getFileId())
                .setType(file.getMediaType())
                .setFileName(file.getName());
    }

    public static ShortInfo of(ShortMediaInfoDto file) {
        return new ShortInfo()
                .setFileId(file.getFileId())
                .setType(file.getMediaType())
                .setFileName(null);
    }

    public static ShortInfo of(AlbumCardModel albumInfo) {
        return new ShortInfo()
                .setFileId(albumInfo.getFileId())
                .setType(TypeOfFile.IMAGE)
                .setFileName(null);
    }

}
