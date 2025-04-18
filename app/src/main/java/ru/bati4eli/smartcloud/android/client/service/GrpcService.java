package ru.bati4eli.smartcloud.android.client.service;

import android.util.Log;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.var;
import ru.bati4eli.mycloud.repo.DownloadFileReq;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.FileInfoRequest;
import ru.bati4eli.mycloud.repo.FileUserRepoServiceGrpc;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.users.JwtRequest;
import ru.bati4eli.mycloud.users.JwtResponse;
import ru.bati4eli.mycloud.users.UserPrivateServiceGrpc;
import ru.bati4eli.smartcloud.android.client.service.Observers.DownloadFileObserver;
import ru.bati4eli.smartcloud.android.client.service.Observers.GrpcFileStreamObserver;
import ru.bati4eli.smartcloud.android.client.service.Observers.SyncObserverOneResponse;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class GrpcService {

    private UserPrivateServiceGrpc.UserPrivateServiceBlockingStub authClient;
    private FileUserRepoServiceGrpc.FileUserRepoServiceStub repoClient;

    public static GrpcService init() {
        return new GrpcService();
    }

    private GrpcService() {
        try {
            authClient = UserPrivateServiceGrpc.newBlockingStub(MiserableDI.get(ManagedChannel.class));
            Channel channel = MiserableDI.get(Channel.class);
            repoClient = FileUserRepoServiceGrpc.newStub(channel);
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
        repoClient.getFileInfo(request,response);
        return response.getResponse();
    }

    public void getRootFilesSync(GrpcFileStreamObserver responseObserver) {
        var request = FileInfoRequest.newBuilder()
                .setRoot(true)
                .build();

        repoClient.getSubFiles(request, responseObserver);
        responseObserver.waiting();
    }

    public void getSubFilesSync(GrpcFile currentFolder, GrpcFileStreamObserver responseObserver) {
        var request = FileInfoRequest.newBuilder()
                .setFileId(currentFolder.getFileId())
                .build();

        repoClient.getSubFiles(request, responseObserver);
        responseObserver.waiting();
    }

    public void downloadFile(GrpcFile grpcFile, DownloadType downloadType) {

        DownloadFileReq req = DownloadFileReq.newBuilder()
                .setFileId(grpcFile.getFileId())
                .setType(downloadType)
                .build();

        DownloadFileObserver responseObserver = new DownloadFileObserver(grpcFile, downloadType);
        repoClient.downloadFile(req, responseObserver);
        responseObserver.waiting();
    }

}
