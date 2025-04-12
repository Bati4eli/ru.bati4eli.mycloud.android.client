package ru.bati4eli.smartcloud.android.client.service;

import android.util.Log;
import io.grpc.ManagedChannel;
import lombok.var;
import ru.bati4eli.mycloud.repo.FileRepoService;
import ru.bati4eli.mycloud.repo.FileUserRepoServiceGrpc;
import ru.bati4eli.mycloud.users.UserPrivateServiceGrpc;
import ru.bati4eli.mycloud.users.UserService;
import ru.bati4eli.smartcloud.android.client.utils.MiserableDI;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.util.Iterator;

public class GrpcService {

    private ManagedChannel channel;
    private UserPrivateServiceGrpc.UserPrivateServiceBlockingStub authClient;
    private FileUserRepoServiceGrpc.FileUserRepoServiceBlockingStub repoClient;

    public static GrpcService init() {
        return new GrpcService();
    }

    public GrpcService() {
        try {
            channel = MiserableDI.get(ManagedChannel.class);
            authClient = UserPrivateServiceGrpc.newBlockingStub(channel);
            repoClient = FileUserRepoServiceGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            //setLabel(e.getMessage(), Color.RED);
            Log.e("serg", e.getLocalizedMessage());
        }
    }

    public UserService.JwtResponse authorize(String username, String password) {
        var jwtRequest = UserService.JwtRequest.newBuilder()
                .setLogin(username)
                .setPass(password)
                .build();
        UserService.JwtResponse response = authClient.authenticate(jwtRequest);

        ParametersUtil.setSecrets(username, password, response.getToken());
        return response;
    }

    public Iterator<FileRepoService.GrpcFile> getRootFiles() {
        var request = FileRepoService.FileInfoRequest.newBuilder()
                .setRoot(true)
                .build();
        return repoClient.getSubFiles(request);
    }

    public void shutdown() {
        channel.shutdown();
    }
}
