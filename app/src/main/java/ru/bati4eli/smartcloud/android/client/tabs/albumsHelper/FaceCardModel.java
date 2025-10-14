package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import lombok.Data;
import ru.bati4eli.mycloud.repo.ShortFaceDto;

@Data
public class FaceCardModel implements AlbumInterface {
    private final Long faceId;
    private final String faceName;
    private final Integer amount;

    public FaceCardModel(ShortFaceDto faceDto) {
        this.faceId = faceDto.getGroupId();
        this.faceName = faceDto.getName();
        this.amount = faceDto.getAmountFiles();
    }
}
