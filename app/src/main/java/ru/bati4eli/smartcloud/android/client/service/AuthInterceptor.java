package ru.bati4eli.smartcloud.android.client.service;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

public class AuthInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        Metadata addHeaders = new Metadata();
        Metadata.Key<String> authKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        addHeaders.put(authKey, "Bearer " + ParametersUtil.getToken());

        ClientCall<ReqT, RespT> call = next.newCall(method, CallOptions.DEFAULT);
        return new ClientCall<ReqT, RespT>() {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.merge(addHeaders); // Объединяем заголовки
                call.start(responseListener, headers);
            }

            @Override
            public void request(int numMessages) {
                call.request(numMessages);
            }

            @Override
            public void cancel(String message, Throwable cause) {
                call.cancel(message, cause);
            }

            @Override
            public void halfClose() {
                call.halfClose();
            }

            @Override
            public void sendMessage(ReqT message) {
                call.sendMessage(message);
            }
        };
    }
}
