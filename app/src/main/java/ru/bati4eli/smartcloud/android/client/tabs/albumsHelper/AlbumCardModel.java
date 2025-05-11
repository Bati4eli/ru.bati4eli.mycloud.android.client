package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import lombok.Data;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;

@Data
public class AlbumCardModel {

    private final String fontAwesomeIcon;
    private final String label;

    private final Long fileId;
    private final String albumId;
    private final String albumName;
    private final Integer amount;
    // for map
    private final Double latitude;
    private final Double longitude;
    private final Double radiusKm;

    public AlbumCardModel(RespAlbumInfo albumInfo, String fontAwesomeIcon, String label) {
        // from AlbumInfo
        this.fileId = albumInfo.getFileId();
        this.albumId = albumInfo.getAlbumId();
        this.albumName = albumInfo.getAlbumName();
        this.amount = albumInfo.getAmount();
        this.latitude = albumInfo.getLatitude();
        this.longitude = albumInfo.getLongitude();
        this.radiusKm = albumInfo.getRadiusKm();
        // other
        this.fontAwesomeIcon = fontAwesomeIcon;
        this.label = label;
    }

}
