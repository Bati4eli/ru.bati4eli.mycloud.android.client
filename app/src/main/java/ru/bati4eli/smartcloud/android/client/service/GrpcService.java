package ru.bati4eli.smartcloud.android.client.service;

import android.util.Log;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.var;
import ru.bati4eli.mycloud.repo.AlbumType;
import ru.bati4eli.mycloud.repo.DateCounterResponse;
import ru.bati4eli.mycloud.repo.DownloadFileReq;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.FileInfoRequest;
import ru.bati4eli.mycloud.repo.FileUserRepoServiceGrpc;
import ru.bati4eli.mycloud.repo.GetFacesReq;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.MediaServiceGrpc;
import ru.bati4eli.mycloud.repo.ReqAlbumInfo;
import ru.bati4eli.mycloud.repo.ReqFilterMedias;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;
import ru.bati4eli.mycloud.repo.ShortFaceDto;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.mycloud.users.JwtRequest;
import ru.bati4eli.mycloud.users.JwtResponse;
import ru.bati4eli.mycloud.users.UserPrivateServiceGrpc;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.service.observers.AdapterItemsObserver;
import ru.bati4eli.smartcloud.android.client.service.observers.DownloadFileObserver;
import ru.bati4eli.smartcloud.android.client.service.observers.StreamObserverIterator;
import ru.bati4eli.smartcloud.android.client.service.observers.SyncObserverOneResponse;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoObserver;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.time.OffsetDateTime;
import java.util.Iterator;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class GrpcService {

    private UserPrivateServiceGrpc.UserPrivateServiceBlockingStub authClient;
    private FileUserRepoServiceGrpc.FileUserRepoServiceStub repoClient;
    private MediaServiceGrpc.MediaServiceStub mediaService;

    public static GrpcService init() {
        return new GrpcService();
    }

    private GrpcService() {
        try {
            authClient = UserPrivateServiceGrpc.newBlockingStub(MiserableDI.get(ManagedChannel.class));
            Channel channel = MiserableDI.get(Channel.class);
            repoClient = FileUserRepoServiceGrpc.newStub(channel);
            mediaService = MediaServiceGrpc.newStub(channel);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public JwtResponse authorize(String username, String password) {
        var jwtRequest = JwtRequest.newBuilder()
                .setLogin(username)
                .setPass(password)
                .build();
        JwtResponse response = authClient.authenticate(jwtRequest);

        ParametersUtil.setSecrets(username, password, response.getToken());
        return response;
    }

    public GrpcFile getRootFileInfo() {
        var request = FileInfoRequest.newBuilder()
                .setRoot(true)
                .build();
        SyncObserverOneResponse<GrpcFile> response = new SyncObserverOneResponse<>();
        repoClient.getFileInfo(request, response);
        return response.getResponse();
    }

    public void getSubFilesSync(GrpcFile currentFolder, AdapterItemsObserver<GrpcFile, ?> responseObserver) {
        var request = FileInfoRequest.newBuilder()
                .setFileId(currentFolder.getFileId())
                .build();

        repoClient.getSubFiles(request, responseObserver);
        responseObserver.waiting();
    }

    public void downloadFileSync(ShortInfo info, DownloadType downloadType) {
        DownloadFileReq req = DownloadFileReq.newBuilder()
                .setFileId(info.getFileId())
                .setType(downloadType)
                .build();

        DownloadFileObserver responseObserver = new DownloadFileObserver(info, downloadType);
        repoClient.downloadFile(req, responseObserver);
        responseObserver.waiting();
    }

    public void downloadFileAsync(ShortInfo info, DownloadType downloadType, Runnable onComplete) {
        DownloadFileReq req = DownloadFileReq.newBuilder()
                .setFileId(info.getFileId())
                .setType(downloadType)
                .build();

        DownloadFileObserver responseObserver = new DownloadFileObserver(info, downloadType, onComplete);
        repoClient.downloadFile(req, responseObserver);
    }

    public StreamObserverIterator<RespAlbumInfo> getAlbums(AlbumType albumType) {
        ReqAlbumInfo request = ReqAlbumInfo.newBuilder()
                .setAlbumType(albumType)
                .build();
        StreamObserverIterator<RespAlbumInfo> responseObserver = new StreamObserverIterator<>();
        mediaService.getAlbumInfo(request, responseObserver);
        return responseObserver;
    }

    public Iterator<ShortFaceDto> getFaces() {
        GetFacesReq req = GetFacesReq.newBuilder().build();
        StreamObserverIterator<ShortFaceDto> responseObserver = new StreamObserverIterator<>();
        mediaService.getFaces(req, responseObserver);
        return responseObserver;
    }

    public Iterator<DateCounterResponse> getMonthCounters() {
        ReqFilterMedias req = ReqFilterMedias.newBuilder().build();
        StreamObserverIterator<DateCounterResponse> responseObserver = new StreamObserverIterator<>();
        mediaService.findMediaFilesCounters(req, responseObserver);
        return responseObserver;
    }

    public StreamObserverIterator<ShortMediaInfoDto> getAllPhotos() {
        ReqFilterMedias request = ReqFilterMedias.newBuilder()
                .build();
        var observer = new StreamObserverIterator<ShortMediaInfoDto>();
        mediaService.findMediaFiles(request, observer);
        observer.waiting();
        return observer;
    }

    public void getPhotosByDate(OffsetDateTime start, OffsetDateTime end, PhotoObserver observer) {
        Log.i(TAG, "LOADING getPhotosByDate " + start.toString() + " TO " + end.toString());
        ReqFilterMedias request = ReqFilterMedias.newBuilder()
                .setDateStart(start.toString())
                .setDateEnd(end.toString())
                .build();
        //var observer = new StreamObserverIterator<ShortMediaInfoDto>();
        mediaService.findMediaFiles(request, observer);
    }
}
