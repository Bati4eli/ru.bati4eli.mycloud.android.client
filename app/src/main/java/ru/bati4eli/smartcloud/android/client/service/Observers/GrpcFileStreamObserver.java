package ru.bati4eli.smartcloud.android.client.service.Observers;

import android.util.Log;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


@Data
public class GrpcFileStreamObserver implements StreamObserver<GrpcFile> {
    private final FileAdapter fileAdapter;
    private final SwipeRefreshLayout swipeRefreshLayout;
    private final GrpcService grpcService = MiserableDI.get(GrpcService.class);
    private AtomicBoolean working = new AtomicBoolean(true);

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
        Log.d(TAG, "GrpcFileStreamObserver: " + throwable.getLocalizedMessage());
        swipeRefreshLayout.setRefreshing(false);
        working.set(false);
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "### GrpcFileStreamObserver COMPLETED!!!!");
        fileAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        working.set(false);
    }

    public boolean isWorking() {
        return working.get();
    }

    public void waiting() {
        while (isWorking()) {
        }
    }
}
