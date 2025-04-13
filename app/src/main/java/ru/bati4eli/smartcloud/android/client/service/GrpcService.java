package ru.bati4eli.smartcloud.android.client.service;

import android.util.Log;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.FileInfoRequest;
import ru.bati4eli.mycloud.repo.FileUserRepoServiceGrpc;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.users.JwtRequest;
import ru.bati4eli.mycloud.users.JwtResponse;
import ru.bati4eli.mycloud.users.UserPrivateServiceGrpc;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

public class GrpcService {

    private UserPrivateServiceGrpc.UserPrivateServiceBlockingStub authClient;
    private FileUserRepoServiceGrpc.FileUserRepoServiceStub repoClient;

    public static GrpcService init() {
        return new GrpcService();
    }

    private GrpcService() {
        try {
            authClient = UserPrivateServiceGrpc.newBlockingStub(MiserableDI.get(ManagedChannel.class));
            repoClient = FileUserRepoServiceGrpc.newStub(MiserableDI.get(Channel.class));
        } catch (Exception e) {
            Log.e("serg", e.getLocalizedMessage());
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

    public void getRootFiles(FileAdapter fileAdapter) {
        var request = FileInfoRequest.newBuilder()
                .setRoot(true)
                .build();

        repoClient.getSubFiles(request, getGrpcFileStreamObserver(fileAdapter));
    }

    @NotNull
    private static StreamObserver<GrpcFile> getGrpcFileStreamObserver(FileAdapter fileAdapter) {
        return new StreamObserver<GrpcFile>() {
            @Override
            public void onNext(GrpcFile grpcFile) {
                Log.d("serg", grpcFile.getName());
                fileAdapter.add(grpcFile);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("serg", throwable.getLocalizedMessage());
            }

            @Override
            public void onCompleted() {
                Log.d("serg", "### COMPLETED!!!!");
                fileAdapter.notifyDataSetChanged();
            }
        };
    }

//    public void shutdown() {
//        channel.shutdown();
//    }
}
