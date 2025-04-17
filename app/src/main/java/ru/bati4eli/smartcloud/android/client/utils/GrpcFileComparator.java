package ru.bati4eli.smartcloud.android.client.utils;

import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;

import java.util.Comparator;

public class GrpcFileComparator {

    public static Comparator<GrpcFile> getFileComparator(SortByEnum sortBy, SortOrderEnum sortOrder) {
        Comparator<GrpcFile> foldersFirst = (f1, f2) -> {
            boolean isFolder1 = f1.getMediaType() == TypeOfFile.FOLDER;
            boolean isFolder2 = f2.getMediaType() == TypeOfFile.FOLDER;
            // Сначала сравниваем: если обе не папки или обе папки — 0.
            if (isFolder1 == isFolder2) return 0;
            // Если первая папка — она должна быть выше.
            return isFolder1 ? -1 : 1;
        };

        Comparator<GrpcFile> mainComparator = null;
        switch (sortBy) {
            case SORT_BY_CREATE_DATE:
                mainComparator = Comparator.comparing(g -> MyUtils.parseDate(g.getCreated()));
                break;
            case SORT_BY_CHANGE_DATE:
                mainComparator = Comparator.comparing(g -> MyUtils.parseDate(g.getLastModify()));
                break;
            case SORT_BY_NAME:
                mainComparator = Comparator.comparing(GrpcFile::getName, String::compareToIgnoreCase);
                break;
            case SORT_BY_TYPE:
                mainComparator = Comparator.comparing(GrpcFile::getMediaType);
                break;
            case SORT_BY_SIZE:
                mainComparator = Comparator.comparing(GrpcFile::getSize);
                break;
        }

        if (sortOrder == SortOrderEnum.SORT_DESCENDING) {
            mainComparator = mainComparator.reversed();
        }

        // Сначала папки вверх, затем сортировка по требованию, затем hashCode для уникальности
        return foldersFirst
                .thenComparing(mainComparator)
                .thenComparing(GrpcFile::hashCode);
    }
}
