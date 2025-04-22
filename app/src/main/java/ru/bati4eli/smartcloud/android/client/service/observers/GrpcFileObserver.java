package ru.bati4eli.smartcloud.android.client.service.observers;

import android.util.Log;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import lombok.Data;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.fileHelpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


@Data
public class GrpcFileObserver extends SyncStreamObserver<GrpcFile> {
    private final FileAdapter fileAdapter;
    private final SwipeRefreshLayout swipeRefreshLayout;
    private final GrpcService grpcService = MiserableDI.get(GrpcService.class);

    public GrpcFileObserver(FileAdapter fileAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.fileAdapter = fileAdapter;
        this.fileAdapter.resetNewTree(GrpcFileComparator.getFileComparator());
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onNext(GrpcFile grpcFile) {
        fileAdapter.add(grpcFile);
        // Если это Видео/Фото, то загружаем превью для него!
        if (grpcFile.getMediaType() == TypeOfFile.IMAGE || grpcFile.getMediaType() == TypeOfFile.VIDEO) {
            // Повторно не надо скачивать превью
            if (!MyUtils.previewExists(grpcFile, DownloadType.PREVIEW_MINI))
                // Синхронный метод!
                grpcService.downloadFile(grpcFile, DownloadType.PREVIEW_MINI);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        onFinally();
        Log.d(TAG, "GrpcFileStreamObserver: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onCompleted() {
        onFinally();
    }

    private void onFinally() {
        fileAdapter.finishAndShow();
        swipeRefreshLayout.setRefreshing(false);
        working.set(false);
    }

}
