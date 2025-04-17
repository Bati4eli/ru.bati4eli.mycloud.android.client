package ru.bati4eli.smartcloud.android.client.service.Observers;

import android.util.Log;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


@Data
public class GrpcFileStreamObserver implements StreamObserver<GrpcFile> {
    private final FileAdapter fileAdapter;
    private final SwipeRefreshLayout swipeRefreshLayout;
    private final GrpcService grpcService = MiserableDI.get(GrpcService.class);
    private AtomicBoolean working = new AtomicBoolean(true);
    private final TreeSet<GrpcFile> sortedFiles;

    public GrpcFileStreamObserver(FileAdapter fileAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.fileAdapter = fileAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        SortByEnum sortBy = ParametersUtil.getSortBy();
        SortOrderEnum sortOrder = ParametersUtil.getSortOrder();
        this.sortedFiles = new TreeSet<>(GrpcFileComparator.getFileComparator(sortBy, sortOrder));
    }

    @Override
    public void onNext(GrpcFile grpcFile) {

        sortedFiles.add(grpcFile);

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
        fileAdapter.addAll(sortedFiles);
        swipeRefreshLayout.setRefreshing(false);
        working.set(false);
        Log.d(TAG, "GrpcFileStreamObserver: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onCompleted() {
        fileAdapter.addAll(sortedFiles);
        swipeRefreshLayout.setRefreshing(false);
        working.set(false);
        //Log.d(TAG, "### GrpcFileStreamObserver COMPLETED!!!!");
    }


    public boolean isWorking() {
        return working.get();
    }

    public void waiting() {
        while (isWorking()) {
        }
    }
}
